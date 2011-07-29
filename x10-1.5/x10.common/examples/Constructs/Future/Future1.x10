/*
 *
 * (C) Copyright IBM Corporation 2006
 *
 *  This file is part of X10 Test.
 *
 */
import harness.x10Test;

/**
 * Future test.
 */
public class Future1 extends x10Test {
	public boolean run() {
		future<int> x = future { 41 };
		return x.force()+1 == 42;
	}

	public static void main(String[] args) {
		new Future1().execute();
	}
}
