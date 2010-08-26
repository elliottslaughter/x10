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

import harness.x10Test;

/**
 * Testing the ability to assign to the field of an object
 * at place here a reference to an object at place here.next().
 *
 * @author vj
 */
public class AsyncNext extends x10Test {

	public def run(): boolean = {
		val Other: Place = here.next();
		val t = new T();
		finish async (Other) {
			val t1: T = new T();
			async at (t) t.val_ = t1;
		}
		return t.val_.home == Other;
	}

	public static def main(Rail[String]) {
		new AsyncNext().execute();
	}

	static class T {
		var val_:Object;
	}
}
