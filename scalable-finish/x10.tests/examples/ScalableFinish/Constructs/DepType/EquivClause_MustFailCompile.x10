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
 * Checks that decplauses are checked when checking type equality.
 *
 * @author vj
 */
public class EquivClause_MustFailCompile   {
    var i: int{self==1} = 1;
    var j: int{self==0} = i;

	public def run(): boolean = {
	   
	    return true;
	}
	public static def main(var args: Rail[String]): void = {
		new EquivClause_MustFailCompile().run ();
	}
	
}
