/*
 * Created on Oct 1, 2004
 */

package polyglot.ext.x10.types;

import polyglot.frontend.Source;
import polyglot.types.*;

/**
 * Parts of this code are taken from the pao extension in the polyglot framework.
 * 
 * @author Christoph von Praun
 * @author vj
 */

public interface X10TypeSystem extends TypeSystem {

    
    public ParsedClassType getRuntimeType();
    
    public ParsedClassType getActivityType();

    public ParsedClassType getFutureActivityType();
    
    public ParsedClassType getFutureType();
    
    public ParsedClassType getX10ObjectType();
    
    public ParsedClassType getPlaceType();
    
    /** Return the method instance for runtime.Primitive.equals */
    public MethodInstance primitiveEquals();

    /** Return the method instance for runtime.T.tValue() */
    public MethodInstance getter(PrimitiveType t);

    /** Return the constructor instance for runtime.T.T(t) */
    public ConstructorInstance wrapper(PrimitiveType t);

    /** Return boxed type runtime.T for primitive t. */
    public Type boxedType(PrimitiveType t);
    
} // end of X10TypeSystem
