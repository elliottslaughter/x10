/*
 * Created on Nov 30, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package polyglot.ext.x10.types;

import polyglot.ext.jl.types.ArrayType_c;
import polyglot.main.Report;
import polyglot.types.Type;
import polyglot.types.TypeSystem;
import polyglot.util.Position;

/**
 * @author vj
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class X10ArrayType_c extends ArrayType_c implements X10ArrayType {

	/**
	 * 
	 */
	public X10ArrayType_c() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param ts
	 * @param pos
	 * @param base
	 */
	public X10ArrayType_c(TypeSystem ts, Position pos, Type base) {
		super(ts, pos, base);
		// TODO Auto-generated constructor stub
	}

//	 ----------------------------- begin manual mixin code from X10Type_c
	/* (non-Javadoc)
	 * @see polyglot.ext.x10.types.X10Type#isNullable()
	 */
	public boolean isNullable() {
			return false;
	}

	/* (non-Javadoc)
	 * @see polyglot.ext.x10.types.X10Type#isFuture()
	 */
	public boolean isFuture() {
		return false;
	}

	/* (non-Javadoc)
	 * @see polyglot.ext.x10.types.X10Type#toNullable()
	 */
	public NullableType toNullable() {
			return null;
	}

	/* (non-Javadoc)
	 * @see polyglot.ext.x10.types.X10Type#toFuture()
	 */
	public FutureType toFuture() {
			return null;
	}

	
	public  boolean isSubtypeImpl( Type t) {
    	X10Type target = (X10Type) t;
    	
    	if (Report.should_report("debug", 5))
			Report.report( 5, "[X10UnknownType_c] isSubTypeImpl |" + this +  "| of |" + t + "|?");	
    	
    	boolean result = ts.equals(this, target) || ts.descendsFrom(this, target);
    	
       	if (result) {
       		if (Report.should_report("debug", 5))
    			Report.report( 5, "[X10UnknownType_c] ..." + result+".");	
     		return result;
    	}
    	if (target.isNullable()) {
    		NullableType toType = target.toNullable();
    		Type baseType = toType.base();
    		result = isSubtypeImpl( baseType );
    		if (Report.should_report("debug", 5))
    			Report.report( 5, "[X10UnknownType_c] ..." + result+".");	
    		return result;
    	}
    	if (Report.should_report("debug", 5))
			Report.report( 5, "[X10UnknownType_c] ..." + result+".");	
    	return false;
    }
	// ----------------------------- end manual mixin code from X10Type_c
	
	
}
