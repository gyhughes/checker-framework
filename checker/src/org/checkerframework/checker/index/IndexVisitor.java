package org.checkerframework.checker.index;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.ExecutableElement;

import org.checkerframework.checker.compilermsgs.qual.CompilerMessageKey;
import org.checkerframework.checker.index.qual.IndexFor;
import org.checkerframework.checker.index.qual.IndexOrHigh;
import org.checkerframework.checker.index.qual.IndexOrLow;
import org.checkerframework.checker.index.qual.LTLength;
import org.checkerframework.checker.index.qual.MinLen;
import org.checkerframework.checker.index.qual.NonNegative;
import org.checkerframework.common.basetype.BaseTypeChecker;
import org.checkerframework.common.basetype.BaseTypeVisitor;
import org.checkerframework.framework.source.Result;
import org.checkerframework.framework.type.AnnotatedTypeMirror;
import org.checkerframework.javacutil.TreeUtils;

import com.sun.source.tree.ArrayAccessTree;
import com.sun.source.tree.ExpressionTree;
import com.sun.source.tree.LiteralTree;
import com.sun.source.tree.MethodInvocationTree;
import com.sun.source.tree.Tree;

//**************************************************************************//
//a visitor that enforces the type rules for our system
//**************************************************************************//
public class IndexVisitor extends BaseTypeVisitor<IndexAnnotatedTypeFactory> {
	
	protected final ExecutableElement indexValueElement;
	protected final ExecutableElement listGet;
	protected final ExecutableElement charAt;
	protected final ProcessingEnvironment env;

	private static final /*@CompilerMessageKey*/ String ARRAY_HIGH = "array.access.unsafe.high";
	private static final /*@CompilerMessageKey*/ String ARRAY_NAME = "array.access.unsafe.name";
	private static final /*@CompilerMessageKey*/ String ARRAY_UNSAFE = "array.access.unsafe";
	private static final /*@CompilerMessageKey*/ String ARRAY_LOW = "array.access.unsafe.low";
	private static final /*@CompilerMessageKey*/ String LIST_HIGH = "list.access.unsafe.high";
	private static final /*@CompilerMessageKey*/ String LIST_LOW = "list.access.unsafe.low";
	private static final /*@CompilerMessageKey*/ String LIST_UNSAFE = "list.access.unsafe";
	private static final /*@CompilerMessageKey*/ String LIST_UNSAFE_NAME = "list.access.unsafe.name";
	private static final /*@CompilerMessageKey*/ String STRING_HIGH = "string.access.unsafe.high";
	private static final /*@CompilerMessageKey*/ String STRING_LOW = "string.access.unsafe.low";
	private static final /*@CompilerMessageKey*/ String STRING_UNSAFE = "string.access.unsafe";
	private static final /*@CompilerMessageKey*/ String STRING_UNSAFE_NAME = "string.access.unsafe.name";

	public IndexVisitor(BaseTypeChecker checker) {
		super(checker);
		env = checker.getProcessingEnvironment();
		indexValueElement = TreeUtils.getMethod("org.checkerframework.checker.index.qual.IndexFor", "value", 0, env);
		listGet = TreeUtils.getMethod("java.util.List", "get", 1, env);
		charAt = TreeUtils.getMethod("java.lang.String", "charAt", 1, env);
	}
	
