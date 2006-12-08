package polyglot.ext.x10.extension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import polyglot.ast.Call;
import polyglot.ast.Cast;
import polyglot.ast.Expr;
import polyglot.ast.New;
import polyglot.ast.Node;
import polyglot.ast.NodeFactory;
import polyglot.ext.x10.ast.DepCast;
import polyglot.ext.x10.ast.ParExpr;
import polyglot.ext.x10.ast.X10Cast;
import polyglot.ext.x10.ast.X10NodeFactory;
import polyglot.ext.x10.types.X10PrimitiveType;
import polyglot.ext.x10.types.X10TypeSystem;
import polyglot.frontend.ExtensionInfo;
import polyglot.types.ConstructorInstance;
import polyglot.types.MethodInstance;
import polyglot.types.Type;

public class X10CastExt_c extends X10Ext_c {
	// Insert boxing and unboxing code.
	public Node rewrite(X10TypeSystem ts, NodeFactory nf, ExtensionInfo info) {

		Cast c = (Cast) node();
		Type rtype = c.expr().type();
		Type ltype = c.castType().type();
		X10NodeFactory xnf = (X10NodeFactory) nf;

		if (!ts.equals(c.type(), ltype)) 
			c = (Cast) c.type(ltype);
		
		if (ltype.isPrimitive() && rtype.isReference()) {
			// Unbox e.g. (int) a, where a's type is BoxedInteger.
			// replace with a.intValue
			MethodInstance mi = ts.getter((X10PrimitiveType) ltype.toPrimitive());
			
			// building new cast, the toType is the boxed primitive one
			Cast x = (Cast) nf.Cast(c.position(),
					 nf.CanonicalTypeNode(c.position(), mi.container()),
					 c.expr());
			x = (Cast) x.type(mi.container());
			
			// adding parenthesis to surround cast operation
			ParExpr px = ((X10NodeFactory)nf).ParExpr(x.position(), x);
			px = (ParExpr) px.type(x.type());
			
			// building unboxing method call
			Call y = nf.Call(c.position(), px, mi.name(),
							 Collections.EMPTY_LIST);
			y = (Call) y.type(mi.returnType());

			Node rewrittenNode = y.methodInstance(mi);
			if (c instanceof DepCast) {
				// Keep the DepCast since the condition needs to be checked.
				rewrittenNode = c.expr((Expr) rewrittenNode);
			}
			
			return rewrittenNode;
		}
		else if (ltype.isReference() && rtype.isPrimitive()) {
			// Box
			ConstructorInstance ci = ts.wrapper((X10PrimitiveType) rtype.toPrimitive());

			List args = new ArrayList(1);
			args.add(c.expr());

			New x = nf.New(c.position(),
						   nf.CanonicalTypeNode(c.position(), ci.container()),
						   args);
			x = (New) x.type(ci.container());
			return x.constructorInstance(ci);
		}

		return c;
	}
}

