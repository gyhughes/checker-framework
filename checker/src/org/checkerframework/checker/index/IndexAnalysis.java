package org.checkerframework.checker.index;

import java.util.List;

import javax.lang.model.element.VariableElement;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.common.basetype.BaseTypeChecker;
import org.checkerframework.framework.flow.CFAbstractAnalysis;
import org.checkerframework.framework.type.AnnotatedTypeMirror;
import org.checkerframework.framework.util.AnnotatedTypes;
import org.checkerframework.javacutil.Pair;

// subclasses the base analysis to use our Transfers instead of the deafaults

public class IndexAnalysis extends CFAbstractAnalysis<IndexValue, IndexStore, IndexTransfer> {
    IndexAnnotatedTypeFactory atypeFactory;

    public IndexAnalysis(BaseTypeChecker checker,
            IndexAnnotatedTypeFactory factory,
            List<Pair<VariableElement, IndexValue>> fieldValues) {
        super(checker, factory, fieldValues);
        this.atypeFactory = (IndexAnnotatedTypeFactory)super.atypeFactory;
    }

    //overrides the superclass method to return our transfers
    @Override
    public IndexTransfer createTransferFunction() {
        return new IndexTransfer(this);
    }

    @Override
    public @Nullable IndexValue createAbstractValue(AnnotatedTypeMirror type) {
        if (!AnnotatedTypes.isValidType(qualifierHierarchy, type)) {
            // If the type is not valid, we return null, which is the same as
            // 'no information'.
            return null;
        }
        return new IndexValue(this, type);
    }

    @Override
    public IndexStore createCopiedStore(IndexStore s) {
        return new IndexStore(s);
    }

    @Override
    public IndexStore createEmptyStore(boolean sequentialSemantics) {
        return new IndexStore(this, sequentialSemantics);
    }



}
