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

package x10.types;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import polyglot.ast.Expr;
import polyglot.types.ClassDef;
import polyglot.types.ClassType;
import polyglot.types.ConstructorInstance;
import polyglot.types.ErrorRef_c;
import polyglot.types.FieldInstance;
import polyglot.types.Flags;
import polyglot.types.Matcher;
import polyglot.types.MemberInstance;
import polyglot.types.MethodInstance;
import polyglot.types.Named;
import polyglot.types.ParsedClassType_c;
import polyglot.types.Ref;
import polyglot.types.SemanticException;
import polyglot.types.Type;
import polyglot.types.TypeObject;
import polyglot.types.TypeSystem;
import polyglot.util.InternalCompilerError;
import polyglot.util.Position;
import polyglot.util.Transformation;
import polyglot.util.TransformingList;
import polyglot.util.TypedList;
import x10.constraint.XFailure;
import x10.constraint.XVar;
import x10.types.constraints.CConstraint;
import x10.types.matcher.Subst;

/** 08/11/09 
 * 
 * An X10ParsedClassType_c represents a type C[T1,..., Tn], where
 * C is a class and T1,..., Tn are type parameters. Almost all the information
 * supplied by an X10ParsedClassType_c is obtained from the x10ClassDef(), a reference
 * to the ClassDef object with which this object is created and that represents the
 * underlying class definition.
 * 
 * <p> This class is used to represent information parsed from both structs and classes.
 * 
 * @author vj
 */
