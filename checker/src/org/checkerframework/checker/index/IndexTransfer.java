package org.checkerframework.checker.index;

import java.util.List;

import javax.lang.model.element.AnnotationMirror;

import org.checkerframework.checker.index.qual.IndexFor;
import org.checkerframework.checker.index.qual.IndexOrHigh;
import org.checkerframework.checker.index.qual.IndexOrLow;
import org.checkerframework.checker.index.qual.LTLength;
import org.checkerframework.checker.index.qual.MinLen;
import org.checkerframework.checker.index.qual.NonNegative;
import org.checkerframework.checker.index.qual.Unknown;
import org.checkerframework.dataflow.analysis.ConditionalTransferResult;
import org.checkerframework.dataflow.analysis.FlowExpressions;
import org.checkerframework.dataflow.analysis.FlowExpressions.Receiver;
import org.checkerframework.dataflow.analysis.RegularTransferResult;
import org.checkerframework.dataflow.analysis.TransferInput;
import org.checkerframework.dataflow.analysis.TransferResult;
import org.checkerframework.dataflow.cfg.node.ArrayCreationNode;
import org.checkerframework.dataflow.cfg.node.AssignmentNode;
import org.checkerframework.dataflow.cfg.node.EqualToNode;
import org.checkerframework.dataflow.cfg.node.FieldAccessNode;
import org.checkerframework.dataflow.cfg.node.GreaterThanNode;
import org.checkerframework.dataflow.cfg.node.GreaterThanOrEqualNode;
import org.checkerframework.dataflow.cfg.node.LessThanNode;
import org.checkerframework.dataflow.cfg.node.LessThanOrEqualNode;
import org.checkerframework.dataflow.cfg.node.Node;
import org.checkerframework.dataflow.cfg.node.NotEqualNode;
import org.checkerframework.dataflow.cfg.node.NumericalAdditionNode;
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
	
	// this transfer makes a variable used in the creation of an array as its length
	// become an IndexOrHigh for the array it intialized
	@Override
	public TransferResult<IndexValue, IndexStore> visitAssignment(AssignmentNode node, TransferInput<IndexValue, IndexStore> in) {
		TransferResult<IndexValue, IndexStore> result = super.visitAssignment(node, in);
		if (node.getExpression() instanceof ArrayCreationNode) {
			ArrayCreationNode ACNode = (ArrayCreationNode)node.getExpression();
			IndexStore store = result.getRegularStore();
			List<Node> nodeList = ACNode.getDimensions();
			// dont know if returns empty list or null if no dimension
			if (nodeList == null || nodeList.size() < 1) {
				return result;
			}
			Node dim = ACNode.getDimension(0);
			Receiver rec = FlowExpressions.internalReprOf(analysis.getTypeFactory(), dim);
			String name = node.getTarget().toString();
			if (name.contains(".")) {
				String[] objs = name.split("\\.");
				name = objs[objs.length -1];
			}
			if (dim instanceof NumericalAdditionNode) {
				if (isVarPlusOne((NumericalAdditionNode)dim, store, name)) {
					return result;
				}
			}
			store.insertValue(rec, IndexAnnotatedTypeFactory.createIndexOrHighAnnotation(name));
		}
		return result;
	}
	
	/**
	 * if the dimension of an array a is a var + 1
	 * @param dim the dimension Node
	 * @param store the store to put the new type in
	 * @param name the name of the connected array
	 */
	private boolean isVarPlusOne(NumericalAdditionNode dim, IndexStore store, String name) {
		if (IndexUtils.isGTZero(dim.getRightOperand())) {
			Receiver rec = FlowExpressions.internalReprOf(analysis.getTypeFactory(), dim.getLeftOperand());
			store.insertValue(rec, IndexAnnotatedTypeFactory.createIndexForAnnotation(name));
			return true;
		}
		if (IndexUtils.isGTZero(dim.getLeftOperand())) {
			Receiver rec = FlowExpressions.internalReprOf(analysis.getTypeFactory(), dim.getRightOperand());
			store.insertValue(rec, IndexAnnotatedTypeFactory.createIndexForAnnotation(name));
			return true;
		}
		return false;
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
			AnnotationMirror anno = IndexAnnotatedTypeFactory.createIndexOrHighAnnotation(arrName);
			IndexValue newResultValue = analysis.createSingleAnnotationValue(anno, result.getResultValue().getType().getUnderlyingType());
			return new RegularTransferResult<>(newResultValue, result.getRegularStore());
		}

		return result;
	}
	
	//******************************************************//
	// these are methods that handle refining on comparisons//
	//******************************************************//
	
	//********************************************************************************//
	// these are methods for GreaterThan Nodes   //
	//********************************************************************************//
	// find the left hand sides annotation then passes it to the right method to handle it
	@Override
	public TransferResult<IndexValue, IndexStore> visitGreaterThan(GreaterThanNode node, TransferInput<IndexValue, IndexStore> in) {
		TransferResult<IndexValue, IndexStore> result = super.visitGreaterThan(node, in);
		Node left = node.getLeftOperand();
		Node right = node.getRightOperand();
		IndexStore thenStore = result.getRegularStore();
		IndexStore elseStore = thenStore.copy();
		ConditionalTransferResult<IndexValue, IndexStore> newResult =
				new ConditionalTransferResult<>(result.getResultValue(), thenStore, elseStore);
		// refine the left side
		greaterThanHelper(left, right, thenStore);
		// refine the right side
		// do same transfers a <= to right side of the >
		lessThanOrEqualHelper(right, left, thenStore);
		// refine else branch
		lessThanOrEqualHelper(left, right, elseStore);
		// refine right side else branch
		greaterThanHelper(right, left, elseStore);
		return newResult;
	}
	/**
	 *
	 * @param left
	 * 			the node that is the left side of the comparison
	 * @param right
	 * 			the right side node
	 * @param thenStore
	 * 			the store that the refinement is placed in
	 *
	 * refines the types of the left side node and puts result into passed store
	 */
	private void greaterThanHelper(Node left, Node right, IndexStore thenStore) {
		Receiver leftRec = FlowExpressions.internalReprOf(analysis.getTypeFactory(), left);
		AnnotatedTypeMirror leftType = atypeFactory.getAnnotatedType(left.getTree());
		if (left instanceof FieldAccessNode) {
			FieldAccessNode FANode = (FieldAccessNode) left;
			if (FANode.getFieldName().equals("length")) {
				Receiver rec = FlowExpressions.internalReprOf(analysis.getTypeFactory(), FANode.getReceiver());
				if (right.getTree().getKind().equals(Tree.Kind.INT_LITERAL)) {
					int val = (int)((LiteralTree)right.getTree()).getValue();
					// if it already has a minlen use the higher of the two
					if (atypeFactory.getAnnotatedType(FANode.getReceiver().getTree()).hasAnnotation(MinLen.class)) {
						val = Math.max(val, IndexUtils.getMinLen(atypeFactory.getAnnotatedType(FANode.getReceiver().getTree()).getAnnotation(MinLen.class)));
					}
					thenStore.insertValue(rec, IndexAnnotatedTypeFactory.createMinLen(val + 1));
				}
			}
		}
		if (leftType.hasAnnotation(Unknown.class)) {
			UnknownGreaterThan(leftRec, right, thenStore, false);
		}
		if (leftType.hasAnnotation(IndexOrLow.class) || leftType.hasAnnotation(LTLength.class)) {
			AnnotationMirror leftAnno = leftType.getAnnotationInHierarchy(IndexAnnotatedTypeFactory.indexOrLow);
			String name = IndexUtils.getValue(leftAnno);
			IndexOrLowGreaterThan(leftRec, right, thenStore, name, false);
		}
	}

	//********************************************************************************//
	// these are methods for GreaterThanOrEqual Nodes   //
	//********************************************************************************//
	// find the left hand sides annotation then passes it to the right method to handle it
	@Override
	public TransferResult<IndexValue, IndexStore> visitGreaterThanOrEqual(GreaterThanOrEqualNode node, TransferInput<IndexValue, IndexStore> in) {
		TransferResult<IndexValue, IndexStore> result = super.visitGreaterThanOrEqual(node, in);
		Node left = node.getLeftOperand();
		Node right = node.getRightOperand();
		IndexStore thenStore = result.getRegularStore();
		IndexStore elseStore = thenStore.copy();
		ConditionalTransferResult<IndexValue, IndexStore> newResult =
				new ConditionalTransferResult<>(result.getResultValue(), thenStore, elseStore);
		// refine the left side
		greaterThanOrEqualHelper(left, right, thenStore);
		// refine right side
		lessThanHelper(right, left, thenStore);
		// refine else branch
		lessThanHelper(left, right, elseStore);
		//refine right side else branch
		greaterThanOrEqualHelper(right, left, elseStore);
		return newResult;
	}
	
	/**
	 *
	 * @param left
	 * 			the node that is the left side of the comparison
	 * @param right
	 * 			the right side node
	 * @param thenStore
	 * 			the store that the refinement is placed in
	 *
	 * refines the types of the left side node and puts result into passed store
	 */
	private void greaterThanOrEqualHelper(Node left, Node right, IndexStore thenStore) {
		Receiver leftRec = FlowExpressions.internalReprOf(analysis.getTypeFactory(), left);
		AnnotatedTypeMirror leftType = atypeFactory.getAnnotatedType(left.getTree());
		if (left instanceof FieldAccessNode) {
			FieldAccessNode FANode = (FieldAccessNode) left;
			if (FANode.getFieldName().equals("length")) {
				Receiver rec = FlowExpressions.internalReprOf(analysis.getTypeFactory(), FANode.getReceiver());
				if (right.getTree().getKind().equals(Tree.Kind.INT_LITERAL)) {
					int val = (int)((LiteralTree)right.getTree()).getValue();
					// if it already has a minlen use the higher of the two
					if (atypeFactory.getAnnotatedType(FANode.getReceiver().getTree()).hasAnnotation(MinLen.class)) {
						val = Math.max(val, IndexUtils.getMinLen(atypeFactory.getAnnotatedType(FANode.getReceiver().getTree()).getAnnotation(MinLen.class)));
					}
					thenStore.insertValue(rec, IndexAnnotatedTypeFactory.createMinLen(val));
				}
			}
		}
		if (leftType.hasAnnotation(Unknown.class)) {
			UnknownGreaterThan(leftRec, right, thenStore, true);
		}
		if (leftType.hasAnnotation(IndexOrLow.class) || leftType.hasAnnotation(LTLength.class)) {
			AnnotationMirror leftAnno = leftType.getAnnotationInHierarchy(IndexAnnotatedTypeFactory.indexOrLow);
			String name = IndexUtils.getValue(leftAnno);
			IndexOrLowGreaterThan(leftRec, right, thenStore, name, true);
		}

	}

	//********************************************************************************//
	// these are methods for NotEqual Nodes   //
	//********************************************************************************//
	@Override
	public TransferResult<IndexValue, IndexStore> visitNotEqual(NotEqualNode node, TransferInput<IndexValue, IndexStore> in) {
		TransferResult<IndexValue, IndexStore> result = super.visitNotEqual(node, in);
		Node left = node.getLeftOperand();
		Node right = node.getRightOperand();
		IndexStore thenStore = result.getRegularStore();
		IndexStore elseStore = thenStore.copy();
		ConditionalTransferResult<IndexValue, IndexStore> newResult =
				new ConditionalTransferResult<>(result.getResultValue(), thenStore, elseStore);
		// refine the left side with the helper
		NotEqualHelper(left,  right, thenStore);
		// refine right side using swapped params
		NotEqualHelper(right, left, thenStore);
		// refine else
		equalsToHelper(left, right, elseStore);
		// refine right side else
		equalsToHelper(right, left, elseStore);
		return newResult;
	}
	
	/**
	 *
	 * @param left
	 * 			the node that is the left side of the comparison
	 * @param right
	 * 			the right side node
	 * @param thenStore
	 * 			the store that the refinement is placed in
	 *
	 * refines the types of the left side node and puts result into passed store
	 */
	public void NotEqualHelper(Node left, Node right,IndexStore thenStore) {
		Receiver leftRec = FlowExpressions.internalReprOf(analysis.getTypeFactory(), left);
		AnnotatedTypeMirror leftType = atypeFactory.getAnnotatedType(left.getTree());
		if (left instanceof FieldAccessNode) {
			FieldAccessNode FANode = (FieldAccessNode) left;
			if (FANode.getFieldName().equals("length")) {
				Receiver rec = FlowExpressions.internalReprOf(analysis.getTypeFactory(), FANode.getReceiver());
				if (right.getTree().getKind().equals(Tree.Kind.INT_LITERAL)) {
					int val = (int)((LiteralTree)right.getTree()).getValue();
					if (val == 0) {
						thenStore.insertValue(rec, IndexAnnotatedTypeFactory.createMinLen(1));
					}
				}
			}
		}

		if (leftType.hasAnnotation(IndexOrHigh.class)) {
			if (right instanceof FieldAccessNode) {
				FieldAccessNode FANode = (FieldAccessNode) right;
				if (FANode.getFieldName().equals("length")) {
					String arrName = FANode.getReceiver().toString();
					if (arrName.contains(".")) {
						String[] objs = arrName.split("\\.");
						arrName = objs[objs.length -1];
					}
					AnnotationMirror anno = IndexAnnotatedTypeFactory.createIndexForAnnotation(arrName);
					thenStore.insertValue(leftRec, anno);
				}
			}

		}
		// have to check exactly -1 because indexorlow could be different
		else if (leftType.hasAnnotation(IndexOrLow.class)) {
			AnnotationMirror leftAnno = leftType.getAnnotation(IndexOrLow.class);
			String name = IndexUtils.getValue(leftAnno);
			if (IndexUtils.isNegOne(right)) {
				AnnotationMirror anno = IndexAnnotatedTypeFactory.createIndexForAnnotation(name);
				thenStore.insertValue(leftRec, anno);
			}
		}
	}

	//********************************************************************************//
	// these are methods for LessThan Nodes   //
	//********************************************************************************//
	// find the left hand sides annotation then passes it to the right method to handle it
	@Override
	public TransferResult<IndexValue, IndexStore> visitLessThan(LessThanNode node, TransferInput<IndexValue, IndexStore> in) {
		TransferResult<IndexValue, IndexStore> result = super.visitLessThan(node, in);
		Node left = node.getLeftOperand();
		Node right = node.getRightOperand();
		IndexStore thenStore = result.getRegularStore();
		IndexStore elseStore = thenStore.copy();
		ConditionalTransferResult<IndexValue, IndexStore> newResult =
				new ConditionalTransferResult<>(result.getResultValue(), thenStore, elseStore);
		// refine the left side
		lessThanHelper(left, right, thenStore);
		// refine right side
		greaterThanOrEqualHelper(right, left, thenStore);
		// refine else
		greaterThanOrEqualHelper(left, right, elseStore);
		// refine right side else
		lessThanHelper(right, left, elseStore);
		return newResult;
	}

	/**
	 *
	 * @param left
	 * 			the node that is the left side of the comparison
	 * @param right
	 * 			the right side node
	 * @param thenStore
	 * 			the store that the refinement is placed in
	 *
	 * refines the types of the left side node and puts result into passed store
	 */
	private void lessThanHelper(Node left, Node right, IndexStore thenStore) {
		Receiver rec = FlowExpressions.internalReprOf(analysis.getTypeFactory(), left);
		AnnotatedTypeMirror leftType = atypeFactory.getAnnotatedType(left.getTree());
		if (leftType.hasAnnotation(IndexOrHigh.class) || leftType.hasAnnotation(NonNegative.class)) {
			IndexOrHighLessThan(rec, right, thenStore, false);
		}
		if (leftType.hasAnnotation(Unknown.class)) {
			UnknownLessThan(rec, right, thenStore, false);
		}
	}

	//********************************************************************************//
	// these are methods for LessThanOrEqual Nodes   //
	//********************************************************************************//
	// find the left hand sides annotation then passes it to the right method to handle it
	@Override
	public TransferResult<IndexValue, IndexStore> visitLessThanOrEqual(LessThanOrEqualNode node, TransferInput<IndexValue, IndexStore> in) {
		TransferResult<IndexValue, IndexStore> result = super.visitLessThanOrEqual(node, in);
		Node left = node.getLeftOperand();
		Node right = node.getRightOperand();
		IndexStore thenStore = result.getRegularStore();
		IndexStore elseStore = thenStore.copy();
		ConditionalTransferResult<IndexValue, IndexStore> newResult =
				new ConditionalTransferResult<>(result.getResultValue(), thenStore, elseStore);
		// refine the left side
		lessThanOrEqualHelper(left, right, thenStore);
		// refine right side
		greaterThanHelper(right, left, thenStore);
		// refine else store
		greaterThanHelper(left, right, elseStore);
		// refine right side else
		lessThanOrEqualHelper(right, left, elseStore);
		return newResult;
	}

	/**
	 *
	 * @param left
	 * 			the node that is the left side of the comparison
	 * @param right
	 * 			the right side node
	 * @param thenStore
	 * 			the store that the refinement is placed in
	 *
	 * refines the types of the left side node and puts result into passed store
	 */
	private void lessThanOrEqualHelper(Node left, Node right, IndexStore thenStore) {
		Receiver rec = FlowExpressions.internalReprOf(analysis.getTypeFactory(), left);
		AnnotatedTypeMirror leftType = atypeFactory.getAnnotatedType(left.getTree());
		if (leftType.hasAnnotation(Unknown.class)) {
			UnknownLessThan(rec, right, thenStore, true);
		}
		if (leftType.hasAnnotation(IndexOrHigh.class) || leftType.hasAnnotation(NonNegative.class)) {
			IndexOrHighLessThan(rec, right, thenStore, true);
		}

	}
	
	//********************************************************************************//
	// these are methods for EqualsTo Nodes   //
	//********************************************************************************//
	// find the left hand sides annotation then passes it to the right method to handle it
	// make IndexorLow(a) == indexOrHigh(a) -> IndexFor(a)
	@Override
	public TransferResult<IndexValue, IndexStore> visitEqualTo(EqualToNode node, TransferInput<IndexValue, IndexStore> in) {
		TransferResult<IndexValue, IndexStore> result = super.visitEqualTo(node, in);
		Node left = node.getLeftOperand();
		Node right = node.getRightOperand();
		IndexStore thenStore = result.getRegularStore();
		IndexStore elseStore = thenStore.copy();
		ConditionalTransferResult<IndexValue, IndexStore> newResult =
				new ConditionalTransferResult<>(result.getResultValue(), thenStore, elseStore);
		// refine the left side
		equalsToHelper(left, right, thenStore);
		// refine right side
		equalsToHelper(right, left, thenStore);
		// refine else
		NotEqualHelper(left, right, elseStore);
		// refine right side else
		NotEqualHelper(right, left, elseStore);
		return newResult;
	}

	/**
	 *
	 * @param left
	 * 			the node that is the left side of the comparison
	 * @param right
	 * 			the right side node
	 * @param thenStore
	 * 			the store that the refinement is placed in
	 *
	 * refines the types of the left side node and puts result into passed store
	 */
	private void equalsToHelper(Node left, Node right, IndexStore thenStore) {
		Receiver leftRec = FlowExpressions.internalReprOf(analysis.getTypeFactory(), left);
		Receiver rightRec = FlowExpressions.internalReprOf(analysis.getTypeFactory(), right);
		AnnotatedTypeMirror leftType = atypeFactory.getAnnotatedType(left.getTree());
		AnnotatedTypeMirror rightType = atypeFactory.getAnnotatedType(right.getTree());
		if (left instanceof FieldAccessNode) {
			FieldAccessNode FANode = (FieldAccessNode) left;
			if (FANode.getFieldName().equals("length")) {
				Receiver rec = FlowExpressions.internalReprOf(analysis.getTypeFactory(), FANode.getReceiver());
				if (right.getTree().getKind().equals(Tree.Kind.INT_LITERAL)) {
					int val = (int)((LiteralTree)right.getTree()).getValue();
					// if it already has a minlen use the higher of the two
					if (atypeFactory.getAnnotatedType(FANode.getReceiver().getTree()).hasAnnotation(MinLen.class)) {
						val = Math.max(val, IndexUtils.getMinLen(atypeFactory.getAnnotatedType(FANode.getReceiver().getTree()).getAnnotation(MinLen.class)));
					}
					thenStore.insertValue(rec, IndexAnnotatedTypeFactory.createMinLen(val));
				}
			}
		}
		if (leftType.hasAnnotation(IndexOrLow.class) || leftType.hasAnnotation(LTLength.class) || leftType.hasAnnotation(IndexFor.class)) {
			IOLEqual(leftRec, rightRec, leftType, rightType, thenStore);
		}
		if (leftType.hasAnnotation(IndexOrHigh.class) || leftType.hasAnnotation(NonNegative.class)) {
			NonNegEqual(leftRec, rightType, thenStore);
		}
	}

	//********************************************************************************//
	// these are methods for equalsTo Nodes once left hand annotation is known		//
	//********************************************************************************//
	
	/**
	 * once we know that the left node is IndexOrLow, we go here to refine.
	 */
	private void IOLEqual(Receiver leftRec, Receiver rightRec, AnnotatedTypeMirror leftType,
			AnnotatedTypeMirror rightType, IndexStore thenStore) {
		String leftName = IndexUtils.getValue(leftType.getAnnotationInHierarchy(IndexAnnotatedTypeFactory.indexFor));
		boolean InF = rightType.hasAnnotation(IndexFor.class);
		boolean IOH = rightType.hasAnnotation(IndexOrHigh.class);
		boolean NN = rightType.hasAnnotation(NonNegative.class);
		if (IOH || NN || InF) {
			// they are both a valid index for just IOL array
			thenStore.insertValue(leftRec, IndexAnnotatedTypeFactory.createIndexForAnnotation(leftName));
			if (InF) {
				// add to both left and right operands
				String rightName = IndexUtils.getValue(rightType.getAnnotation(IndexFor.class));
				thenStore.insertValue(leftRec, IndexAnnotatedTypeFactory.createIndexForAnnotation(rightName));
			}
		}
		if (rightType.hasAnnotation(IndexOrLow.class)) {
			thenStore.insertValue(leftRec, IndexAnnotatedTypeFactory.createIndexOrLowAnnotation(leftName));
		}
	}
	
	/**
	 * once we know that the left node is NonNegative, we go here to refine.
	 */
	private void NonNegEqual(Receiver leftRec, AnnotatedTypeMirror rightType, IndexStore thenStore) {
		boolean InF = rightType.hasAnnotation(IndexFor.class);
		boolean IOL = rightType.hasAnnotation(IndexOrLow.class);
		boolean LTL = rightType.hasAnnotation(LTLength.class);
		if (InF || IOL || LTL) {
			String name = IndexUtils.getValue(rightType.getAnnotationInHierarchy(IndexAnnotatedTypeFactory.indexFor));
			thenStore.insertValue(leftRec, IndexAnnotatedTypeFactory.createIndexForAnnotation(name));
		}
		// if right is indexOrHigh left should be too
		if (rightType.hasAnnotation(IndexOrHigh.class)) {
			String name = IndexUtils.getValue(rightType.getAnnotationInHierarchy(IndexAnnotatedTypeFactory.indexFor));
			thenStore.insertValue(leftRec, IndexAnnotatedTypeFactory.createIndexOrHighAnnotation(name));
		}
	}

	//********************************************************************************//
	// these are methods for LessThan Nodes once left operand Annotation is known  //
	//********************************************************************************//
	/**
	 * once we know that the left node is Unknown, we go here to refine.
	 */
	// Unknown < IndexOrHigh(a), IndexFor(a), IndexOrLow(a), LTLength(a) -> LTLength(a)
	private void UnknownLessThan(Receiver rec, Node right, IndexStore thenStore, boolean orEqual) {
		AnnotatedTypeMirror rightType = atypeFactory.getAnnotatedType(right.getTree());
		boolean IOH = rightType.hasAnnotation(IndexOrHigh.class) && !orEqual;
		boolean InF = rightType.hasAnnotation(IndexFor.class);
		boolean IOL = rightType.hasAnnotation(IndexOrLow.class);
		boolean LTL = rightType.hasAnnotation(LTLength.class);
		if (IOH || InF || IOL || LTL) {
			thenStore.clearValue(rec);
			String aValue = IndexUtils.getValue(rightType.getAnnotationInHierarchy(IndexAnnotatedTypeFactory.indexFor));
			AnnotationMirror anno = IndexAnnotatedTypeFactory.createLTLengthAnnotation(aValue);
			thenStore.insertValue(rec, anno);
		}	
	}
	
	/**
	 * once we know that the left node is IndexOrHigh, we go here to refine.
	 */
	// IndexOrHigh < IndexOrHigh(a), IndexFor(a), IndexOrLow(a), LTLength(a) ->IndexFor(a)
	private void IndexOrHighLessThan(Receiver rec, Node right, IndexStore thenStore, boolean orEqual) {
		AnnotatedTypeMirror rightType = atypeFactory.getAnnotatedType(right.getTree());
		boolean IOH = rightType.hasAnnotation(IndexOrHigh.class) && !orEqual;
		boolean InF = rightType.hasAnnotation(IndexFor.class);
		boolean IOL = rightType.hasAnnotation(IndexOrLow.class);
		boolean LTL = rightType.hasAnnotation(LTLength.class);
		if (IOH || InF || IOL || LTL) {
			thenStore.clearValue(rec);
			String name = IndexUtils.getValue(rightType.getAnnotationInHierarchy(IndexAnnotatedTypeFactory.indexOrHigh));
			thenStore.insertValue(rec, IndexAnnotatedTypeFactory.createIndexForAnnotation(name));
		}
		// if left wasnt an indexOrhigh it should be now
		if (rightType.hasAnnotation(IndexOrHigh.class) && orEqual) {
			String name = IndexUtils.getValue(rightType.getAnnotationInHierarchy(IndexAnnotatedTypeFactory.indexOrHigh));
			thenStore.insertValue(rec, IndexAnnotatedTypeFactory.createIndexOrHighAnnotation(name));
		}

	}

	//********************************************************************************//
	// these are methods for GreaterThan Nodes once left operand Annotation is known  //
	//********************************************************************************//
	
	// this returns a transfer result for @Unknown > x
	//Unknown > IndexOrLow, NonNegative, IndexOrHigh, IndexFor -> NonNegative
	/**
	 * once we know that the left node is Unknown, we go here to refine.
	 */
	private void UnknownGreaterThan(Receiver rec, Node right, IndexStore thenStore, boolean orEqual) {
		AnnotatedTypeMirror rightType = atypeFactory.getAnnotatedType(right.getTree());
		// booleans to see if the type is any in the heirarchy we want to refine
		boolean IOL = (rightType.hasAnnotation(IndexOrLow.class) || IndexUtils.isNegOne(right)) && !orEqual;
		boolean NN = rightType.hasAnnotation(NonNegative.class);
		boolean IOH = rightType.hasAnnotation(IndexOrHigh.class);
		boolean IF = rightType.hasAnnotation(IndexFor.class);

		if (IOL || NN || IOH || IF) {
			AnnotationMirror anno = IndexAnnotatedTypeFactory.createNonNegAnnotation();
			thenStore.insertValue(rec, anno);
		}
		
	}
	//IndexOrLow(a) > IndexOrLow, Nonnegative, IndexOrHigh, IndexFor -> IndexFor(a)
	/**
	 * once we know that the left node is IndexOrLow, we go here to refine.
	 */
	private void IndexOrLowGreaterThan(Receiver rec, Node right, IndexStore thenStore, String name, boolean orEqual) {
		AnnotatedTypeMirror rightType = atypeFactory.getAnnotatedType(right.getTree());
		boolean IOL = (rightType.hasAnnotation(IndexOrLow.class) || IndexUtils.isNegOne(right)) && !orEqual;
		boolean NN = rightType.hasAnnotation(NonNegative.class);
		boolean IOH = rightType.hasAnnotation(IndexOrHigh.class);
		boolean InF = rightType.hasAnnotation(IndexFor.class);
		if (IOL || InF || NN || IOH) {
			thenStore.insertValue(rec, IndexAnnotatedTypeFactory.createIndexForAnnotation(name));
		}
		else if ((rightType.hasAnnotation(IndexOrLow.class) || IndexUtils.isNegOne(right))  && orEqual) {
			thenStore.insertValue(rec, IndexAnnotatedTypeFactory.createIndexOrLowAnnotation(name));
		}
	}
}

