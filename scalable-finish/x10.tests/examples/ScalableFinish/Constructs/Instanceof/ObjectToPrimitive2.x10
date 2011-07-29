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
 * Purpose: Checks a boxed primitive is an instanceof this same primitive type .
 * @author vcave
 **/
public class ObjectToPrimitive2   {
	 
	public def run(): boolean = {
		var array: Rail[X10DepTypeClassOneB]!
		= Rail.make[X10DepTypeClassOneB](1, (int):X10DepTypeClassOneB=>null);
		var var_: x10.lang.Any = array(0);
		return !(var_ instanceof X10DepTypeClassOneB);
	}
	
	public static def main(Rail[String])  {
		new ObjectToPrimitive2().run ();
	}
}