public class X10ParsedClassType_c extends ParsedClassType_c
implements X10ParsedClassType
{
    TypeParamSubst cacheSubst; // "subst" is just an auxiliary structure (cached to improve performance). It represents the typeArguments (thus it is nullified when assigning to typeArguments).

    public int hashCode() {
        return def.hashCode();
    }
    
    @Override
    public boolean equalsImpl(TypeObject o) {
        if (o == this)
            return true;
        if (o == null)
            return false;
        if (o instanceof X10ParsedClassType_c) {
            X10ParsedClassType_c t = (X10ParsedClassType_c) o;
            
            if (! flags().equals(t.flags()))
            	return false;
            if (def != t.def) {
                if (def == null || t.def == null)
                    return false;
                else if (! def.equals(t.def))
                    return false;
            }
            
            if (typeArguments != t.typeArguments) {
                if (typeArguments == null)
                    return t.typeArguments == null || t.typeArguments.isEmpty();
                else if (t.typeArguments == null)
                    return typeArguments.isEmpty();
                else if (! typeArguments.equals(t.typeArguments))
                    return false;
            }

            return true;
        }
        return false;
    }
    
    public
    TypeParamSubst subst() {
        if (cacheSubst == null)
            cacheSubst = new TypeParamSubst((X10TypeSystem) ts, typeArguments, x10Def().typeParameters());
        return cacheSubst;
    }
    
    public X10ParsedClassType_c(ClassDef def) {
        super(def);
        cacheSubst = null;
    }

    public X10ParsedClassType_c(TypeSystem ts, Position pos, Ref<? extends ClassDef> def) {
        super(ts, pos, def);
        cacheSubst = null;
    }
 
	public Type setFlags(Flags f) {
		X10Flags xf = X10Flags.toX10Flags(f);
		X10ParsedClassType_c c = (X10ParsedClassType_c) this.copy();
		c.flags = flags().set(f);
		/*if  (xf.isRooted() || xf.isStruct()) {
			X10ParsedClassType_c c = (X10ParsedClassType_c) this.copy();
			if (c.flags == null)
				c.flags = X10Flags.toX10Flags(c.def().flags());
			if (c.flags == null)
				c.flags = X10Flags.toX10Flags(Flags.NONE);
			f.s
			c.flags = xf.isRooted() 
			? (xf.isStruct() ? X10Flags.toX10Flags(c.flags).Rooted().Struct() 
					: X10Flags.toX10Flags(c.flags).Rooted())
					: ((xf.isStruct()) ? X10Flags.toX10Flags(c.flags).Struct() : c.flags);
			return c;
		}*/
		return c;
	}
	
	
    /** Property initializers, used in annotations. */
    List<Expr> propertyInitializers;
    public List<Expr> propertyInitializers() {
        if (propertyInitializers == null)
            return Collections.EMPTY_LIST;
        return Collections.unmodifiableList(propertyInitializers);
    }
    public Expr propertyInitializer(int i) {
        return propertyInitializers().get(i);
    }
    public X10ClassType propertyInitializers(List<Expr> inits) {
        X10ParsedClassType_c  n = (X10ParsedClassType_c) copy();
        n.propertyInitializers = inits;
        return n;
    }

    public List<Type> annotations() {
        return X10TypeObjectMixin.annotations(this);
    }

    public List<Type> annotationsMatching(Type t) {
        return X10TypeObjectMixin.annotationsMatching(this, t);
    }
    protected SemanticException realClauseInvalid;
	
    public X10ClassDef x10Def() {
        return (X10ClassDef) def();
    }

	public boolean isJavaType() {
	    return def().fromJavaClassFile();
	}
	
	public boolean hasParams() {
	    return x10Def().typeParameters() != null && x10Def().typeParameters().size() != 0;
	}
	
	@Override
	public Type superClass() {
	    Type sup = super.superClass();
	    Type base = X10TypeMixin.baseType(sup);
	    if (base instanceof X10ClassType) {
	        XVar supVar = ((X10ClassType) base).x10Def().thisVar();
	        XVar thisVar = x10Def().thisVar();
	        try {
	            sup = Subst.subst(sup, thisVar, supVar);
	        }
	        catch (SemanticException e) {
	        }
	    }
	    if (!hasParams())
		return sup;
	    TypeParamSubst subst = subst();
	    return subst.reinstantiate(sup);
	}
	
	@Override
	public List<Type> interfaces() {
	    List<Type> interfaces = super.interfaces();
	    List<Type> newInterfaces = new ArrayList<Type>(interfaces.size());
	    for (Type sup : interfaces) {
	        Type base = X10TypeMixin.baseType(sup);
	        if (base instanceof X10ClassType) {
	            XVar supVar = ((X10ClassType) base).x10Def().thisVar();
	            XVar thisVar = x10Def().thisVar();
	            try {
	                sup = Subst.subst(sup, thisVar, supVar);
	            }
	            catch (SemanticException e) {
	            }
	        }
	        newInterfaces.add(sup);
	    }

	    if (!hasParams())
	        return newInterfaces;
	    TypeParamSubst subst = subst();
	    return subst.reinstantiate(newInterfaces);
	}

	public boolean isIdentityInstantiation() {
	    if (!hasParams())
		return true;
            TypeParamSubst subst = subst();
	    return subst.isIdentityInstantiation();
	}

	@Override
	public List<FieldInstance> fields() {
	    if (!hasParams())
	        return super.fields();
            TypeParamSubst subst = subst();
	    return subst.reinstantiate(super.fields());
	}

	@Override
	public List<MethodInstance> methods() {
	    if (!hasParams())
	        return super.methods();
            TypeParamSubst subst = subst();
	    return subst.reinstantiate(super.methods());
	}
	@Override
	public List<ConstructorInstance> constructors() {
	    if (!hasParams())
	        return super.constructors();
            TypeParamSubst subst = subst();
	    return subst.reinstantiate(super.constructors());
	}
	@Override
	public List<MemberInstance<?>> members() {
	    if (!hasParams())
	        return super.members();
            TypeParamSubst subst = subst();
	    return subst.reinstantiate(super.members());
	}
	@Override
	public List<ClassType> memberClasses() {
	    if (!hasParams())
	        return super.memberClasses();
            TypeParamSubst subst = subst();
	    return subst.reinstantiate(super.memberClasses());
	}

	/**
	 * A parsed class is safe iff it explicitly has a flag saying so.
	 */
	public boolean isSafe() {
		return X10Flags.toX10Flags(flags()).isSafe();
	}

	public static class X10FieldAsTypeTransform implements Transformation<X10FieldDef, FieldInstance> {
	    public FieldInstance transform(X10FieldDef def) {
		return def.asInstance();
	    }
	}

	public List<FieldInstance> definedProperties() {
	    return new TransformingList<X10FieldDef, FieldInstance>(x10Def().properties(), new X10FieldAsTypeTransform());
	}

	// TODO: vj 08/11/09. Why are properties not obtained through the ClassDef, just like
	// other class members?
	public List<FieldInstance> properties() {
	    Type superType = superClass();
	    if (superType instanceof X10ClassType) {
	        List<FieldInstance> l = ((X10ClassType) superType).properties();
	        List<FieldInstance> l2 = new ArrayList<FieldInstance>();
	        l2.addAll(l);
	        l2.addAll(definedProperties());
	        return l2;
	    }
	    return definedProperties();
	}
	
	public List<Type> typeMembers() {
	    List<Type> l = new TransformingList<TypeDef, Type>(x10Def().memberTypes(), new TypeDefAsMacroTypeTransform());
	    if (!hasParams())
	        return l;
            TypeParamSubst subst = subst();
	    return subst.reinstantiate(l);
	}
	
	List<Type> typeArguments;
	
	public List<Type> typeArguments() {
	    if (typeArguments == null) {
	       TypedList x = TypedList.copyAndCheck((List) x10Def().typeParameters(), Type.class, true);
	       return x;
	    }
	    return typeArguments;
	}
	
	/**
	 * thisVar is set based on the types used to instantiate this type. All
	 * the types used must agree on their thisVar. That is the thisVar
	 * for this type.
	 */
	public X10ParsedClassType typeArguments(List<Type> typeArgs) {
	    X10ParsedClassType_c n = (X10ParsedClassType_c) copy();
	    n.typeArguments = TypedList.copyAndCheck(typeArgs, Type.class, false);
	    try {
	    n.thisVar = X10TypeMixin.getThisVar(typeArgs);
	    } catch (XFailure z) {
	    	throw new InternalCompilerError(z.toString() + " for type " + this);
	    }
	    n.cacheSubst = null;
	    return n;
	}
	
	@Override
	public Named memberTypeMatching(Matcher<Named> matcher) {
	    Named n = super.memberTypeMatching(matcher);
	    if (n != null)
	        return n;
	    
	    n = typeMemberMatching(matcher);
	    if (n != null)
	        return n;
	    
	    return null;
	}
	
	public MacroType typeMemberMatching(Matcher<Named> matcher) {
	    for (Type t : typeMembers()) {
	        if (t instanceof MacroType) {
	            MacroType mt = (MacroType) t;
	            try {
	                Named n = matcher.instantiate(mt);
	                if (n instanceof MacroType)
	                    return (MacroType) n;
	            }
	            catch (SemanticException e) {
	            }
		}
	    }
	    
	    return null;
	}
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		if (flags() != null) {
			X10Flags f = X10Flags.toX10Flags(flags());
			if (f.isProto()) {
				sb.append("proto ");
			}

		}
		//	sb.append(flags().toString()).append(" ");

		String sup = super.toString();
		sb.append(sup);

		if (propertyInitializers != null) {
			String s = propertyInitializers.toString();
			sb.append("(").append(s.substring(1, s.length()-1)).append(")");
			return sb.toString();
		}
		if (typeArguments != null && ! typeArguments.isEmpty()) {
			sb.append(typeArguments.toString());

		}
		return sb.toString();
	}
	    
	public boolean isProto() { return X10Flags.toX10Flags(flags()).isProto(); }
	public boolean isX10Struct() { 	return X10Flags.toX10Flags(flags()).isStruct(); }
    public Proto makeProto() {
    	if (isProto())
    		return this;
    	X10ParsedClassType_c c = (X10ParsedClassType_c) copy();
    	c.setFlags(X10Flags.toX10Flags(flags()).Proto());
    	return c;
    	
    }
    public Proto baseOfProto() {
    	if (! isProto())
    		return this;
    	X10ParsedClassType_c c = (X10ParsedClassType_c) copy();
    	c.setFlags(X10Flags.toX10Flags(flags()).clearProto());
    	return c;
    }
    public X10Struct makeX10Struct() {
    	if (isX10Struct())
    		return this;
    	X10ParsedClassType_c c = (X10ParsedClassType_c) copy();
    	c.setFlags(X10Flags.toX10Flags(flags()).Struct());
    	return c;
    	
    }
    
	public boolean equalsNoFlag(Type o) {
		if (! (o instanceof X10ParsedClassType_c))
			return false;
		X10ParsedClassType_c  other = (X10ParsedClassType_c) o;
		return this == o;
		
	}
	
	XVar thisVar;
	public XVar thisVar() {
		return thisVar;
	}
	
	CConstraint xClause; //todo: is it mutated? it it used?
	
	public CConstraint getXClause() {
		if (xClause == null) {
			xClause = new CConstraint();
			try {
			xClause.setThisVar(X10TypeMixin.getThisVar(typeArguments()));
			} catch (XFailure f) {
				xClause.setInconsistent();
			}
		}
		return xClause;
	}

	public boolean isValid() {
		return !(def instanceof ErrorRef_c<?>);
	}
}
