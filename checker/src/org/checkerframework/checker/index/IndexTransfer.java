package org.checkerframework.checker.index;

import javax.lang.model.element.AnnotationMirror;

import org.checkerframework.checker.index.qual.*;
import org.checkerframework.dataflow.analysis.ConditionalTransferResult;
import org.checkerframework.dataflow.analysis.FlowExpressions;
import org.checkerframework.dataflow.analysis.FlowExpressions.Receiver;
import org.checkerframework.dataflow.analysis.RegularTransferResult;
import org.checkerframework.dataflow.analysis.TransferInput;
import org.checkerframework.dataflow.analysis.TransferResult;
import org.checkerframework.dataflow.cfg.node.*;
import org.checkerframework.framework.flow.CFAbstractTransfer;
import org.checkerframework.framework.flow.CFStore;
import org.checkerframework.framework.flow.CFValue;
import org.checkerframework.framework.type.AnnotatedTypeMirror;
import org.checkerframework.javacutil.AnnotationUtils;

import com.sun.source.tree.LiteralTree;
import com.sun.source.tree.Tree;


public class IndexTransfer extends CFAbstractTransfer<CFValue, CFStore, IndexTransfer> {
	protected IndexAnalysis analysis;
	protected static IndexAnnotatedTypeFactory atypeFactory;

	public IndexTransfer(IndexAnalysis analysis) {
		super(analysis);
		this.analysis = analysis;
		atypeFactory = (IndexAnnotatedTypeFactory) analysis.getTypeFactory();
	}

	// annotate arr.length to be IndexOrHigh("arr")
	@Override
	public TransferResult<CFValue, CFStore> visitFieldAccess(FieldAccessNode node, TransferInput<CFValue, CFStore> in) {
		TransferResult<CFValue, CFStore> result = super.visitFieldAccess(node, in);

		if (node.getFieldName().equals("length")) {
			String arrName = node.getReceiver().toString();
			if (arrName.contains(".")) {
				String[] objs = arrName.split("\\.");
				arrName = objs[objs.length -1];
			}
			AnnotationMirror anno = atypeFactory.createIndexOrHighAnnotation(arrName);
			CFValue newResultValue = analysis.createSingleAnnotationValue(anno, result.getResultValue().getType().getUnderlyingType());
			return new RegularTransferResult<>(newResultValue, result.getRegularStore());
		}

		return result;
	}

	//******************************************************//
	// these are methods that handle refining on comparisons//
	//******************************************************//
	
	// find the left hand sides annotation then passes it to the right method to handle it
	@Override
	public TransferResult<CFValue, CFStore> visitGreaterThan(GreaterThanNode node, TransferInput<CFValue, CFStore> in) {
		TransferResult<CFValue, CFStore> result = super.visitGreaterThan(node, in);
		Node left = node.getLeftOperand();
		Node right = node.getRightOperand();
		Receiver rec = FlowExpressions.internalReprOf(analysis.getTypeFactory(), left);
		AnnotatedTypeMirror leftType = atypeFactory.getAnnotatedType(left.getTree());

		CFStore thenStore = result.getRegularStore();
		CFStore elseStore = thenStore.copy();
		ConditionalTransferResult<CFValue, CFStore> newResult =
				new ConditionalTransferResult<>(result.getResultValue(), thenStore, elseStore);

		if (leftType.hasAnnotation(Unknown.class)) {
			UnknownGreaterThan(rec, right, thenStore, false);
		}
		if (leftType.hasAnnotation(IndexOrLow.class) || leftType.hasAnnotation(LTLength.class)) {
			AnnotationMirror leftAnno = leftType.getAnnotationInHierarchy(atypeFactory.IndexOrLow);
			String name = getValue(leftAnno);
			IndexOrLowGreaterThan(rec, right, thenStore, name, false);
		}
//		if (leftType.hasAnnotation(LTLength.class)) {
//			AnnotationMirror leftAnno = leftType.getAnnotation(LTLength.class);
//			String name = getValue(leftAnno);
//			// we can use this method because it refines the same
//			IndexOrLowGreaterThan(rec, right, thenStore, name, false);
//		}
		return newResult;
	}

