// (C) Copyright IBM Corporation 2006-2008.
// This file is part of X10 Language.

package x10.array;

import x10.io.Printer;
import x10.io.StringWriter;


/**
 * This class represents a single polyhedral halfspace of the form
 *
 *     a0*x0 + a1*x1 + ... + constant <= 0
 *
 * The as are stored in the first rank elements of ValRow.this; the
 * constant is stored in this(rank) (using homogeneous coordinates).
 *
 * Equivalently, this class may be considered to represent a linear
 * inequality constraint, or a row in a constraint matrix.
 *
 * @author bdlucas
 */

public class PolyRow(rank:nat) extends ValRow {

    static type PolyRegion(rank:nat) = PolyRegion{self.rank==rank};
    static type PolyRegionListBuilder(rank:nat) = PolyRegionListBuilder{self.rank==rank};
    static type PolyRow(rank:nat) = PolyRow{self.rank==rank};
    static type PolyMat(rank:nat) = PolyMat{self.rank==rank};

    //
    //
    //


    def this(as: ValRail[int])= this(as, as.length-1);

    private def this(as: ValRail[int], n:int): PolyRow(n) {
        super(as);
        property(n);
    }

    def this(p:Point, k:int) {
        super(p.rank+1, (i:nat) => i<p.rank? p(i) : k);
        property(p.rank);
    }

    def this(cols:int, init: (i:nat)=>int) {
        super(cols, init);
        property(cols-1);
    }


    /**
     * natural sort order for halfspaces: from lo to hi on each
     * axis, from most major to least major axis, with constant as
     * least siginficant part of key
     */

    static def compare(a: Row, b: Row): int {
        for (var i: int = 0; i<a.cols; i++) {
            if (a(i) < b(i))
                return -1;
            else if (a(i) > b(i))
                return 1;
        }
        return 0;
    }


    /**
     * two halfspaces are parallel if all coefficients are the
     * same; constants may differ
     *
     * XXX only right if first coefficients are the same; needs to
     * allow for multiplication by positive constant
     */

    global def isParallel(that: PolyRow): boolean {
        for (var i: int = 0; i<cols-1; i++)
            if (this(i)!=that(i))
                return false;
        return true;
    }


    /**
     * halfspace is rectangular if only one coefficent is
     * non-zero
     */

    global def isRect(): boolean {
        var nz: boolean = false;
        for (var i: int = 0; i<cols-1; i++) {
            if (this(i)!=0) {
                if (nz) return false;
                nz = true;
            }
        }
        return true;
    }


    /**
     * determine whether point satisfies halfspace
     */

    global def contains(p: Point): boolean {
        var sum: int = this(rank);
        for (var i: int = 0; i<rank; i++)
            sum += this(i)*p(i);
        return sum <= 0;
    }


    /**
     * given
     *    a0*x0 + ... +ar   <=  0
     * complement is
     *    a0*x0 + ... +ar   >   0
     *   -a0*x0 - ... -ar   <   0
     *   -a0*x0 - ... -ar   <= -1
     *   -a0*x0 - ... -ar+1 <=  0
     */

    global def complement(): PolyRow {
        val init = (i:nat) => i<rank? -this(i) : -this(rank)+1;
        val as = Rail.makeVal[int](rank+1, init);
        return new PolyRow(as);
    }


    /**
     * print a halfspace in equation form
     */

    global def printEqn(ps: Printer, spc: String, row: int) {
        var sgn: int = 0;
        var first: boolean = true;
        for (var i: int = 0; i<cols-1; i++) {
            if (sgn==0) {
                if (this(i)<0)
                    sgn = -1;
                else if (this(i)>0)
                    sgn = 1;
            }
            val c = sgn*this(i);
            if (c==1) {
                if (first)
                    ps.print("x" + i);
                else
                    ps.print("+x" + i);
            } else if (c==-1)
                ps.print("-x" + i);
            else if (c!=0)
                ps.print((c>=0&&!first?"+":"") + c + "*x" + i + " ");
            if (c!=0)
                first = false;
        }
        if (first)
            ps.print("0");
        if (sgn>0)
            ps.print(spc + "<=" + spc + (-this(cols-1)));
        else
            ps.print(spc + ">=" + spc + (this(cols-1)));
    }

}
