/*
 *
 * (C) Copyright IBM Corporation 2006
 *
 *  This file is part of X10 Test.
 *
 */
import harness.x10Test;;

/**
 * Test the shorthand syntax for array of arrays initializer.
 *
 * @author igor, 12/2005
 */
public class ArrayArrayInitializerShorthand extends x10Test {

	public def run(): boolean = {
		val d: dist = Dist.makeConstant([1..10, 1..10], here);
		val a: Array[int] = Array.make[int](d, (point)=>0);
		val ia: Array[Array[int]] 
		  = Array.make[Array[int]](d, ((i,j): point) =>a);
		for (val (i,j):point(2) in ia) chk(ia(i, j) == a);
		return true;
	}

	public static def main(var args: Rail[String]): void = {
		new ArrayArrayInitializerShorthand().execute();
	}
}
