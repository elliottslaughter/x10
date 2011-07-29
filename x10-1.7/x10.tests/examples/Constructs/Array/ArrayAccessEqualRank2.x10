/*
 *
 * (C) Copyright IBM Corporation 2006
 *
 *  This file is part of X10 Test.
 *
 */
import harness.x10Test;

/**
 * Test that a can be accessed through point p if p ranges over b.dist
 and a.rank=b.rank. Here a and b are defined over two distributions,
 each of whose regions has rank 1.

 * @author vj 03/17/09 -- fails compilation.

 */

public class ArrayAccessEqualRank2 extends x10Test {

    public def arrayEqual(A: Array[int], B: Array[int](A.rank)) {
        finish
            ateach (p in A.dist) {
            val v = at (B.dist(p)) B(p);
            chk(A(p) == v);
	}
    }

    public def run(): boolean = {

	val R = Region.make(0,9), S = Region.make(0,9); 
	// R and S should both be rank 1.
	val D = Dist.make(R), E=Dist.make(S);
        val a = Array.make[Int](D, (Point)=>0), 
	b = Array.make[Int](E,(Point)=>0);
	arrayEqual(a,b);
        return true;
    }

    public static def main(Rail[String]) = {
        new ArrayAccessEqualRank().execute();
    }
}