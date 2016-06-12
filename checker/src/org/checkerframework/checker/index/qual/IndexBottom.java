package org.checkerframework.checker.index.qual;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

import org.checkerframework.framework.qual.DefaultFor;
import org.checkerframework.framework.qual.ImplicitFor;
import org.checkerframework.framework.qual.LiteralKind;
import org.checkerframework.framework.qual.SubtypeOf;
import org.checkerframework.framework.qual.TargetLocations;
import org.checkerframework.framework.qual.TypeUseLocation;

/**
 * This type annotation indicates that an expression's value is null or is
 * in dead code. This annotation is used internally by the type system but
 * should rarely be written by a programmer.
 *
 * @checker_framework.manual #index-checker Index Checker
 */
@SubtypeOf({IndexFor.class, MinLen.class})
@Target({ ElementType.TYPE_USE, ElementType.TYPE_PARAMETER })
@TargetLocations({ TypeUseLocation.EXPLICIT_LOWER_BOUND,
    TypeUseLocation.EXPLICIT_UPPER_BOUND })
@ImplicitFor(literals = { LiteralKind.NULL },
  typeNames = {java.lang.Void.class})
@DefaultFor({ TypeUseLocation.LOWER_BOUND })
public @interface IndexBottom {
}
