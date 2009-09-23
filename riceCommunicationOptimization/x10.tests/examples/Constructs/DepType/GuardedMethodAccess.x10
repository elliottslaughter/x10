/**
 *
 * (C) Copyright IBM Corporation 2006
 *
 *  This file is part of X10 Test.
 *
 */

/** Tests that a method of a class C, guarded with this(:c), is accessed only in objects
 * whose type is a subtype of C(:c).
 *@author pvarma
 *
 */

import harness.x10Test;

public class GuardedMethodAccess extends x10Test { 

   class Test(i:int, j:int) {
		public var v: int = 0;
		def this(i:int, j:int):Test{self.i==i,self.j==j} = {
			property(i,j);
		}
		public def  key(){i==j}=5;
	}
	
		
	public def run(): boolean = {
		var t: Test!{i==j} = new Test(5, 5);
		t.v = t.key() + 1;
	   return true;
	}  
	
    public static def main(var args: Rail[String]): void = {
        new GuardedMethodAccess().execute();
    }
   

		
}
