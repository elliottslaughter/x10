package x10.lang;
import x10.array.*;
public class loop6 {

  public def work():void{}
  public def foo():void {
        val R: Region = 1..100;
        val d:Dist = R -> here;
        finish ateach(p in d){
                finish{
                        while(true){
                                async{}
                        }
                }
        }
  }        
}


