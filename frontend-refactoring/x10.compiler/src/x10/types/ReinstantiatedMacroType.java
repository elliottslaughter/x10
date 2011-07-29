/**
 * 
 */
package x10.types;

import java.util.List;

import polyglot.util.Position;
import x10.types.constraints.CConstraint;

public final class ReinstantiatedMacroType extends MacroType_c {
	private static final long serialVersionUID = 198078123816586742L;

	private final TypeParamSubst typeParamSubst;
	private final MacroType fi;

	public ReinstantiatedMacroType(TypeParamSubst typeParamSubst, TypeSystem ts, Position pos,
			Ref<TypeDef> def, MacroType fi) {
		super(ts, pos, def);
		this.typeParamSubst = typeParamSubst;
		this.fi = fi;
	}

	@Override
	public Ref<? extends Type> returnTypeRef() {
		if (definedType == null)
			return this.typeParamSubst.reinstantiate(fi.returnTypeRef());
		return definedType;
	}

	@Override
	public Type returnType() {
		if (definedType == null)
			return this.typeParamSubst.reinstantiate(fi.returnType());
		return definedType.get();
	}

	@Override
	public Ref<? extends Type> definedTypeRef() {
		if (definedType == null)
			return this.typeParamSubst.reinstantiate(fi.definedTypeRef());
		return definedType;
	}

	@Override
	public Type definedType() {
		if (definedType == null)
			return this.typeParamSubst.reinstantiate(fi.definedType());
		return definedType.get();
	}

	@Override
	public List<Type> formalTypes() {
		if (formalTypes == null)
			return this.typeParamSubst.reinstantiate(fi.formalTypes());
		return formalTypes;
	}

	@Override
	public CConstraint guard() {
		if (guard == null)
			return this.typeParamSubst.reinstantiate(fi.guard());
		return guard;
	}
}