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
 * @author bdlucas 10/2008
 */
class XTENLANG_50 extends x10Test {

    static class R(rank:int) {
    
        public static def make(val rs: Array[R]): R{self.rank==rs.size} {
            return new R(rs.size);
        }
    
        public static operator (rs:Array[R]) = make(rs);
    
        def this(rank:int):R{self.rank==rank} = property(rank);
    }
    
    val x: R{rank==2} = [new R(1), new R(1)];

    public def run(): boolean {
        return true;
    }

    public static def main(Array[String](1)) {
        new XTENLANG_50().execute();
    }
}