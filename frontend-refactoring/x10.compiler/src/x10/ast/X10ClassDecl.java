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

import java.util.List;

import polyglot.visit.TypeChecker;
import x10.types.ClassDef;
import x10.types.LocalDef;
import x10.types.Name;
import x10.types.Ref;
import x10.types.Type;
import x10.types.TypeDef;
import x10.types.X10ClassDef;
import x10.types.X10TypeSystem;

public interface X10ClassDecl extends ClassDecl {
	DepParameterExpr classInvariant();
	X10ClassDecl classInvariant(DepParameterExpr classInvariant);
	
	List<TypeParamNode> typeParameters();
	X10ClassDecl typeParameters(List<TypeParamNode> typeParameters);
	
	List<PropertyDecl> properties();
	X10ClassDecl properties(List<PropertyDecl> ps);
	
	X10ClassDecl classDef(ClassDef type);
	X10ClassDecl flags(FlagsNode flags);
	X10ClassDecl name(Id name);
	X10ClassDecl superClass(TypeNode superClass);
	X10ClassDecl interfaces(List<TypeNode> interfaces);
	X10ClassDecl body(ClassBody body);
	X10ClassDef classDef();
}
