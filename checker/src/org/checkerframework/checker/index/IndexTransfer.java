package org.checkerframework.checker.index;

import javax.lang.model.element.AnnotationMirror;

import org.checkerframework.checker.index.qual.IndexFor;
import org.checkerframework.checker.index.qual.IndexOrHigh;
import org.checkerframework.checker.index.qual.IndexOrLow;
import org.checkerframework.checker.index.qual.LTLength;
import org.checkerframework.checker.index.qual.NonNegative;
import org.checkerframework.checker.index.qual.Unknown;
import org.checkerframework.dataflow.analysis.ConditionalTransferResult;
import org.checkerframework.dataflow.analysis.FlowExpressions;
import org.checkerframework.dataflow.analysis.FlowExpressions.Receiver;
import org.checkerframework.dataflow.analysis.RegularTransferResult;
import org.checkerframework.dataflow.analysis.TransferInput;
import org.checkerframework.dataflow.analysis.TransferResult;
import org.checkerframework.dataflow.cfg.node.EqualToNode;
import org.checkerframework.dataflow.cfg.node.FieldAccessNode;
import org.checkerframework.dataflow.cfg.node.GreaterThanNode;
import org.checkerframework.dataflow.cfg.node.GreaterThanOrEqualNode;
import org.checkerframework.dataflow.cfg.node.LessThanNode;
import org.checkerframework.dataflow.cfg.node.LessThanOrEqualNode;
import org.checkerframework.dataflow.cfg.node.Node;
import org.checkerframework.dataflow.cfg.node.NotEqualNode;
import org.checkerframework.framework.flow.CFAbstractTransfer;
import org.checkerframework.framework.type.AnnotatedTypeMirror;

import com.sun.source.tree.LiteralTree;
import com.sun.source.tree.Tree;


public class IndexTransfer extends CFAbstractTransfer<IndexValue, IndexStore, IndexTransfer> {
	protected IndexAnalysis analysis;
	protected static IndexAnnotatedTypeFactory atypeFactory;

	public IndexTransfer(IndexAnalysis analysis) {
		super(analysis);
		this.analysis = analysis;
		atypeFactory = (IndexAnnotatedTypeFactory) analysis.getTypeFactory();
	}

	// annotate arr.length to be IndexOrHigh("arr")
	@Override
	public TransferResult<IndexValue, IndexStore> visitFieldAccess(FieldAccessNode node, TransferInput<IndexValue, IndexStore> in) {
		TransferResult<IndexValue, IndexStore> result = super.visitFieldAccess(node, in);
		
		if (node.getFieldName().equals("length")) {
			String arrName = node.getReceiver().toString();
			if (arrName.contains(".")) {
				String[] objs = arrName.split("\\.");
				arrName = objs[objs.length -1];
			}
			AnnotationMirror anno = atypeFactory.createIndexOrHighAnnotation(arrName);
			IndexValue newResultValue = analysis.createSingleAnnotationValue(anno, result.getResultValue().getType().getUnderlyingType());
			return new RegularTransferResult<>(newResultValue, result.getRegularStore());
		}

		return result;
	}
	//TODO refactor methods to use helpers (like visitNotEqual) to avoid rewriting for left and right side refinements
	//******************************************************//
	// these are methods that handle refining on comparisons//
	//******************************************************//
	
