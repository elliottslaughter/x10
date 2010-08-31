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
 * Check a local variable can have a has type.
 *
 * @author vj
 */
public class HasTypeClosure_MustFailCompile   {

	def m(x:Int{self==1}) = x;
	public def run(): boolean = {
		val x = (y:Int)<: Int => y;
		val z <:Int(0) = x(1);
		return true;
	}

	public static def main(Rail[String])  {
		new HasTypeClosure_MustFailCompile().run ();
	}
}
