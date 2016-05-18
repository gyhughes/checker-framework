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
	protected final AnnotationMirror IndexFor;
	protected final AnnotationMirror IndexBottom;
	protected final AnnotationMirror IndexOrLow;
	protected final AnnotationMirror IndexOrHigh;
	protected final AnnotationMirror LTLength;
	protected final AnnotationMirror NonNegative;
	protected final AnnotationMirror Unknown;

	// methods to get values
	protected final ProcessingEnvironment env;

	public IndexAnnotatedTypeFactory(BaseTypeChecker checker) {
		super(checker);
		IndexFor = AnnotationUtils.fromClass(elements, IndexFor.class);
		IndexBottom = AnnotationUtils.fromClass(elements, IndexBottom.class);
		IndexOrLow = AnnotationUtils.fromClass(elements, IndexOrLow.class);
		IndexOrHigh = AnnotationUtils.fromClass(elements, IndexOrHigh.class);
		LTLength = AnnotationUtils.fromClass(elements, LTLength.class);
		NonNegative = AnnotationUtils.fromClass(elements, NonNegative.class);
		Unknown = AnnotationUtils.fromClass(elements, Unknown.class);

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
			if (!type.isAnnotatedInHierarchy(AnnotationUtils.fromClass(elements, NonNegative.class))) {
				if (tree.getKind() == Tree.Kind.INT_LITERAL) {
					int val = (int) tree.getValue();
					if (val >= 0) {
						type.addAnnotation(createNonNegAnnotation());
					}
				}
			}
			return super.visitLiteral(tree, type);
		}
		
		@Override
		public Void visitMethodInvocation(MethodInvocationTree tree, AnnotatedTypeMirror type) {
			ExecutableElement ListSize = TreeUtils.getMethod("java.util.List", "size", 0, env);
			String name = tree.getMethodSelect().toString();
			if (TreeUtils.isMethodInvocation(tree, ListSize, env)) {
				String listName = name.split("\\.")[0];
				type.removeAnnotationInHierarchy(IndexFor);
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
				visitPlus(left, right, type, true);
				break;
			case MINUS:
				visitMinus(left, right, type);
				break;
			default:
				break;
			}
			if (!type.isAnnotatedInHierarchy(IndexFor)) {
				type.addAnnotation(createUnknownAnnotation());
			}
			return super.visitBinary(tree, type);
		}

		// do addition between types
		public void visitPlus(ExpressionTree leftExpr, ExpressionTree rightExpr, AnnotatedTypeMirror type, boolean first) {
			AnnotatedTypeMirror left = getAnnotatedType(leftExpr);
			AnnotatedTypeMirror right = getAnnotatedType(rightExpr);
			// if left is literal 1/0 swap sides because we can handle that.
			if (leftExpr.getKind() == Tree.Kind.INT_LITERAL && first) {
				int val = (int)((LiteralTree)leftExpr).getValue();
				if (val == 1 || val == 0) {
					visitPlus(rightExpr, leftExpr, type, false);
					return;
				}
			}


				// if the right side is a literal we do some special stuff(specifically for 1 an d0)
				if (rightExpr.getKind() == Tree.Kind.INT_LITERAL) {
					int val = (int)((LiteralTree)rightExpr).getValue();
					if (val == 1) {
						if (left.hasAnnotation(IndexOrLow.class) || left.hasAnnotation(IndexFor.class)) {
							String value = IndexTransfer.getValue(left.getAnnotationInHierarchy(IndexOrLow));
							type.addAnnotation(createIndexOrHighAnnotation(value));						
						}
						else if (left.hasAnnotation(NonNegative.class) || left.hasAnnotation(IndexOrHigh.class)) {
							type.addAnnotation(createNonNegAnnotation());
						}
						return;
					}
					// if we are adding 0 change nothing
					else if (val == 0) {
						type.addAnnotation(left.getAnnotationInHierarchy(IndexFor));
						return;
					}
				}
				// anything a subtype of nonneg + subtype nonneg = nonnegative
				if (right.hasAnnotationRelaxed(IndexFor) || right.hasAnnotationRelaxed(IndexOrHigh) || right.hasAnnotation(NonNegative)) {
					if (left.hasAnnotation(NonNegative.class) || left.hasAnnotation(IndexOrHigh.class)|| left.hasAnnotation(IndexFor.class)) {
						type.addAnnotation(createNonNegAnnotation());
					}
				}

		}


		// do subtraction between types
		public void visitMinus(ExpressionTree leftExpr, ExpressionTree rightExpr, AnnotatedTypeMirror type) {
			IndexQualifierHierarchy hierarchy = (IndexQualifierHierarchy) qualHierarchy;
			AnnotatedTypeMirror left = getAnnotatedType(leftExpr);
			AnnotatedTypeMirror right = getAnnotatedType(rightExpr);
			for (AnnotationMirror anno: left.getAnnotations()) {
				// if right side is 1
				if (rightExpr.getKind() == Tree.Kind.INT_LITERAL) {
					int val = (int)((LiteralTree)rightExpr).getValue();
					if (val == 1) {
						// if left sub IOH it becomes IOL
						if (hierarchy.isSubtypeRelaxed(anno, IndexOrHigh)) {
							String value = IndexVisitor.getIndexValue(anno, getValueMethod(anno));
							type.addAnnotation(createIndexOrLowAnnotation(value));						
						}
						// if left subtype LTLength
						else if (hierarchy.isSubtypeRelaxed(anno, LTLength)) {
							String value = IndexVisitor.getIndexValue(anno, getValueMethod(anno));
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
				if (right.hasAnnotationRelaxed(IndexFor) || right.hasAnnotationRelaxed(IndexOrHigh) || right.hasAnnotation(NonNegative)) {
					if (hierarchy.isSubtypeRelaxed(anno, LTLength)) {
						String value = IndexVisitor.getIndexValue(anno, getValueMethod(anno));
						type.addAnnotation(createLTLengthAnnotation(value));
					}
				}
			}
		}

		// make increments and decrements work properly
		@Override
		public Void visitUnary(UnaryTree tree,  AnnotatedTypeMirror type) {
			switch(tree.getKind()) {
			case PREFIX_INCREMENT:
				preInc(tree, type);
				break;
			case PREFIX_DECREMENT:
				preDec(tree, type);
				break;
			case POSTFIX_INCREMENT:
				// works same as preIncrement so use that
				preInc(tree, type);
				break;
			default:
				break;
			}
			// if we removed all the annotations, give it unknown
			if (!type.isAnnotatedInHierarchy(IndexFor)) {
				type.addAnnotation(createUnknownAnnotation());
			}
			return super.visitUnary(tree, type);
		}
		
		// if we increment by one do a transfer
		// IndexOrLow || IndexFor -> IndexOrHigh
		// NonNeg || IndexOrHigh -> NonNeg
		// else -> Unknown
		private void preInc(UnaryTree tree, AnnotatedTypeMirror type) {
			AnnotatedTypeMirror left = getAnnotatedType(tree.getExpression());
			if (left.hasAnnotation(IndexOrLow.class) || left.hasAnnotation(IndexFor.class)) {
				String value = IndexTransfer.getValue(left.getAnnotationInHierarchy(IndexOrLow));
				type.addAnnotation(createIndexOrHighAnnotation(value));						
			}
			else if (left.hasAnnotation(NonNegative.class) || left.hasAnnotation(IndexOrHigh.class)) {
				type.addAnnotation(createNonNegAnnotation());
			}
		}
		
		// transfers for decrement
		// IndexOrHigh or IndexFor -> IndexOrLow
		// LTLength or IndexOrLow -> LTLength
		// NonNeg or Unknown -> Unknown
		private void preDec(UnaryTree tree, AnnotatedTypeMirror type) {
			AnnotatedTypeMirror  ATM = getAnnotatedType(tree.getExpression());
			if (ATM.hasAnnotation(IndexOrHigh.class) || ATM.hasAnnotation(IndexFor.class)) {
				String value = IndexTransfer.getValue(ATM.getAnnotationInHierarchy(IndexFor));
				type.addAnnotation(createIndexOrLowAnnotation(value));						
			}
			else if (ATM.hasAnnotation(LTLength.class) || ATM.hasAnnotation(IndexOrLow.class)) {
				String value = IndexTransfer.getValue(ATM.getAnnotationInHierarchy(IndexFor));
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
		return new IndexQualifierHierarchy(factory, IndexBottom);
	}

	//********************************************************************************//
	// This is the class that handles the subtyping for our qualifiers                //
	//********************************************************************************//	
	private final class IndexQualifierHierarchy extends GraphQualifierHierarchy {
		
		public IndexQualifierHierarchy(MultiGraphFactory f, AnnotationMirror bottom) {
			super(f, bottom);
		}
		
        @Override
        public AnnotationMirror leastUpperBound(AnnotationMirror a1, AnnotationMirror a2) {
        	if (isSubtype(a1, a2)) {
        		return a2;
        	}
        	if (isSubtype(a2, a1)) {
        		return a1;
        	}
        	if (isSubtype(a1, NonNegative) && isSubtype(a2, NonNegative)) {
        		return NonNegative;
        	}
            return Unknown;
        }
		@Override
		public boolean isSubtype(AnnotationMirror rhs, AnnotationMirror lhs) {
			boolean rightNonNeg = AnnotationUtils.areSameIgnoringValues(rhs, NonNegative);
			boolean rightUnknown = AnnotationUtils.areSameIgnoringValues(rhs, Unknown);
			boolean rightBottom = AnnotationUtils.areSameIgnoringValues(rhs, IndexBottom);
			boolean rightHasValueMethod = !(rightNonNeg || rightUnknown || rightBottom);

			boolean leftNonNeg = AnnotationUtils.areSameIgnoringValues(lhs, NonNegative);
			boolean leftUnknown = AnnotationUtils.areSameIgnoringValues(lhs, Unknown);
			boolean leftBottom = AnnotationUtils.areSameIgnoringValues(lhs, IndexBottom);
			boolean leftHasValueMethod = !(leftNonNeg || leftUnknown || leftBottom);
			if (rightHasValueMethod && leftHasValueMethod) {
				String valueRight = IndexVisitor.getIndexValue(rhs, getValueMethod(rhs));
				String valueLeft  = IndexVisitor.getIndexValue(lhs, getValueMethod(lhs));
				if (!valueRight.equals(valueLeft)) {
					return false;
				}
			}
			return super.isSubtype(refine(rhs), refine(lhs));
		}
		// gives subtyping information but ignores all values
		public boolean isSubtypeRelaxed(AnnotationMirror rhs, AnnotationMirror lhs) {
			return super.isSubtype(refine(rhs), refine(lhs));
		}

		// get the type annotation without the value
		private AnnotationMirror refine(AnnotationMirror type) {
			if (AnnotationUtils.areSameIgnoringValues(type, LTLength)) {
				return LTLength;
			}
			else if (AnnotationUtils.areSameIgnoringValues(type, IndexFor)) {
				return IndexFor;
			}
			else if (AnnotationUtils.areSameIgnoringValues(type, IndexOrHigh)) {
				return IndexOrHigh;
			}
			else if (AnnotationUtils.areSameIgnoringValues(type, IndexOrLow)) {
				return IndexOrLow;
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

	// returns the value method specific to the class of the anno passed in
	protected ExecutableElement getValueMethod(AnnotationMirror anno) {
		if (AnnotationUtils.areSameIgnoringValues(anno, IndexFor)) {
			return TreeUtils.getMethod("org.checkerframework.checker.index.qual.IndexFor", "value", 0, env);
		}
		if (AnnotationUtils.areSameIgnoringValues(anno, IndexOrLow)) {
			return TreeUtils.getMethod("org.checkerframework.checker.index.qual.IndexOrLow", "value", 0, env);
		}
		if (AnnotationUtils.areSameIgnoringValues(anno, IndexOrHigh)) {
			return TreeUtils.getMethod("org.checkerframework.checker.index.qual.IndexOrHigh", "value", 0, env);
		}
		if (AnnotationUtils.areSameIgnoringValues(anno, LTLength)) {
			return TreeUtils.getMethod("org.checkerframework.checker.index.qual.LTLength", "value", 0, env);
		}
		return null;
	}


}
