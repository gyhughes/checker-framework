package org.checkerframework.checker.index;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;

import org.checkerframework.checker.index.qual.IndexBottom;
import org.checkerframework.checker.index.qual.IndexFor;
import org.checkerframework.checker.index.qual.IndexOrHigh;
import org.checkerframework.checker.index.qual.IndexOrLow;
import org.checkerframework.checker.index.qual.LTLength;
import org.checkerframework.checker.index.qual.NonNegative;
import org.checkerframework.checker.index.qual.Unknown;
import org.checkerframework.common.basetype.BaseTypeChecker;
import org.checkerframework.framework.type.AnnotatedTypeFactory;
import org.checkerframework.framework.type.AnnotatedTypeMirror;
import org.checkerframework.framework.type.GenericAnnotatedTypeFactory;
import org.checkerframework.framework.type.QualifierHierarchy;
import org.checkerframework.framework.type.treeannotator.ImplicitsTreeAnnotator;
import org.checkerframework.framework.type.treeannotator.ListTreeAnnotator;
import org.checkerframework.framework.type.treeannotator.PropagationTreeAnnotator;
import org.checkerframework.framework.type.treeannotator.TreeAnnotator;
import org.checkerframework.framework.util.AnnotationBuilder;
import org.checkerframework.framework.util.GraphQualifierHierarchy;
import org.checkerframework.framework.util.MultiGraphQualifierHierarchy.MultiGraphFactory;
import org.checkerframework.javacutil.AnnotationUtils;
import org.checkerframework.javacutil.Pair;
import org.checkerframework.javacutil.TreeUtils;

import com.sun.source.tree.BinaryTree;
import com.sun.source.tree.ExpressionTree;
import com.sun.source.tree.LiteralTree;
import com.sun.source.tree.MethodInvocationTree;
import com.sun.source.tree.Tree;
import com.sun.source.tree.UnaryTree;

