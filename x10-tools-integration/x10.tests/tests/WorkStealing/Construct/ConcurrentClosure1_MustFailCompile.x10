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

/*
 * Test closure with concurrent construct. Cannot pass WS Compile.
 */
public class ConcurrentClosure1_MustFailCompile {
	
	var value:int;
	
	public def this(){
	}
	
	public def run():boolean {
		val f = (i:int) => {
			var result:int = 0;
			finish {
				async result = i;
			}
			return result;
		};

		val result = f(1);
		value = result;
		
		Console.OUT.println("ConcurrentClosure1: value = " + value);
		return value == 1;
	}
	
	public static def main(Array[String](1)) {
        val r = new ConcurrentClosure1_MustFailCompile().run();
        if(r){
             x10.io.Console.OUT.println("++++++Test succeeded.");
        }
	}
}