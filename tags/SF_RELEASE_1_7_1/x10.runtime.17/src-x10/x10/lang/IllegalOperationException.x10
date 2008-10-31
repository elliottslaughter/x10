// (C) Copyright IBM Corporation 2006-2008.
// This file is part of X10 Language.

package x10.lang;

public class IllegalOperationException extends RuntimeException {
    public def this() = super("unsupported operation");
    public def this(msg: String) = super(msg);
}