	// find the left hand sides annotation then passes it to the right method to handle it
	@Override
	public TransferResult<CFValue, CFStore> visitGreaterThanOrEqual(GreaterThanOrEqualNode node, TransferInput<CFValue, CFStore> in) {
		TransferResult<CFValue, CFStore> result = super.visitGreaterThanOrEqual(node, in);
		Node left = node.getLeftOperand();
		Node right = node.getRightOperand();
		Receiver rec = FlowExpressions.internalReprOf(analysis.getTypeFactory(), left);
		AnnotatedTypeMirror leftType = atypeFactory.getAnnotatedType(left.getTree());

		CFStore thenStore = result.getRegularStore();
		CFStore elseStore = thenStore.copy();
		ConditionalTransferResult<CFValue, CFStore> newResult =
				new ConditionalTransferResult<>(result.getResultValue(), thenStore, elseStore);

		if (leftType.hasAnnotation(Unknown.class)) {
			UnknownGreaterThan(rec, right, thenStore, true);
		}
		if (leftType.hasAnnotation(IndexOrLow.class) || leftType.hasAnnotation(LTLength.class)) {
			AnnotationMirror leftAnno = leftType.getAnnotationInHierarchy(atypeFactory.IndexOrLow);
			String name = getValue(leftAnno);
			IndexOrLowGreaterThan(rec, right, thenStore, name, true);
		}
//		if (leftType.hasAnnotation(LTLength.class)) {
//			AnnotationMirror leftAnno = leftType.getAnnotation(LTLength.class);
//			String name = getValue(leftAnno);
//			// we can use this method because it refines the same
//			IndexOrLowGreaterThanOrEqual(rec, right, thenStore, name);
//		}
		return newResult;
	}

	@Override
	public TransferResult<CFValue, CFStore> visitNotEqual(NotEqualNode node, TransferInput<CFValue, CFStore> in) {
		TransferResult<CFValue, CFStore> result = super.visitNotEqual(node, in);
		Node left = node.getLeftOperand();
		Node right = node.getRightOperand();
		Receiver rec = FlowExpressions.internalReprOf(analysis.getTypeFactory(), left);
		AnnotatedTypeMirror leftType = atypeFactory.getAnnotatedType(left.getTree());
		CFStore thenStore = result.getRegularStore();
		CFStore elseStore = thenStore.copy();
		ConditionalTransferResult<CFValue, CFStore> newResult =
				new ConditionalTransferResult<>(result.getResultValue(), thenStore, elseStore);
		// check if the right side is the field a.length
		// can't use IndexOrHigh because that might not be exactly the length
		if (leftType.hasAnnotation(IndexOrHigh.class)) {
			if (right instanceof FieldAccessNode) {
				FieldAccessNode FANode = (FieldAccessNode) right;
				if (FANode.getFieldName().equals("length")) {
					String arrName = FANode.getReceiver().toString();
					if (arrName.contains(".")) {
						String[] objs = arrName.split("\\.");
						arrName = objs[objs.length -1];
					}
					AnnotationMirror anno = atypeFactory.createIndexForAnnotation(arrName);
					thenStore.insertValue(rec, anno);
				}
			}

		}
		// have to check exactly -1 because indexorlow could be different
		else if (leftType.hasAnnotation(IndexOrLow.class)) {
			AnnotationMirror leftAnno = leftType.getAnnotation(IndexOrLow.class);
			String name = getValue(leftAnno);
			if (right.getTree().getKind().equals(Tree.Kind.INT_LITERAL) && (int)((LiteralTree)right.getTree()).getValue() == -1) {
				AnnotationMirror anno = atypeFactory.createIndexForAnnotation(name);
				thenStore.insertValue(rec, anno);
			}
		}
		return newResult;
	}

	// find the left hand sides annotation then passes it to the right method to handle it
	@Override
	public TransferResult<CFValue, CFStore> visitLessThan(LessThanNode node, TransferInput<CFValue, CFStore> in) {
		TransferResult<CFValue, CFStore> result = super.visitLessThan(node, in);
		Node left = node.getLeftOperand();
		Node right = node.getRightOperand();
		Receiver rec = FlowExpressions.internalReprOf(analysis.getTypeFactory(), left);
		AnnotatedTypeMirror leftType = atypeFactory.getAnnotatedType(left.getTree());

		CFStore thenStore = result.getRegularStore();
		CFStore elseStore = thenStore.copy();
		ConditionalTransferResult<CFValue, CFStore> newResult =
				new ConditionalTransferResult<>(result.getResultValue(), thenStore, elseStore);
		if (leftType.hasAnnotation(IndexOrHigh.class) || leftType.hasAnnotation(NonNegative.class)) {
			IndexOrHighLessThan(rec, right, thenStore, false);
		} else if (leftType.hasAnnotation(Unknown.class)) {
			UnknownLessThan(rec, right, thenStore, false);
		}

		return newResult;
	}

