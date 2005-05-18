define(`Now',
`async(here) clocked$1 finish async(here) $2')
// Automatically generated by the command
// m4 ClockTest4.m4 > ClockTest4.x10
// Do not edit
/**
 * Clock test for  barrier functions
 *
 * foreach loop body represented with a method
 *
 * @author kemal 3/2005
 */
public class ClockTest4 {

    int val=0;
    const int N=32;

    public boolean run() {
        final clock c = clock.factory.clock();
        
        foreach (point [i]: 1:(N-1)) clocked(c) {
            foreachBody(i,c);
        }
        foreachBody(0,c);
        int temp2;
        atomic {temp2=val;}
        chk(temp2==0);
        return true;
    }

    static void chk(boolean b) {if (!b) throw new Error();}

    void foreachBody(final int i, final clock c) {
            Now((c),{async(here) {atomic val+=i;}})
            next;
            int temp;
            atomic {temp=val;}
            chk(temp == N*(N-1)/2);
            next;
            Now((c),{async(here) {atomic val-=i;}})
            next;
    }
    
    public static void main(String[] args) {
        final boxedBoolean b=new boxedBoolean();
        try {
                finish b.val=(new ClockTest4()).run();
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
