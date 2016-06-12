package org.checkerframework.checker.index;

import org.checkerframework.framework.flow.CFAbstractAnalysis;
import org.checkerframework.framework.flow.CFAbstractValue;
import org.checkerframework.framework.type.AnnotatedTypeMirror;

public class IndexValue extends CFAbstractValue<IndexValue> {

    public IndexValue(CFAbstractAnalysis<IndexValue, ?, ?> analysis, AnnotatedTypeMirror type) {
        super(analysis, type);
    }

}