	// find the left hand sides annotation then passes it to the right method to handle it
	@Override
	public TransferResult<CFValue, CFStore> visitLessThanOrEqual(LessThanOrEqualNode node, TransferInput<CFValue, CFStore> in) {
		TransferResult<CFValue, CFStore> result = super.visitLessThanOrEqual(node, in);
		Node left = node.getLeftOperand();
		Node right = node.getRightOperand();
		Receiver rec = FlowExpressions.internalReprOf(analysis.getTypeFactory(), left);
		AnnotatedTypeMirror leftType = atypeFactory.getAnnotatedType(left.getTree());

		CFStore thenStore = result.getRegularStore();
		CFStore elseStore = thenStore.copy();
		ConditionalTransferResult<CFValue, CFStore> newResult =
				new ConditionalTransferResult<>(result.getResultValue(), thenStore, elseStore);

		if (leftType.hasAnnotation(Unknown.class)) {
			UnknownLessThan(rec, right, thenStore, true);
		}
		if (leftType.hasAnnotation(IndexOrHigh.class) || leftType.hasAnnotation(NonNegative.class)) {
			IndexOrHighLessThan(rec, right, thenStore, true);
		}
		return newResult;
	}

	// find the left hand sides annotation then passes it to the right method to handle it
	// make IndexorLow(a) == indexOrHigh(a) -> IndexFor(a)
	@Override
	public TransferResult<CFValue, CFStore> visitEqualTo(EqualToNode node, TransferInput<CFValue, CFStore> in) {
		TransferResult<CFValue, CFStore> result = super.visitEqualTo(node, in);
		Node left = node.getLeftOperand();
		Node right = node.getRightOperand();
		Receiver leftRec = FlowExpressions.internalReprOf(analysis.getTypeFactory(), left);
		Receiver rightRec = FlowExpressions.internalReprOf(analysis.getTypeFactory(), right);
		AnnotatedTypeMirror leftType = atypeFactory.getAnnotatedType(left.getTree());
		AnnotatedTypeMirror rightType = atypeFactory.getAnnotatedType(right.getTree());
		CFStore thenStore = result.getRegularStore();
		CFStore elseStore = thenStore.copy();
		ConditionalTransferResult<CFValue, CFStore> newResult =
				new ConditionalTransferResult<>(result.getResultValue(), thenStore, elseStore);
		// do both directions because == is commutative
		for (AnnotationMirror anno: leftType.getAnnotations()) {
			thenStore.insertValue(rightRec, anno);
		}
		for (AnnotationMirror anno: rightType.getAnnotations()) {
			thenStore.insertValue(leftRec, anno);
		}
		if (leftType.hasAnnotation(IndexOrLow.class)) {
			IOLEqual(leftRec, rightRec, leftType, rightType, thenStore);
		}
		if (rightType.hasAnnotation(IndexOrLow.class)) {
			IOLEqual(leftRec, rightRec, rightType, leftType, thenStore);
		}
		return newResult;
	}

	//********************************************************************************//
	// these are methods for equalsTo Nodes once left hand annotation is known		//
	//********************************************************************************//
	private void IOLEqual(Receiver leftRec, Receiver rightRec, AnnotatedTypeMirror leftType,
			AnnotatedTypeMirror rightType, CFStore thenStore) {
		String leftName = getValue(leftType.getAnnotation(IndexOrLow.class));
		boolean InF = rightType.hasAnnotation(IndexFor.class);
		boolean IOH = rightType.hasAnnotation(IndexOrHigh.class);
		boolean NN = rightType.hasAnnotation(NonNegative.class);
		if (IOH || NN || InF) {
			// they are both a valid index for just IOL array
			thenStore.insertValue(leftRec, atypeFactory.createIndexForAnnotation(leftName));
			thenStore.insertValue(rightRec, atypeFactory.createIndexForAnnotation(leftName));
			if (InF) {
				// add to both left and right operands
				String rightName = getValue(rightType.getAnnotation(IndexFor.class));
				thenStore.insertValue(leftRec, atypeFactory.createIndexForAnnotation(rightName));
				thenStore.insertValue(rightRec, atypeFactory.createIndexForAnnotation(rightName));
			}
		}
	}

