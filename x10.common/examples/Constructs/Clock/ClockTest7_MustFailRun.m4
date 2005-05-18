define(`Now',
`async(here) clocked$1 finish async(here) $2')
// Automatically generated by the command
// m4 ClockTest7_MustFailRun.m4 > ClockTest7_MustFailRun.x10
// Do not edit
/**
 * Combination of finish and clocks. 
 * New rule: finish cannot pass any clock to a subactivity.
 * should cause a run time or compile time error.
 *
 * @author kemal 3/2005
 */
public class ClockTest7_MustFailRun {
    
    int val=0;
    static final int N=32;
    
    public boolean run() {
        final clock c = clock.factory.clock();
        
        finish foreach (point [i]: 0:(N-1)) clocked(c) {
            Now((c),{atomic val++;})
            System.out.println("Activity "+i+" phase 0");
            next;
            chk(val == N);
            System.out.println("Activity "+i+" phase 1");
            next;
            Now((c),{atomic val++;})
            System.out.println("Activity "+i+" phase 2");
            next;
        }

        next; next; next;

        chk(val ==2*N);

        return true;
    }

    static void chk(boolean b) {if (!b) throw new Error();}
    
    public static void main(String[] args) {
        final boxedBoolean b=new boxedBoolean();
        try {
                finish b.val=(new ClockTest7_MustFailRun()).run();
        } catch (Throwable e) {
                e.printStackTrace();
                b.val=false;
        }
        System.out.println("++++++ "+(b.val?"Test succeeded.":"Test failed."));
        x10.lang.Runtime.setExitCode(b.val?0:1);
    }
    static class boxedBoolean {
        boolean val=false;
    }
    
}
