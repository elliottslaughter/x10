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

import java.util.List;

import x10.ast.Expr;

/** The representative of ClassType in the X10 type hierarchy. 
 * 
 * A class is a reference; arrays are examples of references which are not classes.
 * 
 * @author vj
 *
 */
public interface X10ClassType extends ClassType, X10Struct, X10Use<X10ClassDef> {

	/** Property initializers, used in annotations. */
	List<Expr> propertyInitializers();
	Expr propertyInitializer(int i);
	X10ClassType propertyInitializers(List<Expr> inits);

	boolean isIdentityInstantiation();

	/**
	 * The list of properties of the class. 
	 * @return
	 */
	List<FieldInstance> properties();

	/**
	 * The sublist of properties defined at this class.
	 * All and exactly the properties in this list need to be 
	 * set in each constructor using a property(...) construct.
	 * @return
	 */
	List<FieldInstance> definedProperties();

	List<Type> typeArguments();
	X10ClassType typeArguments(List<Type> typeArgs);

	boolean hasParams();
	List<Type> typeMembers();

	MacroType typeMemberMatching(Matcher<Named> matcher);

	boolean isJavaType();

	X10ClassType container();
	X10ClassType container(StructType container);
}