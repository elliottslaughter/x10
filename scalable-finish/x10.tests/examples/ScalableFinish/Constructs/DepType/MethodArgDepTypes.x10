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
 * Check that a method arg can have a deptype and it is propagated into the body.
 *
 * @author vj
 */
public class MethodArgDepTypes   {
    class Test(i:int, j:int) {
        public def this(ii:int, jj:int):Test{self.i==ii,self.j==jj} = { property(ii,jj); }
    }

    public def m(var t: Test{self.i==self.j}): boolean = {
        var tt: Test{self.i==self.j} = t;
        return true;
    }
    public def run(): boolean = {
        return m(new Test(2,2));
    }
    public static def main(var args: Rail[String]): void = {
        new MethodArgDepTypes().run ();
    }
}
