package x10.visit;

import polyglot.ast.AmbAssign;
import polyglot.ast.Ambiguous;
import polyglot.ast.Assign_c;
import polyglot.ast.FlagsNode_c;
import polyglot.ast.Node;
import polyglot.ast.LocalDecl;
import polyglot.ast.FieldDecl;
import polyglot.ast.Typed;
import polyglot.ast.ClassMember;
import polyglot.ast.VarDecl;
import polyglot.ast.ProcedureCall;
import polyglot.ast.NamedVariable;
import polyglot.ast.FieldAssign;
import polyglot.util.Position;
import polyglot.util.ErrorInfo;
import polyglot.visit.NodeVisitor;
import polyglot.frontend.Job;
import polyglot.main.Report;
import polyglot.types.SemanticException;
import x10.ast.AnnotationNode_c;
import x10.ast.DepParameterExpr;
import x10.ast.X10Formal_c;
import x10.ast.Closure;
import x10.ast.AssignPropertyBody;
import x10.ast.SettableAssign;
import x10.errors.X10ErrorInfo;

public class InstanceInvariantChecker extends NodeVisitor
{
    private Job job;

    public InstanceInvariantChecker(Job job) {
        this.job = job;
    }

    public Node visitEdgeNoOverride(Node parent, Node n) {
    	if (Report.should_report("InstanceInvariantChecker", 2))
    		Report.report(2, "Checking invariants for: " + n);
    	String m = checkInvariants(n);

    	if (m!=null) {
    		String msg = m+("!")+(" n=")+(n).toString();
    		job.compiler().errorQueue().enqueue(X10ErrorInfo.INVARIANT_VIOLATION_KIND,msg,n.position());
    	}

    	n.del().visitChildren(this); // if there is an error, I don't recurse to the children
    	return n;
    }
    
    private boolean isAmbiguous(Node n){
    	if (n instanceof Ambiguous && !(n instanceof DepParameterExpr)
    			&&(!(n instanceof Assign_c) || n instanceof AmbAssign)) {
    			return true;
    	}
    	return false;
    }
    private void myAssert(boolean cond, String msg) throws SemanticException {
        if (!cond)
            throw new SemanticException(msg);
    }
    
    private String checkInvariants(Node n) {
        if (n == null) return "Cannot visit null";

        if (isAmbiguous(n)) return "Ambiguous node found in AST";

        if (n instanceof Typed) {
            if (((Typed)n).type()==null) return "Typed node is missing type";
        } else if (n instanceof ClassMember) {
            if (((ClassMember)n).memberDef()==null) return "ClassMember missing memberDef";
        } else if (n instanceof VarDecl) {
            if (((VarDecl)n).localDef()==null) return "VarDecl missing localDef";
        } else if (n instanceof ProcedureCall) {
            if (((ProcedureCall)n).procedureInstance()==null) return "ProcedureCall missing procedureInstance";
        } else if (n instanceof NamedVariable) {
            if (((NamedVariable)n).varInstance()==null) return "NamedVariable missing varInstance";
        } else if (n instanceof FieldAssign) {
            if (((FieldAssign)n).fieldInstance()==null) return "FieldAssign missing fieldInstance";
        }
        // x10 specific
        else if (n instanceof Closure) {
            if (((Closure)n).closureDef()==null) return "Closure missing closureDef";
        } else if (n instanceof AssignPropertyBody) {
            if (((AssignPropertyBody)n).constructorInstance()==null) return "AssignPropertyBody missing constructorInstance";
        } else if (n instanceof SettableAssign) {
            if (((SettableAssign)n).methodInstance()==null) return "SettableAssign missing methodInstance";
        }
        return null;
    }
}