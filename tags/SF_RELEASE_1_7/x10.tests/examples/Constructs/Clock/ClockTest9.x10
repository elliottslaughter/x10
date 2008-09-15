/*
 *
 * (C) Copyright IBM Corporation 2006
 *
 *  This file is part of X10 Test.
 *
 */
// Automatically generated by the command
// m4 ClockTest9.m4 > ClockTest9.x10
// Do not edit
import harness.x10Test;;

/**
 * Nested barriers test.
 *
 * N outer activities each
 * create M inner activities that do
 * barrier syncs. Then the outer activities do a barrier sync
 * and check the results.
 *
 * @author kemal 4/2005
 */
public class ClockTest9 extends x10Test {

	public const N: int = 8;
	public const M: int = 8;
	val val: Array[int] = Array.make[int](0..N-1->here, (point)=>0);

	public def run(): boolean = {
		finish async {
			val c: clock = clock.make();

			// outer barrier loop
			foreach (val (i): point in [0..N-1]) {
				foreachBody(i, c);
			}
		}
		return true;
	}

	def foreachBody(val i: int, val c: clock): void = {
		async(here) clocked(c) finish async(here) {
			val d: clock = clock.make();

			// inner barrier loop
			foreach (val (j): point in [0..M-1]) {
				foreachBodyInner(i, j, d);
			}
		}
		System.out.println("#0a i = "+i);
		next;
		// at this point each val[k] must be 0
		async(here) clocked(c) finish async(here) for (val (k): point in val) chk(val(k) == 0);
		System.out.println("#0b i = "+i);
		next;
	}

	def foreachBodyInner(val i: int, val j: int, val d: clock): void = {
		// activity i, j increments val[i] by j
		async(here) clocked(d) finish async(here) { atomic val(i) += j; }
		System.out.println("#1 i = "+i+" j = "+j);
		next;
		// val[i] must now be SUM(j = 0 to M-1)(j)
		async(here) clocked(d) finish async(here) { var tmp: int; atomic tmp = val(i); chk(tmp == M*(M-1)/2); }
		System.out.println("#2 i = "+i+" j = "+j);
		next;
		// decrement val[i] by the same amount
		async(here) clocked(d) finish async(here) { atomic val(i) -= j; }
		System.out.println("#3 i = "+i+" j = "+j);
		next;
		// val[i] should be 0 by now
	}

	public static def main(var args: Rail[String]): void = {
		new ClockTest9().executeAsync();
	}
}
