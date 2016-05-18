package org.checkerframework.checker.index;

import org.checkerframework.dataflow.cfg.node.MethodInvocationNode;
import org.checkerframework.framework.flow.CFAbstractAnalysis;
import org.checkerframework.framework.flow.CFAbstractStore;
import org.checkerframework.framework.type.AnnotatedTypeFactory;

public class IndexStore extends CFAbstractStore<IndexValue, IndexStore> {

	
    protected IndexStore(IndexStore other) {
		super(other);
	}
   
    public IndexStore(CFAbstractAnalysis<IndexValue, IndexStore, ?> analysis, boolean sequentialSemantics) {
        super(analysis, sequentialSemantics);
    }

	// change update to throw away info if we call remove or clear
	@Override
	public void updateForMethodCall(MethodInvocationNode n,
            AnnotatedTypeFactory atypeFactory, IndexValue val) {
		String methodName = n.getTarget().getMethod().toString();
		boolean remove = methodName.startsWith("remove(");
        boolean clear = methodName.startsWith("clear(");
		if (remove || clear) {
			localVariableValues.clear();
			fieldValues.clear();
			//methodValues.clear();
			//arrayValues.clear();
			//classValues.clear();
		}
		
		
		super.updateForMethodCall(n, atypeFactory, val);
	}
}
