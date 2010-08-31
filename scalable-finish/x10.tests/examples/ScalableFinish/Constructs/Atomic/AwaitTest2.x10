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
 * Minimal test for await.
 */
public class AwaitTest2   {

	var val_: int = 42;

	public def run(): boolean = {
		await(val_ == 42);
		return true;
	}

	public static def main(var args: Rail[String]): void = {
		new AwaitTest2().run ();
	}
}
