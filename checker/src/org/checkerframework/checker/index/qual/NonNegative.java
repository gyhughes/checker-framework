package org.checkerframework.checker.index.qual;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

import org.checkerframework.framework.qual.SubtypeOf;

/**
 * This type annotation indicates that an expression's value is non-negative.
 *
 * @checker_framework.manual #index-checker Index Checker
 */
@SubtypeOf(UnknownIndex.class)
@Target({ElementType.TYPE_USE, ElementType.TYPE_PARAMETER})
public @interface NonNegative {
}
