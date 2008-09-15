/*
 *
 * (C) Copyright IBM Corporation 2006
 *
 *  This file is part of X10 Test.
 *
 */
import harness.x10Test;;


/**
 * Check that a single statement in an if, subject to flattening, is handled
 correctly.
 */
public class FlattenConditional extends x10Test {
    var a: Array[int](2);
    public def this(): FlattenConditional = {
      a = Array.make[int](([1..10, 1..10] to Region)->here, 
         ((i,j): Point) => { return i+j;});
    }
    
    def m(var a: int): int = {
     if (a == 2) throw new Error();
     return a;
    }
    // m(a[1,1]) should not be executed. If the conditional is flattened
    // so that the body is moved out before the if, then it will be executed
    // and the test will fail.
	public def run(): boolean = {
	var b: int = 0;
	if (a(2, 2) == 0)
		b = m(a(1, 1));
	
	 return b==0;
	}

	public static def main(var args: Rail[String]): void = {
		new FlattenConditional().execute();
	}
	
}
