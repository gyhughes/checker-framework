package org.checkerframework.checker.index.qual;

import org.checkerframework.framework.qual.ImplicitFor;
import org.checkerframework.framework.qual.LiteralKind;
import org.checkerframework.framework.qual.SubtypeOf;

@SubtypeOf({IndexFor.class, MinLen.class})
@ImplicitFor(literals = {LiteralKind.NULL},
typeNames = {java.lang.Void.class})
public @interface IndexBottom {
}
