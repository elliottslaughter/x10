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
 * Check that properly typed arguments are accepted by &&
 *
 * @author vj
 */
public class CondAnd   {
   
   
	public def run(): boolean = {
	   val r1 = 0..100;
	   val r2 = 2..99;
	   val r3 = r1 && r2;
	   return true;
	}
	public static def main(var args: Rail[String]): void = {
		new CondAnd().run ();
	}
}
