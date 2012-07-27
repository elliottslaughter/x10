package x10.constraint;
/**
 * XSimpleOp is a representation of simple operators i.e. operators that are not
 * parameterized (such as =, && etc.). It is essentially a wrapper around a subset 
 * of the Kind enum values. 
 *  
 * @author lshadare
 *
 */
public class XSimpleOp<T extends XType> extends XOp<T> {
	/**
	 * Constructs a simple operator with the given kind. Note that the
	 * kind must not be a kind that is parameterized. 
	 * @param kind
	 */
	XSimpleOp(XOp.Kind kind) {
		super(kind);
		assert kind!= XOp.Kind.APPLY && kind != XOp.Kind.TAG; 
	}

	@Override
	public T type(XTypeSystem<? extends T> ts) {
		return ts.Boolean();
	}

	@Override
	public T type() {
		throw new UnsupportedOperationException("Need to pass in a TypeSystem to deduce the type of a XSimpleOp");
	}

	@Override
	public String toString() {
		return kind.name();
	}

	@Override
	public String prettyPrint() {
		return getKind().prettyPrint();
	}
	
	
}
