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

package x10.ast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import polyglot.main.Report;
import polyglot.util.Position;
import polyglot.util.TypedList;
import polyglot.visit.ContextVisitor;
import polyglot.visit.NodeVisitor;
import x10.types.Context;
import x10.types.ParameterType;
import x10.types.Ref;
import x10.types.SemanticException;
import x10.types.Type;
import x10.types.X10Context;
import x10.types.X10MethodDef;
import x10.types.X10TypeSystem;

/**
 * Captures the commonality of foreach and ateach loops in X10.
 * @author Igor Peshansky
 */
public abstract class X10ClockedLoop_c extends X10Loop_c implements Clocked {

	protected List<Expr> clocks;

	/**
	 * @param pos
	 */
	public X10ClockedLoop_c(Position pos) {
		super(pos);
	}

	/**
	 * @param pos
	 * @param formal
	 * @param domain
	 * @param clocks
	 * @param body
	 */
	public X10ClockedLoop_c(Position pos, Formal formal, Expr domain,
							List<Expr> clocks, Stmt body)
	{
		super(pos, formal, domain, body);
		this.clocks = TypedList.copyAndCheck(clocks, Expr.class, true);
	}
	public X10ClockedLoop_c(Position pos, Formal formal, Expr domain,
			Stmt body)
	{
		super(pos, formal, domain, body);
		// TODO: The clock had to be obtained from the environment in the desugarer and added here.
		this.clocks = new ArrayList<Expr>();
	}

	/** Clocks */
	public List<Expr> clocks() {
		return Collections.unmodifiableList(this.clocks);
	}

	/** Set clocks */
	public Clocked clocks(List<Expr> clocks) {
		X10ClockedLoop_c n = (X10ClockedLoop_c) copy();
		n.clocks = TypedList.copyAndCheck(clocks, Expr.class, true);
		return n;
	}

	public Node visitChildren(NodeVisitor v) {
		Formal formal = (Formal) visitChild(this.formal, v);
		Expr domain = (Expr) visitChild(this.domain, v);
		List<Expr> clocks = visitList(this.clocks, v);
		Stmt body = (Stmt) visitChild(this.body, v);
		return ((Clocked) reconstruct(formal, domain, body)).clocks(clocks);
	}

	public Context enterChildScope(Node child, Context c) {
	    if (child == this.body) {
	        return AtStmt_c.createDummyAsync(c,false); // only subclass is AtEach (so it is an at, not an async)
	    }
	    return c;
	}

	
	public Node typeCheck(ContextVisitor tc) throws SemanticException {
		X10TypeSystem ts = (X10TypeSystem) tc.typeSystem();
	        for (Expr clock : (List<Expr>) clocks) {
	            if (! ts.isImplicitCastValid(clock.type(), ts.Clock(), tc.context())) {
	        	throw new SemanticException("Clocked loop may only be clocked on a clock.", clock.position());
	            }
	        }
	        
		return super.typeCheck(tc);
	}
}