//*************************************************************//
// Defines the hierarchy and intro rules for the index checker //
//*************************************************************//
public class IndexAnnotatedTypeFactory
extends GenericAnnotatedTypeFactory<IndexValue, IndexStore, IndexTransfer, IndexAnalysis> {

	// base annotations
	protected static AnnotationMirror indexFor;
	protected static AnnotationMirror indexBottom;
	protected static AnnotationMirror indexOrLow;
	protected static AnnotationMirror indexOrHigh;
	protected static AnnotationMirror lTLength;
	protected static AnnotationMirror nonNegative;
	protected static AnnotationMirror unknown;

	// methods to get values
	protected static ProcessingEnvironment env;

	public IndexAnnotatedTypeFactory(BaseTypeChecker checker) {
		super(checker);
		indexFor = AnnotationUtils.fromClass(elements, IndexFor.class);
		indexBottom = AnnotationUtils.fromClass(elements, IndexBottom.class);
		indexOrLow = AnnotationUtils.fromClass(elements, IndexOrLow.class);
		indexOrHigh = AnnotationUtils.fromClass(elements, IndexOrHigh.class);
		lTLength = AnnotationUtils.fromClass(elements, LTLength.class);
		nonNegative = AnnotationUtils.fromClass(elements, NonNegative.class);
		unknown = AnnotationUtils.fromClass(elements, Unknown.class);

		env = checker.getProcessingEnvironment();
		this.postInit();
	}

	@Override
	public TreeAnnotator createTreeAnnotator() {
		return new ListTreeAnnotator(
				new ImplicitsTreeAnnotator(this),
				new IndexTreeAnnotator(this),
				new PropagationTreeAnnotator(this)
				);
	}


	@Override
	protected IndexAnalysis createFlowAnalysis(List<Pair<VariableElement, IndexValue>> fieldvalues) {
		return new IndexAnalysis(checker, this, fieldvalues);
	}

	private class IndexTreeAnnotator extends TreeAnnotator {
		public IndexTreeAnnotator(AnnotatedTypeFactory atypeFactory) {
			super(atypeFactory);
		}

		@Override
		public Void visitLiteral(LiteralTree tree, AnnotatedTypeMirror type) {
			// if this is an Integer specifically
			if (tree.getKind() == Tree.Kind.INT_LITERAL) {
				int val = (int) tree.getValue();
				if (val >= 0) {
					type.addAnnotation(createNonNegAnnotation());
				}
			} // no else, only annotate Integers
			return super.visitLiteral(tree, type);
		}

		@Override
		public Void visitMethodInvocation(MethodInvocationTree tree, AnnotatedTypeMirror type) {
			ExecutableElement ListSize = TreeUtils.getMethod("java.util.List", "size", 0, env);
			ExecutableElement StrLen = TreeUtils.getMethod("java.lang.String", "length", 0, env);
			String name = tree.getMethodSelect().toString();
			if (TreeUtils.isMethodInvocation(tree, ListSize, env) || TreeUtils.isMethodInvocation(tree, StrLen, env)) {
				String listName = name.split("\\.")[0];
				type.removeAnnotationInHierarchy(indexFor);
				type.addAnnotation(createIndexOrHighAnnotation(listName));
			}
			return super.visitMethodInvocation(tree, type);
		}

		//*****************************************************************//
		// these are the methods that handle Binary operations (+- etc.)    //
		//*****************************************************************//
		public Void visitBinary(BinaryTree tree, AnnotatedTypeMirror type) {
			ExpressionTree left = tree.getLeftOperand();
			ExpressionTree right = tree.getRightOperand();
			switch (tree.getKind()) {
			// call both directions for commutativity
			case PLUS:
				plusHelper(left, right, type);
				break;
			case MINUS:
				minusHelper(left, right, type);
				break;
			default:
				break;
			}
			if (!type.isAnnotatedInHierarchy(indexFor)) {
				type.addAnnotation(createUnknownAnnotation());
			}
			return super.visitBinary(tree, type);
		}

		// do addition between types
		public void plusHelper(ExpressionTree leftExpr, ExpressionTree rightExpr, AnnotatedTypeMirror type) {
			AnnotatedTypeMirror leftType = getAnnotatedType(leftExpr);
			AnnotatedTypeMirror rightType = getAnnotatedType(rightExpr);
			// if left is literal 1/0 and right is not a literal swap them because we already handle the transfer for that
			// and it would be redundant to repeat it all again
			// we don't want right to be a literal too b/c we could be swapping forever
			if (leftExpr.getKind() == Tree.Kind.INT_LITERAL && !(rightExpr.getKind() == Tree.Kind.INT_LITERAL)) {
				int val = (int)((LiteralTree)leftExpr).getValue();
				if (val == 1 || val == 0) {
					plusHelper(rightExpr, leftExpr, type);
					return;
				}
			}
			// if the right side is a literal we do some special stuff(specifically for 1 and 0)
			if (rightExpr.getKind() == Tree.Kind.INT_LITERAL) {
				int val = (int)((LiteralTree)rightExpr).getValue();
				if (val == 1) {
					if (leftType.hasAnnotation(IndexOrLow.class) || leftType.hasAnnotation(IndexFor.class)) {
						String value = IndexUtils.getValue(leftType.getAnnotationInHierarchy(indexOrLow));
						type.addAnnotation(createIndexOrHighAnnotation(value));						
					}
					else if (leftType.hasAnnotation(NonNegative.class) || leftType.hasAnnotation(IndexOrHigh.class)) {
						type.addAnnotation(createNonNegAnnotation());
					}
					return;
				}
				// if we are adding 0 change nothing
				else if (val == 0) {
					type.addAnnotation(leftType.getAnnotationInHierarchy(indexFor));
					return;
				}
			}
			// anything a subtype of nonneg + subtype nonneg = nonnegative
			if (rightType.hasAnnotationRelaxed(indexFor) || rightType.hasAnnotationRelaxed(indexOrHigh) || rightType.hasAnnotation(nonNegative)) {
				if (leftType.hasAnnotation(NonNegative.class) || leftType.hasAnnotation(IndexOrHigh.class)|| leftType.hasAnnotation(IndexFor.class)) {
					type.addAnnotation(createNonNegAnnotation());
				}
			}
		}

		// do subtraction between types
		public void minusHelper(ExpressionTree leftExpr, ExpressionTree rightExpr, AnnotatedTypeMirror type) {
			IndexQualifierHierarchy hierarchy = (IndexQualifierHierarchy) qualHierarchy;
			AnnotatedTypeMirror leftType = getAnnotatedType(leftExpr);
			AnnotatedTypeMirror rightType = getAnnotatedType(rightExpr);
			AnnotationMirror anno = leftType.getAnnotationInHierarchy(indexFor);
			// if right side is 1
			if (rightExpr.getKind() == Tree.Kind.INT_LITERAL) {
				int val = (int)((LiteralTree)rightExpr).getValue();
				if (val == 1) {
					// if left sub IOH it becomes IOL
					if (hierarchy.isSubtypeRelaxed(anno, indexOrHigh)) {
						String value = IndexUtils.getIndexValue(anno, IndexUtils.getValueMethod(anno));
						type.addAnnotation(createIndexOrLowAnnotation(value));						
					}
					// if left subtype LTLength
					else if (hierarchy.isSubtypeRelaxed(anno, lTLength)) {
						String value = IndexUtils.getIndexValue(anno, IndexUtils.getValueMethod(anno));
						type.addAnnotation(createLTLengthAnnotation(value));	
					}
					return;
				}
				if (val == 0) {
					type.addAnnotation(anno);
					return ;
				}
			}
			// if right is sub of NonNeg
			if (rightType.hasAnnotationRelaxed(indexFor) || rightType.hasAnnotationRelaxed(indexOrHigh) || rightType.hasAnnotation(nonNegative)) {
				if (hierarchy.isSubtypeRelaxed(anno, lTLength) || leftType.hasAnnotation(IndexOrHigh.class)) {
					String value = IndexUtils.getIndexValue(anno, IndexUtils.getValueMethod(anno));
					type.addAnnotation(createLTLengthAnnotation(value));
				}
			}
		}

		// make increments and decrements work properly
		@Override
		public Void visitUnary(UnaryTree tree,  AnnotatedTypeMirror type) {
			AnnotatedTypeMirror left = getAnnotatedType(tree.getExpression());
			switch(tree.getKind()) {
			case PREFIX_INCREMENT:
				prefixIncrementHelper(left, type);
				break;
			case PREFIX_DECREMENT:
				prefixDecrementHelper(left, type);
				break;
			case POSTFIX_INCREMENT:
				// works same as preIncrement so use that
				prefixIncrementHelper(left, type);
				break;
			default:
				break;
			}
			// if we removed all the annotations, give it unknown
			if (!type.isAnnotatedInHierarchy(indexFor)) {
				type.addAnnotation(createUnknownAnnotation());
			}
			return super.visitUnary(tree, type);
		}

		// if we increment by one do a transfer
		// IndexOrLow || IndexFor -> IndexOrHigh
		// NonNeg || IndexOrHigh -> NonNeg
		// else -> Unknown
		protected void prefixIncrementHelper(AnnotatedTypeMirror atm, AnnotatedTypeMirror type) {
			if (atm.hasAnnotation(IndexOrLow.class) || atm.hasAnnotation(IndexFor.class)) {
				String value = IndexUtils.getValue(atm.getAnnotationInHierarchy(indexOrLow));
				type.addAnnotation(createIndexOrHighAnnotation(value));						
			}
			else if (atm.hasAnnotation(NonNegative.class) || atm.hasAnnotation(IndexOrHigh.class)) {
				type.addAnnotation(createNonNegAnnotation());
			}
		}

		// transfers for decrement
		// IndexOrHigh or IndexFor -> IndexOrLow
		// LTLength or IndexOrLow -> LTLength
		// NonNeg or Unknown -> Unknown
		private void prefixDecrementHelper(AnnotatedTypeMirror atm, AnnotatedTypeMirror type) {
			if (atm.hasAnnotation(IndexOrHigh.class) || atm.hasAnnotation(IndexFor.class)) {
				String value = IndexUtils.getValue(atm.getAnnotationInHierarchy(indexFor));
				type.addAnnotation(createIndexOrLowAnnotation(value));						
			}
			else if (atm.hasAnnotation(LTLength.class) || atm.hasAnnotation(IndexOrLow.class)) {
				String value = IndexUtils.getValue(atm.getAnnotationInHierarchy(indexFor));
				type.addAnnotation(createLTLengthAnnotation(value));
			}
		}
	}

	@Override
	protected Set<Class<? extends Annotation>> createSupportedTypeQualifiers() {
		return getBundledTypeQualifiersWithPolyAll(IndexFor.class);
	}

	@Override
	public QualifierHierarchy createQualifierHierarchy(MultiGraphFactory factory) {
		return new IndexQualifierHierarchy(factory, indexBottom);
	}

	//********************************************************************************//
	// This is the class that handles the subtyping for our qualifiers                //
	//********************************************************************************//	
	private final class IndexQualifierHierarchy extends GraphQualifierHierarchy {

		public IndexQualifierHierarchy(MultiGraphFactory f, AnnotationMirror bottom) {
			super(f, bottom);
		}

		@Override
		public AnnotationMirror leastUpperBound(AnnotationMirror anno1, AnnotationMirror anno2) {
			if (isSubtype(anno1, anno2)) {
				return anno2;
			}
			if (isSubtype(anno2, anno1)) {
				return anno1;
			}
			if (isSubtype(anno1, nonNegative) && isSubtype(anno2, nonNegative)) {
				return nonNegative;
			}
			return unknown;
		}
		@Override
		public boolean isSubtype(AnnotationMirror rhs, AnnotationMirror lhs) {
			boolean rightNonNeg = AnnotationUtils.areSameIgnoringValues(rhs, nonNegative);
			boolean rightUnknown = AnnotationUtils.areSameIgnoringValues(rhs, unknown);
			boolean rightBottom = AnnotationUtils.areSameIgnoringValues(rhs, indexBottom);
			boolean rightHasValueMethod = !(rightNonNeg || rightUnknown || rightBottom);

			boolean leftNonNeg = AnnotationUtils.areSameIgnoringValues(lhs, nonNegative);
			boolean leftUnknown = AnnotationUtils.areSameIgnoringValues(lhs, unknown);
			boolean leftBottom = AnnotationUtils.areSameIgnoringValues(lhs, indexBottom);
			boolean leftHasValueMethod = !(leftNonNeg || leftUnknown || leftBottom);
			if (rightHasValueMethod && leftHasValueMethod) {
				String valueRight = IndexUtils.getIndexValue(rhs, IndexUtils.getValueMethod(rhs));
				String valueLeft  = IndexUtils.getIndexValue(lhs, IndexUtils.getValueMethod(lhs));
				if (!valueRight.equals(valueLeft)) {
					return false;
				}
			}
			return super.isSubtype(removeValue(rhs), removeValue(lhs));
		}
		// gives subtyping information but ignores all values
		public boolean isSubtypeRelaxed(AnnotationMirror rhs, AnnotationMirror lhs) {
			return super.isSubtype(removeValue(rhs), removeValue(lhs));
		}

		// get the type annotation without the value
		private AnnotationMirror removeValue(AnnotationMirror type) {
			if (AnnotationUtils.areSameIgnoringValues(type, lTLength)) {
				return lTLength;
			}
			else if (AnnotationUtils.areSameIgnoringValues(type, indexFor)) {
				return indexFor;
			}
			else if (AnnotationUtils.areSameIgnoringValues(type, indexOrHigh)) {
				return indexOrHigh;
			}
			else if (AnnotationUtils.areSameIgnoringValues(type, indexOrLow)) {
				return indexOrLow;
			}
			return type;
		}
	}

	//********************************************************************************//
	// These are the methods that build the annotations we apply using this factory   //
	//********************************************************************************//	

	//returns a new @NonNegative annotation
	AnnotationMirror createNonNegAnnotation() {
		AnnotationBuilder builder = new AnnotationBuilder(processingEnv, NonNegative.class);
		return builder.build();
	}
	//returns a new @Unknown annotation
	AnnotationMirror createUnknownAnnotation() {
		AnnotationBuilder builder = new AnnotationBuilder(processingEnv, Unknown.class);
		return builder.build();
	}

	//returns a new @IndexOrLow annotation
	AnnotationMirror createIndexOrLowAnnotation(String name) {
		AnnotationBuilder builder = new AnnotationBuilder(processingEnv, IndexOrLow.class);
		builder.setValue("value", name);
		return builder.build();
	}

	//returns a new @IndexOrHigh annotation
	AnnotationMirror createIndexOrHighAnnotation(String name) {
		AnnotationBuilder builder = new AnnotationBuilder(processingEnv, IndexOrHigh.class);
		builder.setValue("value", name);
		return builder.build();
	}

	//returns a new @LTLength annotation
	AnnotationMirror createLTLengthAnnotation(String name) {
		AnnotationBuilder builder = new AnnotationBuilder(processingEnv, LTLength.class);
		builder.setValue("value", name);
		return builder.build();
	}

	//returns a new @IndexFor annotation
	AnnotationMirror createIndexForAnnotation(String name) {
		AnnotationBuilder builder = new AnnotationBuilder(processingEnv, IndexFor.class);
		builder.setValue("value", name);
		return builder.build();
	}
}
