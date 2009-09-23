// This file is part of X10 Test. *

import harness.x10Test;

/**
 * @author milthorpe 6/2009
 */
class XTENLANG_443 extends x10Test {

    public def run(): boolean {
        /*
         * Validate x10.lang.Math functions by use of trigonometric 
         * and hyperbolic identities.
         */
        val theta : double = Math.PI / 4.0;
        val cos : double = Math.cos(theta);
        val sin : double = Math.sin(theta);
        val tan : double = Math.tan(theta);
        chk(tan == sin / cos);

        var theta2 : double = Math.atan(tan);
        chk(theta2 == theta);

        val x : double = 0.6;
        chk(Math.asin(x) + Math.acos(x) == Math.PI / 2.0);
        chk(Math.atan(x) + Math.atan(1.0/x) == Math.PI / 2.0);

        val sinh : double = Math.sinh(theta);
        val cosh : double = Math.cosh(theta);
        val tanh : double = Math.tanh(theta);
        chk(tanh == sinh / cosh);   

        /* cartesian (1.0, 1.0) = polar (PI/4, sqrt(2)) */ 
        theta2 = Math.atan2(1.0, 1.0);
        chk(theta2 == theta);
        val hypot : double = Math.hypot(1.0, 1.0);
        chk(hypot == Math.sqrt(2.0));     

        /* Validate cube root against a cube */
        val piCubed : double = Math.PI * Math.PI * Math.PI;
        chk (Math.cbrt(piCubed) == Math.PI);

        /* Validate expm1 against log1p */
        val d : double = 0.02;
        val e : double = Math.expm1(d);
        chk(Math.log1p(e) == d);

        return true;
    }

    public static def main(Rail[String]) {
        new XTENLANG_443().execute();
    }
}

