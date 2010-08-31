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
 * Check that a nonblocking method can be overridden only by a nonblocking method.
 * @author vj  9/2006
 */
public class NonBlockingOverride_MustFailCompile   {

    class T1 {
      public nonblocking def m(): void = { }
    }
    class T2 extends T1 {
      public def m(): void = { /* should give a compile error. */ }
    }
   
	public def run(): boolean = {
		
		return true;
	}

	public static def main(var args: Rail[String]): void = {
		new NonBlockingOverride_MustFailCompile().run ();
	}

	
}
