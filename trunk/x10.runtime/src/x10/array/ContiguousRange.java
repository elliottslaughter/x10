/*
 * Created on Nov 1, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package x10.array;

/**
 * @author praun
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ContiguousRange implements Range {

	/**
	 * Cardinality of the range, i.e., the number of element 
	 * in the integer space it covers.
	 */	
	public final int card;
	public final int lo;
	public final int hi;
	
	/** 
	 * Range that starts at lo (including)
	 * to hi (including!).
	 */
	public ContiguousRange(int lo, int hi) {
		assert hi >= lo;
		
		this.lo = lo;
		this.hi = hi;
		card = hi - lo + 1; // inclusive!
	}
    
	public int size() {
	    return card;
	}
	
	public int coord(int ord) {
	    assert ord < card;
	    return lo + ord;
	}
	
	public int ordinal(int p) {
		assert contains(p);	
		return p - lo;
	}
	
	public Range union(Range r) {
	    assert r != null;
	    assert r instanceof ContiguousRange;
	    
	    ContiguousRange cr = (ContiguousRange) r;
	    int l = (lo < cr.lo) ? lo : cr.lo;
	    int h = (hi > cr.hi) ? hi : cr.hi;
	    return new ContiguousRange(l, h);
	}
	
	public Range intersect(Range r) {
	    assert r != null;
	    assert r instanceof ContiguousRange;
	    
	    ContiguousRange cr = (ContiguousRange) r;
	    int l = (lo > cr.lo) ? lo : cr.lo;
	    int h = (hi < cr.hi) ? hi : cr.hi;
	    return new ContiguousRange(l, h);
	}
	
	public Range difference(Range r) {
	    assert r != null;
	    assert r instanceof ContiguousRange;
	    
	    // Ranges with holes are not allowed (yet), hence 
	    // both ranges must have either a common lo or hi level or both.
	    ContiguousRange cr = (ContiguousRange) r;
	    assert lo == cr.lo || hi == cr.hi; 
	    return intersect(r);
	}
	
	public boolean contains(int p) {
		return lo <= p && p <= hi; // X10 spec says range is inclusive!
	}
	
	public boolean contains(Range r) {
	    assert r != null;
	    assert r instanceof ContiguousRange;
	    
	    ContiguousRange cr = (ContiguousRange) r;
		return cr.lo >= lo && cr.hi <= hi;
	}
	
	public String toString() {
		return "[" + lo + ".." + hi + "]";
	}
	
	public boolean equals(Object o) {
	    assert o != null;
		assert o instanceof Range;
		if (!(o instanceof ContiguousRange))
		    return false;
		else {
		    ContiguousRange rhs = (ContiguousRange) o;
		    return rhs.lo == lo && rhs.hi == hi;
		}
	}
	
	public int hashCode() {
	    return card;
	}
}
