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
	protected IndexAnnotatedTypeFactory atypeFactory;

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
			UnknownGreaterThan(rec, right, thenStore);
		}
		if (leftType.hasAnnotation(IndexOrLow.class)) {
			AnnotationMirror leftAnno = leftType.getAnnotation(IndexOrLow.class);
			String name = getValue(leftAnno);
			IndexOrLowGreaterThan(rec, right, thenStore, name);
		}
		if (leftType.hasAnnotation(LTLength.class)) {
			AnnotationMirror leftAnno = leftType.getAnnotation(LTLength.class);
			String name = getValue(leftAnno);
			// we can use this method because it refines the same
			IndexOrLowGreaterThan(rec, right, thenStore, name);
		}
		return newResult;
	}
	
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
			UnknownGreaterThanOrEqual(rec, right, thenStore);
		}
		if (leftType.hasAnnotation(IndexOrLow.class)) {
			AnnotationMirror leftAnno = leftType.getAnnotation(IndexOrLow.class);
			String name = getValue(leftAnno);
			IndexOrLowGreaterThanOrEqual(rec, right, thenStore, name);
		}
		if (leftType.hasAnnotation(LTLength.class)) {
			AnnotationMirror leftAnno = leftType.getAnnotation(LTLength.class);
			String name = getValue(leftAnno);
			// we can use this method because it refines the same
			IndexOrLowGreaterThanOrEqual(rec, right, thenStore, name);
		}
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
		
		if (leftType.hasAnnotation(Unknown.class)) {
			UnknownLessThan(rec, right, thenStore);
		}
		if (leftType.hasAnnotation(IndexOrHigh.class) || leftType.hasAnnotation(NonNegative.class)) {
			IndexOrHighLessThan(rec, right, thenStore);
		}
		return newResult;
	}

	
	//********************************************************************************//
	// these are methods for LessThan Nodes once left operand Annotation is known  //
	//********************************************************************************//
	private void UnknownLessThan(Receiver rec, Node right, CFStore thenStore) {
		AnnotatedTypeMirror rightType = atypeFactory.getAnnotatedType(right.getTree());
		boolean IOH = rightType.hasAnnotation(IndexOrHigh.class);
		boolean InF = rightType.hasAnnotation(IndexFor.class);
		boolean IOL = rightType.hasAnnotation(IndexOrLow.class);
		boolean LTL = rightType.hasAnnotation(LTLength.class);
		if (IOH || InF || IOL || LTL) {
			String aValue = getValue(rightType.getAnnotationInHierarchy(atypeFactory.IndexFor));
			AnnotationMirror anno = atypeFactory.createLTLengthAnnotation(aValue);
			thenStore.insertValue(rec, anno);
		}	
	}
	private void IndexOrHighLessThan(Receiver rec, Node right, CFStore thenStore){
		AnnotatedTypeMirror rightType = atypeFactory.getAnnotatedType(right.getTree());
		for (AnnotationMirror anno: rightType.getAnnotations()) {
			boolean IOH = AnnotationUtils.areSameIgnoringValues(anno, atypeFactory.IndexOrHigh);
			boolean InF = AnnotationUtils.areSameIgnoringValues(anno, atypeFactory.IndexFor);
			boolean IOL = AnnotationUtils.areSameIgnoringValues(anno, atypeFactory.IndexOrLow);
			boolean LTL = AnnotationUtils.areSameIgnoringValues(anno, atypeFactory.LTLength);
			if (IOH || InF || IOL || LTL) {
				String name = getValue(anno);
				thenStore.insertValue(rec, atypeFactory.createIndexForAnnotation(name));
			}
		}
		
	}
	
	
	
	
	
	//********************************************************************************//
	// these are methods for GreaterThan Nodes once left operand Annotation is known  //
	//********************************************************************************//
	// this returns a transfer result for @Unknown > x
	private void UnknownGreaterThan(Receiver rec, Node right, CFStore thenStore) {
		AnnotatedTypeMirror rightType = atypeFactory.getAnnotatedType(right.getTree());
		// booleans to see if the type is any in the heirarchy we want to refine
		boolean IOL = rightType.hasAnnotation(IndexOrLow.class);
		boolean NN = rightType.hasAnnotation(NonNegative.class);
		boolean IOH = rightType.hasAnnotation(IndexOrHigh.class);
		boolean IF = rightType.hasAnnotation(IndexFor.class);
		if (IOL || NN || IOH || IF) {
			AnnotationMirror anno = atypeFactory.createNonNegAnnotation();
			thenStore.insertValue(rec, anno);
		}
	}
	
	private void IndexOrLowGreaterThan(Receiver rec, Node right, CFStore thenStore, String name) {
		AnnotatedTypeMirror rightType = atypeFactory.getAnnotatedType(right.getTree());
		boolean IOL = rightType.hasAnnotation(IndexOrLow.class);
		boolean NN = rightType.hasAnnotation(NonNegative.class);
		boolean IOH = rightType.hasAnnotation(IndexOrHigh.class);
		boolean InF = rightType.hasAnnotation(IndexFor.class);
		if (IOL || InF || NN || IOH) {
			thenStore.insertValue(rec, atypeFactory.createIndexForAnnotation(name));
		}
	}
	
	//*******************************************************************************************//
		// these are methods for GreaterThanOrEqual Nodes once left operand Annotation is known  //
		//***************************************************************************************//
		// this returns a transfer result for @Unknown > x
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
		
		private void IndexOrLowGreaterThanOrEqual(Receiver rec, Node right, CFStore thenStore, String name) {

			AnnotatedTypeMirror rightType = atypeFactory.getAnnotatedType(right.getTree());
			for (AnnotationMirror anno: rightType.getAnnotations()) {
				boolean InF = AnnotationUtils.areSameIgnoringValues(anno, atypeFactory.IndexFor);
				boolean NN = rightType.hasAnnotation(NonNegative.class);
				boolean IOH = rightType.hasAnnotation(IndexOrHigh.class);
				if (InF || NN || IOH) {
					String aValue = getValue(anno);
					if (name.equalsIgnoreCase(aValue)) {
						thenStore.insertValue(rec, atypeFactory.createIndexForAnnotation(name));
					}
				}
			}
		}

	// uses a helper method in the visitor and the factory to get the value of the annotation
	public String getValue(AnnotationMirror anno) {
		return IndexVisitor.getIndexValue(anno, atypeFactory.getValueMethod(anno));
	}
}

