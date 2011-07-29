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

package x10.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import polyglot.ast.Expr;
import polyglot.ast.MethodDecl;
import polyglot.ast.TypeNode;
import polyglot.types.ClassDef;
import polyglot.types.ClassType;
import polyglot.types.Flags;
import polyglot.types.LazyRef;
import polyglot.types.LocalDef;
import polyglot.types.Name;
import polyglot.types.QName;
import polyglot.types.Ref;
import polyglot.types.SemanticException;
import polyglot.types.Type;
import polyglot.types.Types;
import polyglot.util.Position;
import x10.ast.AnnotationNode;
import x10.ast.X10StringLit_c;
import x10.constraint.XName;
import x10.constraint.XNameWrapper;
import x10.constraint.XVar;
import x10.constraint.XTerms;
import x10.extension.X10Ext;
import x10.types.X10ClassDef;
import x10.types.X10ClassDef_c;
import x10.types.X10ClassType;
import x10.types.X10ConstructorDef;
import x10.types.X10Flags;
import x10.types.X10MethodDef;
import x10.types.X10ParsedClassType;
import x10.types.X10ParsedClassType_c;
import x10.types.X10TypeSystem;
import x10.types.X10TypeSystem_c;

public class Struct {
    public static X10ClassDef makeDef(final X10TypeSystem_c xts) {

       final Position pos = Position.COMPILER_GENERATED;

       String name = "Struct";
       X10ClassDef cd = (X10ClassDef) new X10ClassDef_c(xts, null) {
           @Override
           public ClassType asType() {
               if (asType == null) {
                   X10ClassDef cd = this;
                   asType = new X10ParsedClassType_c(this);
               }
               return asType;
           }
       };

       cd.position(pos);
       cd.name(Name.make(name));
       try {
           cd.setPackage(Types.ref(xts.packageForName(QName.make("x10.lang"))));
       }
       catch (SemanticException e) {
           assert false;
       }

       QName fullName = QName.make("x10.lang", name);
       cd.kind(ClassDef.TOP_LEVEL);
       cd.superType(null); // base class has no superclass
       
       cd.setInterfaces(Collections.<Ref<? extends Type>> singletonList(xts.lazyAny()));
       cd.flags(X10Flags.toX10Flags(Flags.PUBLIC.Abstract()).Struct());

       // NOTE: don't call cd.asType() until after the type parameters are
       // added.
       X10ParsedClassType ct = (X10ParsedClassType) cd.asType();
       xts.systemResolver().install(fullName, ct);

       String fullNameWithThis = fullName + "#this";
       //String fullNameWithThis = "this";
       XName thisName = new XNameWrapper<Object>(new Object(), fullNameWithThis);
       XVar thisVar = XTerms.makeLocal(thisName);

       final LazyRef<X10ParsedClassType> PLACE = Types.lazyRef(null);
       PLACE.setResolver(new Runnable() {
           public void run() {
               PLACE.update((X10ParsedClassType) xts.Place());
           }
       });
       final LazyRef<X10ParsedClassType> STRING = Types.lazyRef(null);
       STRING.setResolver(new Runnable() {
           public void run() {
               STRING.update((X10ParsedClassType) xts.String());
           }
       });
       final LazyRef<X10ParsedClassType> BOOLEAN = Types.lazyRef(null);
       BOOLEAN.setResolver(new Runnable() {
           public void run() {
               BOOLEAN.update((X10ParsedClassType) xts.Boolean());
           }
       });
       final LazyRef<X10ParsedClassType> OBJECT = Types.lazyRef(null);
       OBJECT.setResolver(new Runnable() {
           public void run() {
               OBJECT.update((X10ParsedClassType) xts.Object());
           }
       });
       X10ConstructorDef ci = (X10ConstructorDef) xts.constructorDef(pos, Types.ref(ct), Flags.PUBLIC.Native(), 
               Collections.EMPTY_LIST, 
               Collections.EMPTY_LIST);
       cd.addConstructor(ci);

       X10MethodDef mi;
       List<Expr> list;
       X10ClassType ann;
	
       // @Native("java", "x10.lang.Place.place(x10.core.Ref.home(#0))")
       // property def home():Place
       mi = xts.methodDef(pos, Types.ref(ct), 
               X10Flags.toX10Flags(Flags.PUBLIC.Native()).Property().Global().Safe(), 
               PLACE,
               xts.homeName(), 
               Collections.EMPTY_LIST, 
               Collections.EMPTY_LIST, 
               thisVar,
               Collections.EMPTY_LIST, 
               null, 
               null, 
               Collections.EMPTY_LIST, 
               null, //offerTye
               null);
       final LazyRef<X10ParsedClassType> NATIVE_LOC = Types.lazyRef(null);
       NATIVE_LOC.setResolver(new Runnable() {
           public void run() {
               List<Expr> list = new ArrayList<Expr>(2);
               list.add(new X10StringLit_c(pos, "java"));
               list.add(new X10StringLit_c(pos,  "x10.lang.Place.place(x10.core.Ref.home(#0))"));
               X10ParsedClassType ann=  (X10ParsedClassType) ((X10ParsedClassType) xts.NativeType()).propertyInitializers(list);
               NATIVE_LOC.update(ann);
           }
       });
       mi.setDefAnnotations(Collections.<Ref<? extends Type>> singletonList(NATIVE_LOC));
       cd.addMethod(mi);
        
       // @Native("java", "x10.core.Ref.at(#0, #1)")
       // property def at(p:Object):boolean;
       List<LocalDef> parameters = xts.dummyLocalDefs(Collections.<Ref<? extends Type>> singletonList(OBJECT));
       mi = xts.methodDef(pos, Types.ref(ct), 
               X10Flags.toX10Flags(Flags.PUBLIC.Native()).Property().Safe(), 
               BOOLEAN,
               Name.make("at"), 
               Collections.EMPTY_LIST, 
               Collections.<Ref<? extends Type>> singletonList(OBJECT),
               thisVar,
               parameters,
               null, 
               null, 
               Collections.EMPTY_LIST, 
               null, //offerType
               null);
       final LazyRef<X10ParsedClassType> NATIVE_AT_1 = Types.lazyRef(null);
       NATIVE_AT_1.setResolver(new Runnable() {
           public void run() {
               List<Expr> list = new ArrayList<Expr>(2);
               list.add(new X10StringLit_c(pos, "java"));
               list.add(new X10StringLit_c(pos, "x10.core.Ref.at(#0, #1)"));
               X10ParsedClassType ann=  (X10ParsedClassType) ((X10ParsedClassType) xts.NativeType()).propertyInitializers(list);
               NATIVE_AT_1.update(ann);
           }
       });
       mi.setDefAnnotations(Collections.<Ref<? extends Type>> singletonList(NATIVE_AT_1));
       cd.addMethod(mi);

       // @Native("java", "x10.core.Ref.typeName(#0)")
       // @Native("c++", "x10aux::type_name(#0)")
       // native final global safe def typeName():String;        
       mi = xts.methodDef(pos,
               Types.ref(ct),
               X10Flags.toX10Flags(Flags.PUBLIC.Native().Final()).Global().Safe(), 
               STRING,
               Name.make("typeName"),
               Collections.EMPTY_LIST, 
               Collections.EMPTY_LIST,
               thisVar,
               Collections.EMPTY_LIST, 
               null,
               null,
               Collections.EMPTY_LIST,
               null, // offerType
               null
       );
       final LazyRef<X10ParsedClassType> NATIVE_TYPE_NAME = Types.lazyRef(null);
       NATIVE_TYPE_NAME.setResolver(new Runnable() {
           public void run() {
               List<Expr> list = new ArrayList<Expr>(2);
               list.add(new X10StringLit_c(pos, "java"));
               list.add(new X10StringLit_c(pos,  "x10.core.Ref.typeName(#0)"));
               X10ParsedClassType ann=  (X10ParsedClassType) ((X10ParsedClassType) xts.NativeType()).propertyInitializers(list);
               NATIVE_TYPE_NAME.update(ann);
           }
       });
       final LazyRef<X10ParsedClassType> NATIVE_CPP_TYPE_NAME = Types.lazyRef(null);
       NATIVE_CPP_TYPE_NAME.setResolver(new Runnable() {
           public void run() {
               List<Expr> list = new ArrayList<Expr>(2);
               list.add(new X10StringLit_c(pos, "c++"));
               list.add(new X10StringLit_c(pos,  "x10aux::type_name(#0)"));
               X10ParsedClassType ann=  (X10ParsedClassType) ((X10ParsedClassType) xts.NativeType()).propertyInitializers(list);
               NATIVE_CPP_TYPE_NAME.update(ann);
           }
       });

       List<Ref<? extends Type>> tn_ann = new ArrayList<Ref<? extends Type>>();
       tn_ann.add(NATIVE_TYPE_NAME);
       tn_ann.add(NATIVE_CPP_TYPE_NAME);
       mi.setDefAnnotations(tn_ann);
       //mi.setDefAnnotations(Collections.<Ref<? extends Type>> singletonList(NATIVE_TYPE_NAME));
       cd.addMethod(mi);

       //  @Native("java", "\"<struct>\"")
       //  native global safe def toString():String;        
       mi = xts.methodDef(pos,
               Types.ref(ct),
               X10Flags.toX10Flags(Flags.PUBLIC.Native()).Global().Safe(), 
               STRING,
               Name.make("toString"),
               Collections.EMPTY_LIST, 
               Collections.EMPTY_LIST,
               thisVar,
               Collections.EMPTY_LIST, 
               null,
               null,
               Collections.EMPTY_LIST,
               null, // offerType
               null
       );
       final LazyRef<X10ParsedClassType> NATIVE_TYPE_NAME2 = Types.lazyRef(null);
       NATIVE_TYPE_NAME2.setResolver(new Runnable() {
           public void run() {
               List<Expr> list = new ArrayList<Expr>(2);
               list.add(new X10StringLit_c(pos, "java"));
               list.add(new X10StringLit_c(pos,  "\"<struct>\""));
               X10ParsedClassType ann=  (X10ParsedClassType) ((X10ParsedClassType) xts.NativeType()).propertyInitializers(list);
               NATIVE_TYPE_NAME2.update(ann);
           }
       });

       mi.setDefAnnotations(Collections.<Ref<? extends Type>> singletonList(NATIVE_TYPE_NAME2));
       cd.addMethod(mi);

       // @Native("java", "x10.core.Ref.at(#0, #1.id)")
       // property def at(p:Place):boolean;
       parameters = xts.dummyLocalDefs(Collections.<Ref<? extends Type>> singletonList(PLACE));
       mi = xts.methodDef(pos, Types.ref(ct), 
               X10Flags.toX10Flags(Flags.PUBLIC.Native()).Property().Safe(), 
               BOOLEAN,
               Name.make("at"), 
               Collections.EMPTY_LIST, 
               Collections.<Ref<? extends Type>> singletonList(PLACE),
               thisVar,
               parameters,
               null, 
               null, 
               Collections.EMPTY_LIST, 
               null, // offerType
               null);
       final LazyRef<X10ParsedClassType> NATIVE_AT_2 = Types.lazyRef(null);
       NATIVE_AT_2.setResolver(new Runnable() {
           public void run() {
               List<Expr> list = new ArrayList<Expr>(2);
               list.add(new X10StringLit_c(pos, "java"));
               list.add(new X10StringLit_c(pos, "x10.core.Ref.at(#0, #1.id)"));
               X10ParsedClassType ann=  (X10ParsedClassType) ((X10ParsedClassType) xts.NativeType()).propertyInitializers(list);
               NATIVE_AT_2.update(ann);
           }
       });
       mi.setDefAnnotations(Collections.<Ref<? extends Type>> singletonList(NATIVE_AT_2));
       cd.addMethod(mi);

       //@NativeRep("java", "x10.core.Struct", null, null)
       final LazyRef<X10ParsedClassType> NATIVE_REP = Types.lazyRef(null);
       NATIVE_REP.setResolver(new Runnable() {
           public void run() {
               List<Expr> list = new ArrayList<Expr>(4);
               list.add(new X10StringLit_c(pos, "java"));
               list.add(new X10StringLit_c(pos, "x10.core.Struct"));
               list.add(null);
               list.add(null);
               X10ParsedClassType ann = (X10ParsedClassType) ((X10ParsedClassType) xts.NativeRep()).propertyInitializers(list);

               NATIVE_REP.update(ann);
           }
       });


       //@NativeRep("c++", "x10::lang::Struct", "x10::lang::Struct", null)
       final LazyRef<X10ParsedClassType> NATIVE_REP_CPP = Types.lazyRef(null);
       NATIVE_REP_CPP.setResolver(new Runnable() {
           public void run() {
               List<Expr> list = new ArrayList<Expr>(4);
               list.add(new X10StringLit_c(pos, "c++"));
               list.add(new X10StringLit_c(pos, "x10::lang::Struct"));
               list.add(new X10StringLit_c(pos, "x10::lang::Struct"));
               list.add(null);
               X10ParsedClassType ann = (X10ParsedClassType) ((X10ParsedClassType) xts.NativeRep()).propertyInitializers(list);

               NATIVE_REP_CPP.update(ann);
           }
       });

       List<Ref<? extends Type>> cd_ann = new ArrayList<Ref<? extends Type>>();
       cd_ann.add(NATIVE_REP);
       cd_ann.add(NATIVE_REP_CPP);
       cd.setDefAnnotations(cd_ann);
       //cd.setDefAnnotations(Collections.<Ref<? extends Type>> singletonList(NATIVE_REP));
       return cd;
    }

}