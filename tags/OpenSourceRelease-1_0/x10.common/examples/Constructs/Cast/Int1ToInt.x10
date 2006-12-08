import harness.x10Test;

/**
 * Check that dep clauses are cheked when checking statically if a cast can be valid at runtime.
 */
public class Int1ToInt extends x10Test {
	public boolean run() {
		int(:self==0) zero = 0;
		int(:self==1) one = 1;
		int i = (int) one;
		return true;
	}

	public static void main(String[] args) {
		new Int1ToInt().execute();
	}


}