	// if we aren't using an IndexFor the right array, give a warning
	@Override
	public Void visitArrayAccess(ArrayAccessTree tree, Void type) {
		ExpressionTree index = tree.getIndex();
		String arrName = tree.getExpression().toString();
		AnnotatedTypeMirror indexType = atypeFactory.getAnnotatedType(index);
		AnnotatedTypeMirror arrType = atypeFactory.getAnnotatedType(tree.getExpression());
		if (arrType.hasAnnotation(MinLen.class)) {
			if (tree.getIndex().getKind().equals(Tree.Kind.INT_LITERAL)) {
				int val = (int)((LiteralTree)tree.getIndex()).getValue();
				int minLen = IndexUtils.getMinLen(arrType.getAnnotation(MinLen.class));
				if (val < minLen) {
					return super.visitArrayAccess(tree, type);
				}
			}
		}
		// warn if not IndexFor
		if (!indexType.hasAnnotation(IndexFor.class)) {
			if (indexType.hasAnnotation(NonNegative.class) || indexType.hasAnnotation(IndexOrHigh.class)) {
				checker.report(Result.warning(ARRAY_HIGH, indexType.toString(), arrName), index);
			} else if (indexType.hasAnnotation(LTLength.class) || indexType.hasAnnotation(IndexOrLow.class)) {
				checker.report(Result.warning(ARRAY_LOW, indexType.toString(), arrName), index);
			} else {	// is unknown
				checker.report(Result.warning(ARRAY_UNSAFE, indexType.toString(), arrName), index);
			}
		}
		// warn if it is IndexFor but not the right array
		else if (!(IndexUtils.getValue(indexType.getAnnotation(IndexFor.class)).equals(arrName))) {
			checker.report(Result.warning(ARRAY_NAME, indexType.toString(), arrName), index);
		}
		return super.visitArrayAccess(tree, type);
	}
	
	// if you try a list.get(i) it warns if i isn't IndexFor(list)
	@Override
	public Void visitMethodInvocation(MethodInvocationTree tree, Void type) {
		String name = tree.getMethodSelect().toString();
		if (TreeUtils.isMethodInvocation(tree, listGet, env)) {
			ExpressionTree index = tree.getArguments().get(0);
			// method is list.get, split to get name of list
			String listName = name.split("\\.")[0];
			AnnotatedTypeMirror indexType = atypeFactory.getAnnotatedType(index);
			if (!indexType.hasAnnotation(IndexFor.class)) {
				if (indexType.hasAnnotation(NonNegative.class) || indexType.hasAnnotation(IndexOrHigh.class)) {
					checker.report(Result.warning(LIST_HIGH, indexType.toString(), listName), index);
				} else if (indexType.hasAnnotation(LTLength.class) || indexType.hasAnnotation(IndexOrLow.class)) {
					checker.report(Result.warning(LIST_LOW, indexType.toString(), listName), index);
				} else {	// is unknown
					checker.report(Result.warning(LIST_UNSAFE, indexType.toString(), listName), index);
				}			}
			else if (!(IndexUtils.getValue(indexType.getAnnotation(IndexFor.class)).equals(listName))) {
				checker.report(Result.warning(LIST_UNSAFE_NAME, indexType.toString(), listName), index);
			}
		} else if (TreeUtils.isMethodInvocation(tree, charAt, env)) {
			ExpressionTree index = tree.getArguments().get(0);
			// method is String.charAt split to get name
			String strName = name.split("\\.")[0];
			AnnotatedTypeMirror indexType = atypeFactory.getAnnotatedType(index);
			if (!indexType.hasAnnotation(IndexFor.class)) {
				if (indexType.hasAnnotation(NonNegative.class) || indexType.hasAnnotation(IndexOrHigh.class)) {
					checker.report(Result.warning(STRING_HIGH, indexType.toString(), strName), index);
				} else if (indexType.hasAnnotation(LTLength.class) || indexType.hasAnnotation(IndexOrLow.class)) {
					checker.report(Result.warning(STRING_LOW, indexType.toString(), strName), index);
				} else {	// is unknown
					checker.report(Result.warning(STRING_UNSAFE, indexType.toString(), strName), index);
				}
			}
			else if (!(IndexUtils.getValue(indexType.getAnnotation(IndexFor.class)).equals(strName))) {
				checker.report(Result.warning(STRING_UNSAFE_NAME, indexType.toString(), strName), index);
			}
		}
		return super.visitMethodInvocation(tree, type);
	}
}