	// find the left hand sides annotation then passes it to the right method to handle it
	@Override
	public TransferResult<IndexValue, IndexStore> visitGreaterThan(GreaterThanNode node, TransferInput<IndexValue, IndexStore> in) {
		TransferResult<IndexValue, IndexStore> result = super.visitGreaterThan(node, in);
		Node left = node.getLeftOperand();
		Node right = node.getRightOperand();
		Receiver rec = FlowExpressions.internalReprOf(analysis.getTypeFactory(), left);
		AnnotatedTypeMirror leftType = atypeFactory.getAnnotatedType(left.getTree());
		
		IndexStore thenStore = result.getRegularStore();
		IndexStore elseStore = thenStore.copy();
		ConditionalTransferResult<IndexValue, IndexStore> newResult =
				new ConditionalTransferResult<>(result.getResultValue(), thenStore, elseStore);
		// refine the left side
		if (leftType.hasAnnotation(Unknown.class)) {
			UnknownGreaterThan(rec, right, thenStore, false);
			UnknownLessThan(rec, right, elseStore, true);
		}
		if (leftType.hasAnnotation(IndexOrLow.class) || leftType.hasAnnotation(LTLength.class)) {
			AnnotationMirror leftAnno = leftType.getAnnotationInHierarchy(atypeFactory.IndexOrLow);
			String name = getValue(leftAnno);
			IndexOrLowGreaterThan(rec, right, thenStore, name, false);
		}
		if (leftType.hasAnnotation(IndexOrHigh.class) || leftType.hasAnnotation(NonNegative.class)) {
			IndexOrHighLessThan(rec, right, elseStore, true);
		}
		
		Receiver rightRec = FlowExpressions.internalReprOf(analysis.getTypeFactory(), right);
		AnnotatedTypeMirror rightType = atypeFactory.getAnnotatedType(right.getTree());
		// refine the right side
		// do same transfers a <= to right side of the >
		if (rightType.hasAnnotation(Unknown.class)) {
			UnknownLessThan(rightRec, left, thenStore, true);
			UnknownGreaterThan(rightRec, left, elseStore, false);
		}
		if (rightType.hasAnnotation(IndexOrHigh.class) || rightType.hasAnnotation(NonNegative.class)) {
			IndexOrHighLessThan(rightRec, left, thenStore, true);
		}
		if (rightType.hasAnnotation(IndexOrLow.class) || rightType.hasAnnotation(LTLength.class)) {
			AnnotationMirror rightAnno = rightType.getAnnotationInHierarchy(atypeFactory.IndexOrLow);
			String name = getValue(rightAnno);
			IndexOrLowGreaterThan(rightRec, left, elseStore, name, false);
		}
		return newResult;
	}

	// find the left hand sides annotation then passes it to the right method to handle it
	@Override
	public TransferResult<IndexValue, IndexStore> visitGreaterThanOrEqual(GreaterThanOrEqualNode node, TransferInput<IndexValue, IndexStore> in) {
		TransferResult<IndexValue, IndexStore> result = super.visitGreaterThanOrEqual(node, in);
		Node left = node.getLeftOperand();
		Node right = node.getRightOperand();
		Receiver rec = FlowExpressions.internalReprOf(analysis.getTypeFactory(), left);
		AnnotatedTypeMirror leftType = atypeFactory.getAnnotatedType(left.getTree());

		IndexStore thenStore = result.getRegularStore();
		IndexStore elseStore = thenStore.copy();
		ConditionalTransferResult<IndexValue, IndexStore> newResult =
				new ConditionalTransferResult<>(result.getResultValue(), thenStore, elseStore);

		if (leftType.hasAnnotation(Unknown.class)) {
			UnknownGreaterThan(rec, right, thenStore, true);
			UnknownLessThan(rec, right, elseStore, false);

		}
		if (leftType.hasAnnotation(IndexOrLow.class) || leftType.hasAnnotation(LTLength.class)) {
			AnnotationMirror leftAnno = leftType.getAnnotationInHierarchy(atypeFactory.IndexOrLow);
			String name = getValue(leftAnno);
			IndexOrLowGreaterThan(rec, right, thenStore, name, true);
		}
		if (leftType.hasAnnotation(IndexOrHigh.class) || leftType.hasAnnotation(NonNegative.class)) {
			IndexOrHighLessThan(rec, right, elseStore, false);
		}
		
		// refine right side
		Receiver rightRec = FlowExpressions.internalReprOf(analysis.getTypeFactory(), right);
		AnnotatedTypeMirror rightType = atypeFactory.getAnnotatedType(right.getTree());
		if (rightType.hasAnnotation(IndexOrHigh.class) || rightType.hasAnnotation(NonNegative.class)) {
			IndexOrHighLessThan(rightRec, left, thenStore, false);
		}
		if (rightType.hasAnnotation(Unknown.class)) {
			UnknownLessThan(rightRec, left, thenStore, false);
			UnknownGreaterThan(rightRec, left, elseStore, true);
		}
		if (rightType.hasAnnotation(IndexOrLow.class) || rightType.hasAnnotation(LTLength.class)) {
			AnnotationMirror rightAnno = rightType.getAnnotationInHierarchy(atypeFactory.IndexOrLow);
			String name = getValue(rightAnno);
			IndexOrLowGreaterThan(rightRec, left, elseStore, name, true);
		}
		return newResult;
	}

