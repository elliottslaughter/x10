/*
 *
 * (C) Copyright IBM Corporation 2006
 *
 *  This file is part of X10 Test.
 *
 */
import harness.x10Test;;

/**
 * Check that a float literal can be cast to float.
 */
public class DoubleLitDepType extends x10Test {
	public def run(): boolean = {
		var f: double(1.2D) = 1.2D;
		return true;
	}

	public static def main(var args: Rail[String]): void = {
		new DoubleLitDepType().execute();
	}


}
