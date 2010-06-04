/*
 *
 * (C) Copyright IBM Corporation 2006-2008
 *
 *  This file is part of X10 Language.
 *
 */
package x10.extension;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import polyglot.ast.Ext;
import polyglot.ast.Node;
import polyglot.ast.NodeFactory;
import polyglot.frontend.ExtensionInfo;
import polyglot.types.ClassType;
import polyglot.types.Type;
import x10.ast.AnnotationNode;
import x10.types.X10ClassType;
import x10.types.X10TypeSystem;

public interface X10Ext extends Ext {
    /**
      * Comment adjacent to the node in the source code.
      * @return the comment
      */
    public String comment();
    
    /**
      * Clone the extension object and set its comment.
      * @param comment
      * @return a new extension object
      */
    public X10Ext comment(String comment);
    
    /**
      * Clone the extension object and the node and set its comment.
      * @param comment
      * @return a new Node
      */
    public Node setComment(String comment);
    
    /**
     * Annotation on the node.
     * @return
     */
    public List<AnnotationNode> annotations();
    public List<X10ClassType> annotationTypes();
    public List<X10ClassType> annotationMatching(Type t);
    
    /**
     * Set the annotations.
     * @param annotations
     * @return
     */
    public Node annotations(List<AnnotationNode> annotations);
}