	//********************************************************************************//
	// these are methods for LessThan Nodes once left operand Annotation is known  //
	//********************************************************************************//
	// Unknown < IndexOrHigh(a), IndexFor(a), IndexOrLow(a), LTLength(a) -> LTLength(a)
	private void UnknownLessThan(Receiver rec, Node right, CFStore thenStore, boolean orEqual) {
		AnnotatedTypeMirror rightType = atypeFactory.getAnnotatedType(right.getTree());
		boolean IOH = rightType.hasAnnotation(IndexOrHigh.class);
		boolean InF = rightType.hasAnnotation(IndexFor.class);
		boolean IOL = rightType.hasAnnotation(IndexOrLow.class);
		boolean LTL = rightType.hasAnnotation(LTLength.class);
		if (orEqual) {
			IOH = false;
		}
		if (IOH || InF || IOL || LTL) {
			thenStore.clearValue(rec);
			String aValue = getValue(rightType.getAnnotationInHierarchy(atypeFactory.IndexFor));
			AnnotationMirror anno = atypeFactory.createLTLengthAnnotation(aValue);
			thenStore.insertValue(rec, anno);
		}	
	}
	// IndexOrHigh < IndexOrHigh(a), IndexFor(a), IndexOrLow(a), LTLength(a) ->IndexFor(a)
	private void IndexOrHighLessThan(Receiver rec, Node right, CFStore thenStore, boolean orEqual) {
		AnnotatedTypeMirror rightType = atypeFactory.getAnnotatedType(right.getTree());
		boolean IOH = rightType.hasAnnotation(IndexOrHigh.class);
		boolean InF = rightType.hasAnnotation(IndexFor.class);
		boolean IOL = rightType.hasAnnotation(IndexOrLow.class);
		boolean LTL = rightType.hasAnnotation(LTLength.class);
		if (orEqual) {
			IOH = false;
		}
		if (IOH || InF || IOL || LTL) {
			thenStore.clearValue(rec);
			String name = getValue(rightType.getAnnotationInHierarchy(atypeFactory.IndexOrHigh));
			thenStore.insertValue(rec, atypeFactory.createIndexForAnnotation(name));
		}

	}
//	//********************************************************************************//
//	// these are methods for LessThanOrEqual Nodes once left operand Annotation is known//
//	//********************************************************************************//
//	// Unknown <= IndexFor(a), IndexOrLow(a), LTLength(a) -> LTLength(a)
//	private void UnknownLessThanOrEqual(Receiver rec, Node right, CFStore thenStore) {
//		AnnotatedTypeMirror rightType = atypeFactory.getAnnotatedType(right.getTree());
//		boolean InF = rightType.hasAnnotation(IndexFor.class);
//		boolean IOL = rightType.hasAnnotation(IndexOrLow.class);
//		boolean LTL = rightType.hasAnnotation(LTLength.class);
//		if (InF || IOL || LTL) {
//			String aValue = getValue(rightType.getAnnotationInHierarchy(atypeFactory.IndexFor));
//			AnnotationMirror anno = atypeFactory.createLTLengthAnnotation(aValue);
//			thenStore.insertValue(rec, anno);
//		}	
//	}
//	// IndexOrHigh <= IndexFor(a), IndexOrLow(a), LTLength(a) ->IndexFor(a)
//	private void IndexOrHighLessThanOrEqual(Receiver rec, Node right, CFStore thenStore) {
//		AnnotatedTypeMirror rightType = atypeFactory.getAnnotatedType(right.getTree());
//		for (AnnotationMirror anno: rightType.getAnnotations()) {
//			boolean InF = AnnotationUtils.areSameIgnoringValues(anno, atypeFactory.IndexFor);
//			boolean IOL = AnnotationUtils.areSameIgnoringValues(anno, atypeFactory.IndexOrLow);
//			boolean LTL = AnnotationUtils.areSameIgnoringValues(anno, atypeFactory.LTLength);
//			if (InF || IOL || LTL) {
//				String name = getValue(anno);
//				thenStore.insertValue(rec, atypeFactory.createIndexForAnnotation(name));
//			}
//		}
//
//	}

