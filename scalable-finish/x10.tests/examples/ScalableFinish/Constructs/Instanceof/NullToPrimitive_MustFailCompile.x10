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
 * Purpose: Checks null is not an instance of a primitive 
 * Issue: primitive has to be encapsulated in a nullable so that null can be an instanceof.
 * @author vcave
 **/
public class NullToPrimitive_MustFailCompile   {
	 
	public def run(): boolean = {
		return !(null instanceof Int);
	}
	
	public static def main(var args: Rail[String]): void = {
		new NullToPrimitive_MustFailCompile().run ();
	}
}
