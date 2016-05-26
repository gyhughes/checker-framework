package org.checkerframework.checker.index;

import java.util.HashMap;
import java.util.Map;

import org.checkerframework.checker.index.qual.IndexFor;
import org.checkerframework.checker.index.qual.IndexOrHigh;
import org.checkerframework.checker.index.qual.IndexOrLow;
import org.checkerframework.checker.index.qual.LTLength;
import org.checkerframework.checker.index.qual.NonNegative;
import org.checkerframework.dataflow.analysis.FlowExpressions;
import org.checkerframework.dataflow.analysis.FlowExpressions.FieldAccess;
import org.checkerframework.dataflow.analysis.FlowExpressions.Receiver;
import org.checkerframework.dataflow.cfg.node.MethodInvocationNode;
import org.checkerframework.dataflow.cfg.node.Node;
import org.checkerframework.framework.flow.CFAbstractAnalysis;
import org.checkerframework.framework.flow.CFAbstractStore;
import org.checkerframework.framework.type.AnnotatedTypeFactory;
import org.checkerframework.framework.type.AnnotatedTypeMirror;

public class IndexStore extends CFAbstractStore<IndexValue, IndexStore> {

	protected IndexStore(IndexStore other) {
		super(other);
	}
	public IndexStore(CFAbstractAnalysis<IndexValue, IndexStore, ?> analysis, boolean sequentialSemantics) {
		super(analysis, sequentialSemantics);
	}
	
    // changes values similar to ++ if we remove from a list
    // if we clear a list anything NonNegatvie goes to NonNeg else becomes unknown
	@Override
	public void updateForMethodCall(MethodInvocationNode n,
            AnnotatedTypeFactory atypeFactory, IndexValue val) {
		String methodName = n.getTarget().getMethod().toString();
		boolean remove = methodName.startsWith("remove(");
        boolean clear = methodName.startsWith("clear(");
		Map<Receiver, IndexValue> replace = new HashMap<Receiver, IndexValue>();
		if (clear) {
			for (FlowExpressions.LocalVariable rec: localVariableValues.keySet()) {
				applyTransfer(rec, replace, true);
			}
			for (FieldAccess rec: fieldValues.keySet()) {
				applyTransfer(rec, replace,  true);
			}
		}
		if (remove) {
			for (FlowExpressions.LocalVariable rec: localVariableValues.keySet()) {
				applyTransfer(rec, replace, false);
			}
			for (FieldAccess rec: fieldValues.keySet()) {
				applyTransfer(rec, replace, false);
			}
		}
		for (Receiver rec: replace.keySet()) {
			replaceValue(rec, replace.get(rec));
		}
		
		super.updateForMethodCall(n, atypeFactory, val);
	}
	// takes a map to store rec for change -> the new value (if we just replace it breaks iterator)
	// we figure out what annotation the reciever passed has
	// and based on whether or not the method is a call to clear we get the new value for it
	// then put it in the map
	private void applyTransfer(Receiver rec, Map<Receiver, IndexValue> replace, boolean isClear) {
		IndexAnnotatedTypeFactory atypeFactory = ((IndexAnalysis)this.analysis).atypeFactory;
		IndexValue value = this.getValue(rec);
		AnnotatedTypeMirror atm = value.getType();
		boolean InF = atm.hasAnnotation(IndexFor.class);
		boolean IOH = atm.hasAnnotation(IndexOrHigh.class);
		boolean IOL = atm.hasAnnotation(IndexOrLow.class);
		boolean NN = atm.hasAnnotation(NonNegative.class);
		if (!isClear) {
			if (InF || IOL) {
				String name = IndexUtils.getValue(atm.getAnnotationInHierarchy(IndexAnnotatedTypeFactory.indexFor));
				IndexValue val = analysis.createSingleAnnotationValue(atypeFactory.createIndexOrHighAnnotation(name), rec.getType());
				replace.put(rec, val);
			} else if (NN || IOH) {
				IndexValue val = analysis.createSingleAnnotationValue(atypeFactory.createNonNegAnnotation(), rec.getType());
				replace.put(rec, val);
			} else {
				IndexValue val = analysis.createSingleAnnotationValue(atypeFactory.createUnknownAnnotation(), rec.getType());
				replace.put(rec, val);
			}
		} else {
			if (InF || IOL || NN) {
				IndexValue val = analysis.createSingleAnnotationValue(atypeFactory.createNonNegAnnotation(), rec.getType());
				replace.put(rec, val);
			} else {
				IndexValue val = analysis.createSingleAnnotationValue(atypeFactory.createUnknownAnnotation(), rec.getType());
				replace.put(rec, val);
			}
		}
		
	}
	@Override
	public void updateForAssignment(Node n ,IndexValue val) {
		super.updateForAssignment(n, val);
		String[] classes = n.toString().split("//.");
		String name = classes[classes.length -1];
		// make a map to store all the update we want to make
		Map<Receiver, IndexValue> replace = new HashMap<Receiver, IndexValue>();
		// update all local variable info
		for (FlowExpressions.LocalVariable rec: localVariableValues.keySet()) {
			applyAssign(rec, replace, name);
		}
		// update all field info
		for (FieldAccess rec: fieldValues.keySet()) {
			applyAssign(rec, replace, name);
		}
		// put those update in to the store
		for (Receiver rec: replace.keySet()) {
			replaceValue(rec, replace.get(rec));
		}
	}
	
	// get a type receiver the map that holds the changes we want to make, and the name of the target
	// we want to change any annotations that are connected to what we are reassign
	private void applyAssign(Receiver rec, Map<Receiver, IndexValue> replace, String name) {
		IndexValue value = this.getValue(rec);
		AnnotatedTypeMirror atm = value.getType();
		boolean InF = atm.hasAnnotation(IndexFor.class);
		boolean IOH = atm.hasAnnotation(IndexOrHigh.class);
		boolean IOL = atm.hasAnnotation(IndexOrLow.class);
		boolean LTL = atm.hasAnnotation(LTLength.class);
		// if this rec has a type connected to an array
		if (InF || IOH || IOL || LTL) {
			String val = IndexUtils.getValue(atm.getAnnotationInHierarchy(IndexAnnotatedTypeFactory.indexFor));
			// if that array has the same name as the thing being assigned
			if (val.equals(name) || val.contains(name + '.')) {
				// treat this as a clearing on value(retain only info about nonneg)
				applyTransfer(rec, replace, true);
			}
		}
	}
}