	@Override
	public TransferResult<IndexValue, IndexStore> visitNotEqual(NotEqualNode node, TransferInput<IndexValue, IndexStore> in) {
		TransferResult<IndexValue, IndexStore> result = super.visitNotEqual(node, in);
		Node left = node.getLeftOperand();
		Node right = node.getRightOperand();
		Receiver leftRec = FlowExpressions.internalReprOf(analysis.getTypeFactory(), left);
		Receiver rightRec = FlowExpressions.internalReprOf(analysis.getTypeFactory(), right);

		AnnotatedTypeMirror leftType = atypeFactory.getAnnotatedType(left.getTree());
		AnnotatedTypeMirror rightType = atypeFactory.getAnnotatedType(right.getTree());
		IndexStore thenStore = result.getRegularStore();
		IndexStore elseStore = thenStore.copy();
		ConditionalTransferResult<IndexValue, IndexStore> newResult =
				new ConditionalTransferResult<>(result.getResultValue(), thenStore, elseStore);
		// refine the left side with the helper
		NotEqualHelper(leftType, rightType, left,  right, leftRec, rightRec, thenStore, elseStore);
		// refine right side using swapped params
		NotEqualHelper(rightType, leftType, right, left, rightRec, leftRec, thenStore, elseStore);
		return newResult;
	}
	
