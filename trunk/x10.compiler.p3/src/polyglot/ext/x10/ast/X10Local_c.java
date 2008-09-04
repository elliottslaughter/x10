/*
 *
 * (C) Copyright IBM Corporation 2006
 *
 *  This file is part of X10 Language.
 *
 */
package polyglot.ext.x10.ast;

/**
 * Immutable representation of a local variable access. 
 * Introduced to add X10 specific type checks. A local variable accessed
 * in a deptype must be final.
 * 
 * @author vj
 */
import polyglot.ast.Id;
import polyglot.ast.Local_c;
import polyglot.ast.Node;
import polyglot.ext.x10.types.X10Context;
import polyglot.ext.x10.types.X10Flags;
import polyglot.ext.x10.types.X10ProcedureDef;
import polyglot.ext.x10.types.X10TypeMixin;
import polyglot.ext.x10.types.X10TypeSystem;
import polyglot.types.CodeDef;
import polyglot.types.Context;
import polyglot.types.LocalInstance;
import polyglot.types.SemanticException;
import polyglot.types.Type;
import polyglot.types.Types;
import polyglot.types.UnknownType;
import polyglot.types.VarDef;
import polyglot.util.Position;
import polyglot.visit.ContextVisitor;
import x10.constraint.XConstraint;
import x10.constraint.XConstraint_c;
import x10.constraint.XFailure;
import x10.constraint.XLocal;
import x10.constraint.XTerm;
import x10.constraint.XVar;

public class X10Local_c extends Local_c {

	public X10Local_c(Position pos, Id name) {
		super(pos, name);
		
	}
	public Node typeCheck(ContextVisitor tc) throws SemanticException {
		
		try {
			Context context = tc.context();
			LocalInstance li = context.findLocal(name.id());

			// if the local is defined in an outer class, then it must be final
			if (!context.isLocal(li.name())) {
				// this local is defined in an outer class
				if (!li.flags().isFinal() && !X10Flags.toX10Flags(li.flags()).isShared()) {
					throw new SemanticException("Local variable \"" + li.name() + 
							"\" is accessed from an inner class or a closure, and must be declared final or shared.",
							this.position());                     
				}
			}

			X10Local_c result = (X10Local_c) super.typeCheck(tc);
			
			// Permit occurrences of local variables in the type of the variable.
			X10Context xtc = (X10Context) tc.context();
			VarDef dli = xtc.varWhoseTypeIsBeingElaborated();
			if (xtc.inDepType()) {
				li = result.localInstance();
				if (! (li.equals(dli)) && ! li.flags().isFinal()) {
					throw new SemanticError("Local variable " + li.name() 
							+ " must be final in a dependent clause.", 
							position());
				}
			}
			
			// Add in self==x to local variable x.
			if (result.localInstance().flags().isFinal()) {
			    Type t = result.type();
			    if (! (t instanceof UnknownType)) {
			        XConstraint c = X10TypeMixin.xclause(t);
			        c = (c == null) ? new XConstraint_c() : c.copy();
			        X10TypeSystem xts = (X10TypeSystem) tc.typeSystem();
			        try {
			            XLocal resultTerm = xts.xtypeTranslator().trans(c, result);
			            c.addSelfBinding(resultTerm);
			            t = X10TypeMixin.xclause(t, c);
			            result = (X10Local_c) result.type(t);
			        }
			        catch (SemanticException e) {
			        }
			    }
			}
			
			// Fold in the method's guard.
			CodeDef ci = xtc.currentCode();
			if (ci instanceof X10ProcedureDef) {
			    X10ProcedureDef pi = (X10ProcedureDef) ci;
				XConstraint c = Types.get(pi.guard());
				if (c != null) {
					X10TypeSystem xts = (X10TypeSystem) tc.typeSystem();

					// Substitute self for x (this local) in the guard.
//					C_Var var = new TypeTranslator(xts).trans(localInstance());
//        			Promise p = c.intern(var);
//        			System.out.println("before = " + c);
//        			c = c.substitute(p.term(), C_Special.Self);
//        			System.out.println("after = " + c);

      			    // Add the guard into the constraint for this type. 
        			Type t = result.type();

        			XConstraint dep = X10TypeMixin.xclause(t);
        			if (dep == null) dep = new XConstraint_c();
        			else dep = dep.copy();
//        			XTerm resultTerm = xts.xtypeTranslator().trans(result);
//        			dep.addSelfBinding((XVar) resultTerm);
        			dep.addIn(c);
        			
        			t = X10TypeMixin.xclause(t, dep);
        			
					return result.type(t);
				}
			}
			
			return result;
		} catch (SemanticException z) {
//			if (tc instanceof TypeElaborator && !(z instanceof SemanticError)) {
//				// Ignore semantic exceptions that may arise during TypeElaboration. The
//				// field being referenced may not exist because of an MDE -- e.g. its type
//				// does not yet have its signature resolved. 
//				if (Report.should_report("types", 2)) 
//					Report.report(2, "X10Local_c: " + this 
//							+ " encountered exception " + z + " during " + tc + "; being ignored.");
//				return this;
//			} else {
				throw z;
//			}
		}
		catch (XFailure e) {
			throw new SemanticException(e.getMessage(), position());
		}
	}
	
}
