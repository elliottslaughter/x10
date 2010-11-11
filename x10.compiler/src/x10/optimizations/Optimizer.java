/*
 *  This file is part of the X10 project (http://x10-lang.org).
 *
 *  This file is licensed to You under the Eclipse Public License (EPL);
 *  You may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *      http://www.opensource.org/licenses/eclipse-1.0.php
 *
 *  (C) Copyright IBM Corporation 2006-2010.
 */

package x10.optimizations;

import java.util.ArrayList;
import java.util.List;

import polyglot.ast.NodeFactory;
import polyglot.frontend.ExtensionInfo;
import polyglot.frontend.Goal;
import polyglot.frontend.Job;
import polyglot.frontend.Scheduler;
import polyglot.types.TypeSystem;
import polyglot.visit.NodeVisitor;
import x10.ExtensionInfo.X10Scheduler.ValidatingVisitorGoal;
//  import x10.visit.DeadVariableEliminator;
import x10.visit.Inliner;

public class Optimizer {

    private final Scheduler scheduler;

    public Optimizer(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    public static List<Goal> goals(Scheduler scheduler, Job job, Goal flattener) {
        return new Optimizer(scheduler).goals(job, flattener);
    }

    public List<Goal> goals(Job job, Goal flattener) {
        List<Goal> goals = new ArrayList<Goal>();
        goals.add(LoopUnrolling(job));
        goals.add(ForLoopOptimizations(job));
        goals.add(Inliner(job));
//        if (!x10.Configuration.FLATTEN_EXPRESSIONS) goals.add(flattener); // don't add it twice
        if (x10.Configuration.EXPERIMENTAL) {
  //        goals.add(DeadAssignmentEliminator(job));
  //        DeadAssignmentEliminator(job).addPrereq(flattener);
        }
        // TODO: add an empty goal that prereqs the above
        return goals;
    }

    public Goal ForLoopOptimizations(Job job) {
        ExtensionInfo extInfo = job.extensionInfo();
        TypeSystem ts = extInfo.typeSystem();
        NodeFactory nf = extInfo.nodeFactory();
        return new ValidatingVisitorGoal("For Loop Optimizations", job, new ForLoopOptimizer(job, ts, nf)).intern(scheduler);
    }

    public Goal LoopUnrolling(Job job) {
        ExtensionInfo extInfo = job.extensionInfo();
        TypeSystem ts = extInfo.typeSystem();
        NodeFactory nf = extInfo.nodeFactory();
        return new ValidatingVisitorGoal("Loop Unrolling", job, new LoopUnroller(job, ts, nf)).intern(scheduler);
    }
/*
    public Goal DeadAssignmentEliminator(Job job) {
        ExtensionInfo extInfo = job.extensionInfo();
        TypeSystem ts = extInfo.typeSystem();
        NodeFactory nf = extInfo.nodeFactory();
        NodeVisitor visitor = new DeadVariableEliminator(job, ts, nf);
        return new ValidatingVisitorGoal("Dead Assignment Elimination", job, visitor).intern(scheduler);
    }
*/
    public Goal Inliner(Job job) {
        ExtensionInfo extInfo = job.extensionInfo();
        TypeSystem ts = extInfo.typeSystem();
        NodeFactory nf = extInfo.nodeFactory();
        return new ValidatingVisitorGoal("Inlined", job, new Inliner(job, ts, nf)).intern(scheduler);
    }

}