	// does the transfers for NotEqual, given the left and right typeMirrors the receivers for them and the Stores
	// factored out of the old method so that we can do left side and right side separatly
	public void NotEqualHelper(AnnotatedTypeMirror leftType, AnnotatedTypeMirror rightType, Node left, Node right,
			Receiver leftRec, Receiver rightRec, IndexStore thenStore, IndexStore elseStore) {
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
					thenStore.insertValue(leftRec, anno);
				}
			}

		}
		// have to check exactly -1 because indexorlow could be different
		else if (leftType.hasAnnotation(IndexOrLow.class)) {
			AnnotationMirror leftAnno = leftType.getAnnotation(IndexOrLow.class);
			String name = getValue(leftAnno);
			if (right.getTree().getKind().equals(Tree.Kind.INT_LITERAL) && (int)((LiteralTree)right.getTree()).getValue() == -1) {
				AnnotationMirror anno = atypeFactory.createIndexForAnnotation(name);
				thenStore.insertValue(leftRec, anno);
			}
		}
		// so the elseStore using ==
		if (leftType.hasAnnotation(IndexOrLow.class) || leftType.hasAnnotation(LTLength.class)) {
			IOLEqual(leftRec, rightRec, leftType, rightType, elseStore);
		}
		if (leftType.hasAnnotation(IndexOrHigh.class) || leftType.hasAnnotation(NonNegative.class)) {
			NonNegEqual(leftRec, rightType, elseStore);
		}
	}

	// find the left hand sides annotation then passes it to the right method to handle it
	@Override
	public TransferResult<IndexValue, IndexStore> visitLessThan(LessThanNode node, TransferInput<IndexValue, IndexStore> in) {
		TransferResult<IndexValue, IndexStore> result = super.visitLessThan(node, in);
		Node left = node.getLeftOperand();
		Node right = node.getRightOperand();
		Receiver rec = FlowExpressions.internalReprOf(analysis.getTypeFactory(), left);
		AnnotatedTypeMirror leftType = atypeFactory.getAnnotatedType(left.getTree());

		IndexStore thenStore = result.getRegularStore();
		IndexStore elseStore = thenStore.copy();
		ConditionalTransferResult<IndexValue, IndexStore> newResult =
				new ConditionalTransferResult<>(result.getResultValue(), thenStore, elseStore);
		
		if (leftType.hasAnnotation(IndexOrHigh.class) || leftType.hasAnnotation(NonNegative.class)) {
			IndexOrHighLessThan(rec, right, thenStore, false);
		}
		if (leftType.hasAnnotation(Unknown.class)) {
			UnknownLessThan(rec, right, thenStore, false);
			UnknownGreaterThan(rec, right, elseStore, true);
		}
		if (leftType.hasAnnotation(IndexOrLow.class) || leftType.hasAnnotation(LTLength.class)) {
			AnnotationMirror leftAnno = leftType.getAnnotationInHierarchy(atypeFactory.IndexOrLow);
			String name = getValue(leftAnno);
			IndexOrLowGreaterThan(rec, right, elseStore, name, true);
		}
		return newResult;
	}

	// find the left hand sides annotation then passes it to the right method to handle it
	@Override
	public TransferResult<IndexValue, IndexStore> visitLessThanOrEqual(LessThanOrEqualNode node, TransferInput<IndexValue, IndexStore> in) {
		TransferResult<IndexValue, IndexStore> result = super.visitLessThanOrEqual(node, in);
		Node left = node.getLeftOperand();
		Node right = node.getRightOperand();
		Receiver rec = FlowExpressions.internalReprOf(analysis.getTypeFactory(), left);
		AnnotatedTypeMirror leftType = atypeFactory.getAnnotatedType(left.getTree());

		IndexStore thenStore = result.getRegularStore();
		IndexStore elseStore = thenStore.copy();
		ConditionalTransferResult<IndexValue, IndexStore> newResult =
				new ConditionalTransferResult<>(result.getResultValue(), thenStore, elseStore);

		if (leftType.hasAnnotation(Unknown.class)) {
			UnknownLessThan(rec, right, thenStore, true);
			UnknownGreaterThan(rec, right, elseStore, false);
		}
		if (leftType.hasAnnotation(IndexOrHigh.class) || leftType.hasAnnotation(NonNegative.class)) {
			IndexOrHighLessThan(rec, right, thenStore, true);
		}
		if (leftType.hasAnnotation(IndexOrLow.class) || leftType.hasAnnotation(LTLength.class)) {
			AnnotationMirror leftAnno = leftType.getAnnotationInHierarchy(atypeFactory.IndexOrLow);
			String name = getValue(leftAnno);
			IndexOrLowGreaterThan(rec, right, elseStore, name, false);
		}
		return newResult;
	}

	// find the left hand sides annotation then passes it to the right method to handle it
	// make IndexorLow(a) == indexOrHigh(a) -> IndexFor(a)
	@Override
	public TransferResult<IndexValue, IndexStore> visitEqualTo(EqualToNode node, TransferInput<IndexValue, IndexStore> in) {
		TransferResult<IndexValue, IndexStore> result = super.visitEqualTo(node, in);
		Node left = node.getLeftOperand();
		Node right = node.getRightOperand();
		Receiver leftRec = FlowExpressions.internalReprOf(analysis.getTypeFactory(), left);
		Receiver rightRec = FlowExpressions.internalReprOf(analysis.getTypeFactory(), right);
		AnnotatedTypeMirror leftType = atypeFactory.getAnnotatedType(left.getTree());
		AnnotatedTypeMirror rightType = atypeFactory.getAnnotatedType(right.getTree());
		IndexStore thenStore = result.getRegularStore();
		IndexStore elseStore = thenStore.copy();
		ConditionalTransferResult<IndexValue, IndexStore> newResult =
				new ConditionalTransferResult<>(result.getResultValue(), thenStore, elseStore);
		// do both directions because == is commutative
		if (leftType.hasAnnotation(IndexOrLow.class) || leftType.hasAnnotation(LTLength.class) || leftType.hasAnnotation(IndexFor.class)) {
			IOLEqual(leftRec, rightRec, leftType, rightType, thenStore);
		}
		if (leftType.hasAnnotation(IndexOrHigh.class) || leftType.hasAnnotation(NonNegative.class)) {
			NonNegEqual(leftRec, rightType, thenStore);
		}
		
		// do the else store (same as notEqual)
		
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
					elseStore.insertValue(leftRec, anno);
				}
			}

		}
		// have to check exactly -1 because indexorlow could be different
		else if (leftType.hasAnnotation(IndexOrLow.class)) {
			AnnotationMirror leftAnno = leftType.getAnnotation(IndexOrLow.class);
			String name = getValue(leftAnno);
			if (right.getTree().getKind().equals(Tree.Kind.INT_LITERAL) && (int)((LiteralTree)right.getTree()).getValue() == -1) {
				AnnotationMirror anno = atypeFactory.createIndexForAnnotation(name);
				elseStore.insertValue(leftRec, anno);
			}
		}
		
		
		return newResult;
	}


	//********************************************************************************//
	// these are methods for equalsTo Nodes once left hand annotation is known		//
	//********************************************************************************//
	private void IOLEqual(Receiver leftRec, Receiver rightRec, AnnotatedTypeMirror leftType,
			AnnotatedTypeMirror rightType, IndexStore thenStore) {
		String leftName = getValue(leftType.getAnnotationInHierarchy(atypeFactory.IndexFor));
		boolean InF = rightType.hasAnnotation(IndexFor.class);
		boolean IOH = rightType.hasAnnotation(IndexOrHigh.class);
		boolean NN = rightType.hasAnnotation(NonNegative.class);
		if (IOH || NN || InF) {
			// they are both a valid index for just IOL array
			thenStore.insertValue(leftRec, atypeFactory.createIndexForAnnotation(leftName));
			//thenStore.insertValue(rightRec, atypeFactory.createIndexForAnnotation(leftName));
			if (InF) {
				// add to both left and right operands
				String rightName = getValue(rightType.getAnnotation(IndexFor.class));
				thenStore.insertValue(leftRec, atypeFactory.createIndexForAnnotation(rightName));
				//thenStore.insertValue(rightRec, atypeFactory.createIndexForAnnotation(rightName));
			}
		}
		if (rightType.hasAnnotation(IndexOrLow.class)) {
			thenStore.insertValue(leftRec, atypeFactory.createIndexOrLowAnnotation(leftName));
			//thenStore.insertValue(rightRec, atypeFactory.createIndexOrLowAnnotation(leftName));
		}
	}
	
	private void NonNegEqual(Receiver leftRec, AnnotatedTypeMirror rightType, IndexStore thenStore) {
		boolean InF = rightType.hasAnnotation(IndexFor.class);
		boolean IOL = rightType.hasAnnotation(IndexOrLow.class);
		boolean LTL = rightType.hasAnnotation(LTLength.class);
		if (InF || IOL || LTL) {
			String name = getValue(rightType.getAnnotationInHierarchy(atypeFactory.IndexFor));
			thenStore.insertValue(leftRec, atypeFactory.createIndexForAnnotation(name));
		}
		// if right is indexOrHigh left should be too
		if (rightType.hasAnnotation(IndexOrHigh.class)) {
			String name = getValue(rightType.getAnnotationInHierarchy(atypeFactory.IndexFor));
			thenStore.insertValue(leftRec, atypeFactory.createIndexOrHighAnnotation(name));
		}
	}

	//********************************************************************************//
	// these are methods for LessThan Nodes once left operand Annotation is known  //
	//********************************************************************************//
	// Unknown < IndexOrHigh(a), IndexFor(a), IndexOrLow(a), LTLength(a) -> LTLength(a)
	private void UnknownLessThan(Receiver rec, Node right, IndexStore thenStore, boolean orEqual) {
		AnnotatedTypeMirror rightType = atypeFactory.getAnnotatedType(right.getTree());
		boolean IOH = rightType.hasAnnotation(IndexOrHigh.class) && !orEqual;
		boolean InF = rightType.hasAnnotation(IndexFor.class);
		boolean IOL = rightType.hasAnnotation(IndexOrLow.class);
		boolean LTL = rightType.hasAnnotation(LTLength.class);
		if (IOH || InF || IOL || LTL) {
			thenStore.clearValue(rec);
			String aValue = getValue(rightType.getAnnotationInHierarchy(atypeFactory.IndexFor));
			AnnotationMirror anno = atypeFactory.createLTLengthAnnotation(aValue);
			thenStore.insertValue(rec, anno);
		}	
	}
	// IndexOrHigh < IndexOrHigh(a), IndexFor(a), IndexOrLow(a), LTLength(a) ->IndexFor(a)
	private void IndexOrHighLessThan(Receiver rec, Node right, IndexStore thenStore, boolean orEqual) {
		AnnotatedTypeMirror rightType = atypeFactory.getAnnotatedType(right.getTree());
		boolean IOH = rightType.hasAnnotation(IndexOrHigh.class) && !orEqual;
		boolean InF = rightType.hasAnnotation(IndexFor.class);
		boolean IOL = rightType.hasAnnotation(IndexOrLow.class);
		boolean LTL = rightType.hasAnnotation(LTLength.class);
		if (IOH || InF || IOL || LTL) {
			thenStore.clearValue(rec);
			String name = getValue(rightType.getAnnotationInHierarchy(atypeFactory.IndexOrHigh));
			thenStore.insertValue(rec, atypeFactory.createIndexForAnnotation(name));
		}
		// if left wasnt an indexOrhigh it should be now
		if (rightType.hasAnnotation(IndexOrHigh.class) && orEqual) {
			String name = getValue(rightType.getAnnotationInHierarchy(atypeFactory.IndexOrHigh));
			thenStore.insertValue(rec, atypeFactory.createIndexOrHighAnnotation(name));
		}

	}

	//********************************************************************************//
	// these are methods for GreaterThan Nodes once left operand Annotation is known  //
	//********************************************************************************//
	// this returns a transfer result for @Unknown > x
	//Unknown > IndexOrLow, NonNegative, IndexOrHigh, IndexFor -> NonNegative
	private void UnknownGreaterThan(Receiver rec, Node right, IndexStore thenStore, boolean orEqual) {
		AnnotatedTypeMirror rightType = atypeFactory.getAnnotatedType(right.getTree());
		// booleans to see if the type is any in the heirarchy we want to refine
		boolean IOL = (rightType.hasAnnotation(IndexOrLow.class) || isNegOne(right)) && !orEqual;
		boolean NN = rightType.hasAnnotation(NonNegative.class);
		boolean IOH = rightType.hasAnnotation(IndexOrHigh.class);
		boolean IF = rightType.hasAnnotation(IndexFor.class);

		if (IOL || NN || IOH || IF) {
			AnnotationMirror anno = atypeFactory.createNonNegAnnotation();
			thenStore.insertValue(rec, anno);
		}
		
	}
	
	private boolean isNegOne(Node right) {
		if (right.getTree().getKind().equals(Tree.Kind.INT_LITERAL)) {
			int val = (int)((LiteralTree)right.getTree()).getValue();
			return val == -1;
		}
		return false;
	}

	//IndexOrLow(a) > IndexOrLow, Nonnegative, IndexOrHigh, IndexFor -> IndexFor(a)
	private void IndexOrLowGreaterThan(Receiver rec, Node right, IndexStore thenStore, String name, boolean orEqual) {
		AnnotatedTypeMirror rightType = atypeFactory.getAnnotatedType(right.getTree());
		boolean IOL = (rightType.hasAnnotation(IndexOrLow.class) || isNegOne(right)) && !orEqual;
		boolean NN = rightType.hasAnnotation(NonNegative.class);
		boolean IOH = rightType.hasAnnotation(IndexOrHigh.class);
		boolean InF = rightType.hasAnnotation(IndexFor.class);
		if (IOL || InF || NN || IOH) {
			thenStore.insertValue(rec, atypeFactory.createIndexForAnnotation(name));
		}
		else if ((rightType.hasAnnotation(IndexOrLow.class) || isNegOne(right))  && orEqual) {
			thenStore.insertValue(rec, atypeFactory.createIndexOrLowAnnotation(name));
		}
	}

	// uses a helper method in the visitor and the factory to get the value of the annotation
	public static String getValue(AnnotationMirror anno) {
		return IndexVisitor.getIndexValue(anno, atypeFactory.getValueMethod(anno));
	}
}

