package org.checkerframework.checker.index;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.ExecutableElement;

import org.checkerframework.checker.index.qual.*;
import org.checkerframework.common.basetype.BaseTypeChecker;
import org.checkerframework.common.basetype.BaseTypeVisitor;
import org.checkerframework.framework.source.Result;
import org.checkerframework.framework.type.AnnotatedTypeMirror;
import org.checkerframework.javacutil.AnnotationUtils;
import org.checkerframework.javacutil.TreeUtils;
import org.checkerframework.checker.compilermsgs.qual.CompilerMessageKey;

import com.sun.source.tree.ArrayAccessTree;
import com.sun.source.tree.ExpressionTree;
import com.sun.source.tree.MethodInvocationTree;

//**************************************************************************//
//a visitor that enforces the type rules for our system
//**************************************************************************//
public class IndexVisitor extends BaseTypeVisitor<IndexAnnotatedTypeFactory> {
	
	protected final ExecutableElement IndexValueElement;
	protected final ExecutableElement ListGet;
	protected final ProcessingEnvironment env;

	private static final /*@CompilerMessageKey*/ String ARRAY_HIGH = "array.access.unsafe.high";
	private static final /*@CompilerMessageKey*/ String ARRAY_NAME = "array.access.unsafe.name";
	private static final /*@CompilerMessageKey*/ String ARRAY_UNSAFE = "array.access.unsafe";
	private static final /*@CompilerMessageKey*/ String ARRAY_LOW = "array.access.unsafe.low";
	private static final /*@CompilerMessageKey*/ String LIST_UNSAFE = "list.access.unsafe";
	private static final /*@CompilerMessageKey*/ String LIST_UNSAFE_NAME = "list.access.unsafe.name";

	
	public IndexVisitor(BaseTypeChecker checker) {
		super(checker);
		env = checker.getProcessingEnvironment();
		IndexValueElement = TreeUtils.getMethod("org.checkerframework.checker.index.qual.IndexFor", "value", 0, env);
		ListGet = TreeUtils.getMethod("java.util.List", "get", 1, env);
	}
	// visit an array access
	// if we arent using an IndexFor the right array, give a warning
	@Override
	public Void visitArrayAccess(ArrayAccessTree tree, Void type) {
		ExpressionTree index = tree.getIndex();
		String name = tree.getExpression().toString();
		AnnotatedTypeMirror indexType = atypeFactory.getAnnotatedType(index);
		// warn if not Index for
		if (!indexType.hasAnnotation(IndexFor.class)) {
			String message = "Potentially unsafe array access: only use @IndexFor as index. Found: " + indexType.toString();
			if (indexType.hasAnnotation(NonNegative.class) || indexType.hasAnnotation(IndexOrHigh.class)) {
				checker.report(Result.warning(ARRAY_HIGH, name, indexType.toString()), index);
			} else if (indexType.hasAnnotation(LTLength.class) || indexType.hasAnnotation(IndexOrLow.class)) {
				checker.report(Result.warning(ARRAY_LOW, name, indexType.toString()), index);
			} else {
				checker.report(Result.warning(ARRAY_UNSAFE, name, indexType.toString()), index);
			}
		}
		// warn if it is IndexFor nut not the right array
		else if (!(getIndexValue(indexType.getAnnotation(IndexFor.class), IndexValueElement).equals(name))) {
			checker.report(Result.warning(ARRAY_NAME, name, indexType.toString()), index);
		}
		return super.visitArrayAccess(tree, type);
	}
	
	// visits method invocations
	// if you try a list.get(i) it warns if i isnt indexfor(list)
	@Override
	public Void visitMethodInvocation(MethodInvocationTree tree, Void type) {
		String name = tree.getMethodSelect().toString();
		if (TreeUtils.isMethodInvocation(tree, ListGet, env)) {
			ExpressionTree index = tree.getArguments().get(0);
			String listName = name.split("\\.")[0];
			AnnotatedTypeMirror indexType = atypeFactory.getAnnotatedType(index);
			if (!indexType.hasAnnotation(IndexFor.class)) {
				checker.report(Result.warning(LIST_UNSAFE, listName, indexType.toString()), index);
			}
			else if (!(getIndexValue(indexType.getAnnotation(IndexFor.class), IndexValueElement).equals(listName))) {
				checker.report(Result.warning(LIST_UNSAFE_NAME, listName, indexType.toString()), index);
			}
			
		}
		return super.visitMethodInvocation(tree, type);
		
	}
	
	// returns the value of an IndexFor annotation, given the annotation and Value method
	protected static String getIndexValue(AnnotationMirror indexType, ExecutableElement IndexValueElement) {
		return (String) AnnotationUtils.getElementValuesWithDefaults(indexType).get(IndexValueElement).getValue();
	}

}
