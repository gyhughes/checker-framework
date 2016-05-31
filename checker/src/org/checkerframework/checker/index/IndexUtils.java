package org.checkerframework.checker.index;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.ExecutableElement;

import org.checkerframework.checker.index.qual.*;
import org.checkerframework.dataflow.cfg.node.Node;
import org.checkerframework.javacutil.AnnotationUtils;
import org.checkerframework.javacutil.TreeUtils;

import com.sun.source.tree.ExpressionTree;
import com.sun.source.tree.LiteralTree;
import com.sun.source.tree.Tree;

public class IndexUtils {

	// returns the value method specific to the class of the anno passed in
	static ExecutableElement getValueMethod(AnnotationMirror anno) {
		if (AnnotationUtils.areSameIgnoringValues(anno, IndexAnnotatedTypeFactory.indexFor)) {
			return TreeUtils.getMethod("org.checkerframework.checker.index.qual.IndexFor", "value", 0, IndexAnnotatedTypeFactory.env);
		}
		if (AnnotationUtils.areSameIgnoringValues(anno, IndexAnnotatedTypeFactory.indexOrLow)) {
			return TreeUtils.getMethod("org.checkerframework.checker.index.qual.IndexOrLow", "value", 0, IndexAnnotatedTypeFactory.env);
		}
		if (AnnotationUtils.areSameIgnoringValues(anno, IndexAnnotatedTypeFactory.indexOrHigh)) {
			return TreeUtils.getMethod("org.checkerframework.checker.index.qual.IndexOrHigh", "value", 0, IndexAnnotatedTypeFactory.env);
		}
		if (AnnotationUtils.areSameIgnoringValues(anno, IndexAnnotatedTypeFactory.lTLength)) {
			return TreeUtils.getMethod("org.checkerframework.checker.index.qual.LTLength", "value", 0, IndexAnnotatedTypeFactory.env);
		}
		return null;
	}

	// returns the value of an IndexFor annotation, given the annotation and Value method
	static String getIndexValue(AnnotationMirror indexType, ExecutableElement IndexValueElement) {
		return (String) AnnotationUtils.getElementValuesWithDefaults(indexType).get(IndexValueElement).getValue();
	}

	/**
	 * returns the String value of an annotation in the IndexFor Hierarchy
	 * if anno does not have a value method throws IllegalArgumentException
	 */
	public static String getValue(AnnotationMirror anno) {
		if (!hasValueMethod(anno)) {
			throw new IllegalArgumentException("anno must have a value method");
		}
		return getIndexValue(anno, getValueMethod(anno));
	}

	/**
	 *  given a node this returns whether the node is the literal -1
	 * @param right
	 * 		a node we wish to test
	 * @return whether it represents -1 literal
	 */
	public static  boolean isNegOne(Node right) {
		if (right.getTree().getKind().equals(Tree.Kind.INT_LITERAL)) {
			int val = (int)((LiteralTree)right.getTree()).getValue();
			return val == -1;
		}
		return false;
	}
	/**
	 *  given a node this returns whether the node is greater than 0
	 * @param node
	 * 		a node we wish to test
	 * @return whether it is Greater than 0
	 */
	public static boolean isGTZero(Node node) {
		if (node.getTree().getKind().equals(Tree.Kind.INT_LITERAL)) {
			int val = (int)((LiteralTree)node.getTree()).getValue();
			return val > 0;
		}
		return false;
	}
	
	public static boolean hasValueMethod(AnnotationMirror anno) {
		boolean InF = AnnotationUtils.areSameByClass(anno, IndexFor.class);
		boolean IOH = AnnotationUtils.areSameByClass(anno, IndexOrHigh.class);
		boolean IOL = AnnotationUtils.areSameByClass(anno, IndexOrLow.class);
		boolean LTL = AnnotationUtils.areSameByClass(anno, LTLength.class);
		return (InF || IOH || IOL || LTL);
		
	}
	
	public static int getMinLen(AnnotationMirror annotation) {
		ExecutableElement valueMethod = TreeUtils.getMethod("org.checkerframework.checker.index.qual.MinLen", "value", 0, IndexAnnotatedTypeFactory.env);
		return (int) AnnotationUtils.getElementValuesWithDefaults(annotation).get(valueMethod).getValue();
	}

}
