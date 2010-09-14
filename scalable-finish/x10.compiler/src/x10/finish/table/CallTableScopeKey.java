package x10.finish.table;
/**
 * 
 * class represents a finish block in x10
 */
public class CallTableScopeKey extends CallTableKey {
	
	private static final long serialVersionUID = 1L;
	/**
	 * true if this object represents a "finish" contruct in the program
	 * otherwise it represents an "at"
	 */
	public boolean isFinish;
	//only for "at"
	public boolean isHere=false;
	/**
	 * to calculate the arity of a method call from this block
	 */
	public int blk;
	
	public CallTableScopeKey(String s,String n, int line, int column, 
		int b, boolean f, boolean here) {
		// finish = f;
		super(s,n,line,column);
		blk = b;
		isFinish = f;
		//finish's isHere is always true
		isHere = (isFinish) || here;
	}
	
	/**
	 * although "tmp" is not necessary to included as part of a signature
	 */
	public String genSignature(){
	    String tmp;
	    if(isFinish == true){
	    	    tmp = ".finish.";
	    }
	    else{
	    	    tmp = ".at.";
	    }
	    return (scope + tmp + line + "." + column);
	}
	
	public String toString() {
	    return genSignature()+"."+pattern+"."+isHere;
	}

	public boolean equals(Object o) {
	    if (o instanceof CallTableScopeKey) {
		return this.genSignature().equals(((CallTableScopeKey) o).genSignature());
	    }
	    return false;
	}

	public int hashCode() {
	    return this.genSignature().hashCode();
	}
}
