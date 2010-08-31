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

 

/**
 * 
 * Test that constraints are correctly propagated through when a field's receiver is an expression e.
 * The expression may be of type Foo!. If the field Foo.f is declared of type Fum!, then it must be the
 * case that e.f's home is statically known to be here.

 * @author vj
 */
public class FieldReceiverIsExpr   {
    
	class F {
		val f:F!;
	        def m(){}
	        def this(f:F!) { this.f=f;}
	}
	    def m() { 
	        (new F(null) as F!).f.m();
	    }

    public def run() = true;

    public static def main(Rail[String]) {
	  new FieldReceiverIsExpr().run ();
    }

}
