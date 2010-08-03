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

import x10.util.Box;
import harness.x10Test;

/**
 * @author bdlucas 12/2008
 */

class XTENLANG_244 extends x10Test {

    static def check(a:Object, b:Object) = a==b;
    
    public def run():boolean {
        return check(1 as Box[Int],1 as Box[Int])==false;
    }

    public static def main(Rail[String]) {
        new XTENLANG_244().execute();
    }
}
