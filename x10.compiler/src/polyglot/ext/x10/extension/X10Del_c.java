/**
 * 
 */
package polyglot.ext.x10.extension;

import java.util.Collections;
import java.util.List;

import polyglot.ast.JL_c;
import polyglot.ast.Node;
import polyglot.ast.NodeFactory;
import polyglot.ext.x10.ast.AnnotationNode;
import polyglot.ext.x10.types.X10ClassType;
import polyglot.ext.x10.types.X10TypeSystem;
import polyglot.frontend.ExtensionInfo;
import polyglot.types.Type;
import polyglot.util.CollectionUtil;
import polyglot.visit.NodeVisitor;

/**
 * @author nystrom
 * 
 */
public class X10Del_c extends JL_c implements X10Del {

	/**
	 * 
	 */
	public X10Del_c() {
		super();
	}

	public boolean generated() {
		return ((X10Ext) node().ext()).generated();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see polyglot.ext.x10.extension.X10Ext#comment()
	 */
	public String comment() {
		return ((X10Ext) node().ext()).comment();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see polyglot.ext.x10.extension.X10Ext#comment(java.lang.String)
	 */
	public X10Ext comment(String comment) {
		return ((X10Ext) node().ext()).comment(comment);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see polyglot.ext.x10.extension.X10Ext#setComment(java.lang.String)
	 */
	public Node setComment(String comment) {
		return ((X10Ext) node().ext()).setComment(comment);
	}

	public Node setGenerated(boolean generated) {
		return ((X10Ext) node().ext()).setGenerated(generated);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seepolyglot.ext.x10.extension.X10Ext#rewrite(polyglot.ext.x10.types.
	 * X10TypeSystem, polyglot.ast.NodeFactory, polyglot.frontend.ExtensionInfo)
	 */
	public Node rewrite(X10TypeSystem ts, NodeFactory nf, ExtensionInfo info) {
		return ((X10Ext) node().ext()).rewrite(ts, nf, info);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see polyglot.ext.x10.extension.X10Ext#annotations()
	 */
	public List<AnnotationNode> annotations() {
		if (node().ext() == null)
			return Collections.EMPTY_LIST;
		return ((X10Ext) node().ext()).annotations();
	}

	public List<X10ClassType> annotationTypes() {
		if (node().ext() == null)
			return Collections.EMPTY_LIST;
		return ((X10Ext) node().ext()).annotationTypes();
	}

	public List<X10ClassType> annotationMatching(Type t) {
		if (node().ext() == null)
			return null;
		return ((X10Ext) node().ext()).annotationMatching(t);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see polyglot.ext.x10.extension.X10Ext#annotations(java.util.List)
	 */
	public Node annotations(List<AnnotationNode> annotations) {
		return ((X10Ext) node().ext()).annotations(annotations);
	}

	/** Override visitChildren for all nodes so annotations are visited. */
	public Node visitChildren(NodeVisitor v) {
		List<AnnotationNode> oldAnnotations = annotations();
		Node n = super.visitChildren(v);
		if (oldAnnotations == null || oldAnnotations.isEmpty()) {
			return n;
		}
		List<AnnotationNode> newAnnotations = node().visitList(oldAnnotations,
				v);
		if (!CollectionUtil.equals(oldAnnotations, newAnnotations)) {
			return ((X10Del) n.del()).annotations(newAnnotations);
		}
		return n;
	}
}