	//********************************************************************************//
	// these are methods for GreaterThan Nodes once left operand Annotation is known  //
	//********************************************************************************//
	// this returns a transfer result for @Unknown > x
	//Unknown > IndexOrLow, NonNegative, IndexOrHigh, IndexFor -> NonNegative
	private void UnknownGreaterThan(Receiver rec, Node right, CFStore thenStore, boolean orEqual) {
		AnnotatedTypeMirror rightType = atypeFactory.getAnnotatedType(right.getTree());
		// booleans to see if the type is any in the heirarchy we want to refine
		boolean IOL = rightType.hasAnnotation(IndexOrLow.class);
		boolean NN = rightType.hasAnnotation(NonNegative.class);
		boolean IOH = rightType.hasAnnotation(IndexOrHigh.class);
		boolean IF = rightType.hasAnnotation(IndexFor.class);
		if (orEqual) {
			IOL = false;
		}
		if (IOL || NN || IOH || IF) {
			AnnotationMirror anno = atypeFactory.createNonNegAnnotation();
			thenStore.insertValue(rec, anno);
		}
	}
	
	//IndexOrLow(a) > IndexOrLow, Nonnegative, IndexOrHigh, IndexFor -> IndexFor(a)
	private void IndexOrLowGreaterThan(Receiver rec, Node right, CFStore thenStore, String name, boolean orEqual) {
		AnnotatedTypeMirror rightType = atypeFactory.getAnnotatedType(right.getTree());
		boolean IOL = rightType.hasAnnotation(IndexOrLow.class);
		boolean NN = rightType.hasAnnotation(NonNegative.class);
		boolean IOH = rightType.hasAnnotation(IndexOrHigh.class);
		boolean InF = rightType.hasAnnotation(IndexFor.class);
		if (orEqual) {
			IOL = false;
		}
		if (IOL || InF || NN || IOH) {
			thenStore.insertValue(rec, atypeFactory.createIndexForAnnotation(name));
		}
	}

	//*******************************************************************************************//
	// these are methods for GreaterThanOrEqual Nodes once left operand Annotation is known  //
	//***************************************************************************************//
	// this returns a transfer result for @Unknown >= x
	//Unknown >=, NonNegative, IndexOrHigh, IndexFor -> NonNegative
	private void UnknownGreaterThanOrEqual(Receiver rec, Node right, CFStore thenStore) {
		AnnotatedTypeMirror rightType = atypeFactory.getAnnotatedType(right.getTree());
		// booleans to see if the type is any in the heirarchy we want to refine
		boolean NN = rightType.hasAnnotation(NonNegative.class);
		boolean IOH = rightType.hasAnnotation(IndexOrHigh.class);
		boolean IF = rightType.hasAnnotation(IndexFor.class);
		if (NN || IOH || IF) {
			AnnotationMirror anno = atypeFactory.createNonNegAnnotation();
			thenStore.insertValue(rec, anno);
		}
	}
	//IndexOrLow(a) >= Nonnegative, IndexOrHigh, IndexFor -> IndexFor(a)
	private void IndexOrLowGreaterThanOrEqual(Receiver rec, Node right, CFStore thenStore, String name) {
		AnnotatedTypeMirror rightType = atypeFactory.getAnnotatedType(right.getTree());
		for (AnnotationMirror anno: rightType.getAnnotations()) {
			boolean InF = AnnotationUtils.areSameIgnoringValues(anno, atypeFactory.IndexFor);
			boolean NN = rightType.hasAnnotation(NonNegative.class);
			boolean IOH = rightType.hasAnnotation(IndexOrHigh.class);
			if (InF || NN || IOH) {
				thenStore.insertValue(rec, atypeFactory.createIndexForAnnotation(name));
			}
		}
	}

	// uses a helper method in the visitor and the factory to get the value of the annotation
	public static String getValue(AnnotationMirror anno) {
		return IndexVisitor.getIndexValue(anno, atypeFactory.getValueMethod(anno));
	}
}

