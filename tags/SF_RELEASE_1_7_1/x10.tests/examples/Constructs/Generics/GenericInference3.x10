// (C) Copyright IBM Corporation 2008
// This file is part of X10 Test. *

import harness.x10Test;


/**
 * A call to a polymorphic method, closure, or constructor may omit
 * the explicit type arguments. If the method has a type parameter T,
 * the type argument corresponding to T is inferred to be the least
 * common ancestor of the types of any formal parameters of type T.
 *
 * @author bdlucas 8/2008
 */

public class GenericInference3 extends GenericTest {

    class V           {const name = "V"};
    class W extends V {const name = "W"};
    class X extends V {const name = "X"};
    class Y extends X {const name = "Y"};
    class Z extends X {const name = "Z"};

    def m[T](t1:T,t2:T){T<:V} = V.name;

    public def run(): boolean = {

        val v = new V();
        val w = new W();
        val x = new X();
        val y = new Y();
        val z = new Z();

        val vz = m(v,z);
        val wz = m(w,z);
        val xy = m(x,y);
        val yz = m(y,z);
        val yy = m(y,y);

        check("vz", vz, "V");
        check("wz", wz, "V");
        check("xy", xy, "V");
        check("yz", yz, "V");
        check("yy", yy, "V");

        return result;
    }

    public static def main(var args: Rail[String]): void = {
        new GenericInference3().execute();
    }
}
