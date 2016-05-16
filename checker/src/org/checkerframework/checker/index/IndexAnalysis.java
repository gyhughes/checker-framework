package org.checkerframework.checker.index;

import java.util.List;

import javax.lang.model.element.VariableElement;

import org.checkerframework.checker.nonneg.NonNegAnnotatedTypeFactory;
import org.checkerframework.checker.nonneg.NonNegTransfer;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.common.basetype.BaseTypeChecker;
import org.checkerframework.framework.flow.CFAbstractAnalysis;
import org.checkerframework.framework.flow.CFStore;
import org.checkerframework.framework.flow.CFValue;
import org.checkerframework.framework.type.AnnotatedTypeMirror;
import org.checkerframework.javacutil.Pair;

// subclasses the base analysis to use our Transfers instead of the deafaults

public class IndexAnalysis extends CFAbstractAnalysis<CFValue, CFStore, IndexTransfer> {

	public IndexAnalysis(BaseTypeChecker checker,
			IndexAnnotatedTypeFactory factory,
			List<Pair<VariableElement, CFValue>> fieldValues) {
		super(checker, factory, fieldValues);
	}
	
	//overrides the superclass method to return our transfers
	@Override
	public IndexTransfer createTransferFunction() {
		return new IndexTransfer(this);
	}
	  //**************************************************************//
	 // these methods are just Overridden so that they can use "this"//
	//**************************************************************//
	@Override
	public @Nullable CFValue createAbstractValue(AnnotatedTypeMirror type) {
		return defaultCreateAbstractValue(this, type);
	}
	
	@Override
	public CFStore createCopiedStore(CFStore s) {
		return new CFStore(this, s);
	}
	
	@Override
	public CFStore createEmptyStore(boolean sequentialSemantics) {
		return new CFStore(this, sequentialSemantics);
	}
	
}
