package org.checkerframework.checker.index;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.util.Elements;

import org.checkerframework.checker.index.qual.IndexBottom;
import org.checkerframework.checker.index.qual.IndexFor;
import org.checkerframework.checker.index.qual.IndexOrHigh;
import org.checkerframework.checker.index.qual.IndexOrLow;
import org.checkerframework.checker.index.qual.LTLength;
import org.checkerframework.checker.index.qual.NonNegative;
import org.checkerframework.checker.index.qual.Unknown;
import org.checkerframework.dataflow.cfg.node.Node;
import org.checkerframework.javacutil.AnnotationUtils;
import org.checkerframework.javacutil.TreeUtils;

import com.sun.source.tree.LiteralTree;
import com.sun.source.tree.Tree;
import com.sun.source.tree.Tree.Kind;

public class IndexUtils {

	// returns the value method specific to the class of the anno passed in
	static ExecutableElement getValueMethod(AnnotationMirror anno) {
		if (AnnotationUtils.areSameIgnoringValues(anno, IndexAnnotatedTypeFactory.IndexFor)) {
			return TreeUtils.getMethod("org.checkerframework.checker.index.qual.IndexFor", "value", 0, IndexAnnotatedTypeFactory.env);
		}
		if (AnnotationUtils.areSameIgnoringValues(anno, IndexAnnotatedTypeFactory.IndexOrLow)) {
			return TreeUtils.getMethod("org.checkerframework.checker.index.qual.IndexOrLow", "value", 0, IndexAnnotatedTypeFactory.env);
		}
		if (AnnotationUtils.areSameIgnoringValues(anno, IndexAnnotatedTypeFactory.IndexOrHigh)) {
			return TreeUtils.getMethod("org.checkerframework.checker.index.qual.IndexOrHigh", "value", 0, IndexAnnotatedTypeFactory.env);
		}
		if (AnnotationUtils.areSameIgnoringValues(anno, IndexAnnotatedTypeFactory.LTLength)) {
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
	 */
	public static String getValue(AnnotationMirror anno) {
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
}
