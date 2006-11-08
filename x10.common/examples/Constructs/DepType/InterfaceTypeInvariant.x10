/** Tests that the properties of an interface are implemented by a compliant class 
 * and that the interface constraint is entailed by the compliant class.
 *@author pvarma
 *
 */

import harness.x10Test;

public class InterfaceTypeInvariant(int i, int j) extends x10Test { 

    public static interface Test (int l, int m : m == l ) {
     public int put();
    }
    
    class Tester(int l, int m : m == l ) implements Test{
      public Tester(int arg) { l = arg; m = arg;}
      public int put() {
        return 0;
      }
	}
 
    public  boolean run() { 
	 return true;
    }
	
    public static void main(String[] args) {
        new InterfaceTypeInvariant().execute();
    }
   

		
}
