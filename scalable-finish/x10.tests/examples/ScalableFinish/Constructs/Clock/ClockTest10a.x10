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
 * Using clocks to do simple producer consumer synchronization
 * for this task DAG (arrows point downward)
 * in pipelined fashion. On each clock period,
 * each stage of the pipeline reads the previous clock period's
 * result from the previous stage and produces its new result
 * for the current clock period.
 *
 * <code>
 *   A   stage 0 A produces the stream 1,2,3,...
 *  / \
 *  B  C  stage 1 B is "double", C is "square" function
 *  \ /|
 *   D E  stage 2 D is \(x,y)(x+y+10), E is \(x)(x*7)
 * </code>
 *
 * @author kemal 4/2005
 */
public class ClockTest10a   {
	// varX[0] and varX[1] serve alternately as
	// the "new result" and "old result"
	val varA: Rail[int]! = Rail.make[int](2, (x:int)=>0);
	val varB: Rail[int]! = Rail.make[int](2, (x:int)=>0);
	val varC: Rail[int]! = Rail.make[int](2, (x:int)=>0);
	val varD: Rail[int]! = Rail.make[int](2, (x:int)=>0);
	val varE: Rail[int]! = Rail.make[int](2, (x:int)=>0);
	public const N = 10;
	public const pipeDepth = 2;

	static def ph(var x: int): int = { return x % 2; }

	public def run(): boolean = {
		finish async(here) {
			val a = Clock.make();
			val b = Clock.make();
			val c = Clock.make();
			async clocked(a) taskA(a);
			async clocked(a, b) taskB(a, b);
			async clocked(a, c) taskC(a, c);
			async clocked(b, c) taskD(b, c);
			async clocked(c) taskE(c);
		}
		return true;
	}

	def taskA(val a: Clock): void = {
		for ((k):Point(1) in 1..N) {
			varA(ph(k)) = k;
			x10.io.Console.OUT.println(k + " A producing " + varA(ph(k)));
			next;
		}
	}
	def taskB(val a: Clock, val b: Clock): void = {
		for ((k):Point(1) in 1..N) {
			varB(ph(k)) = varA(ph(k-1))+varA(ph(k-1));
			x10.io.Console.OUT.println(k + " B consuming oldA producing " + varB(ph(k)));
			next;
		}
	}
	def taskC(val a: Clock, val c: Clock): void = {
		for ((k):Point(1) in 1..N) {
			varC(ph(k)) = varA(ph(k-1))*varA(ph(k-1));
			x10.io.Console.OUT.println(k+" C consuming oldA producing "+ varC(ph(k)));
			next;
		}
	}
	def taskD(val b: Clock, val c: Clock): void = {
		for ((k):Point(1) in 1..N) {
			varD(ph(k)) = varB(ph(k-1))+varC(ph(k-1))+10;
			x10.io.Console.OUT.println(k+" D consuming oldC producing "+varD(ph(k)));
			var n: int = k-pipeDepth;
			chk(!(k>pipeDepth) || varD(ph(k)) == n+n+n*n+10);
			next;
		}
	}
	def taskE(val c: Clock): void = {
		for ((k):Point(1) in 1..N) {
			varE(ph(k)) = varC(ph(k-1))*7;
			x10.io.Console.OUT.println(k+" E consuming oldC producing "+varE(ph(k)));
			var n: int = k-pipeDepth;
			chk(!(k>pipeDepth) || varE(ph(k)) == n*n*7);
			next;
		}
	}

	public static def main(var args: Rail[String]): void = {
		new ClockTest10a().run ();
	}

	static class boxedInt {
		var val: int;
	}
}
