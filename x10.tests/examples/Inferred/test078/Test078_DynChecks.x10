//OPTIONS: -STATIC_CHECKS=false -CONSTRAINT_INFERENCE=false -VERBOSE_INFERENCE=true

package test078;

import harness.x10Test;
import x10.compiler.InferGuard;

public class Test078_DynChecks extends x10Test {

    public def run(): boolean {
	val v1 = new Vec(42);
	val v2 = new Vec(42);
	val v3 = new Vec(42);
	Vec.add3(v1, v2, v3);
        return true;
    }

    public static def main(Array[String](1)) {
    	new Test078_DynChecks().execute();
    }

}
