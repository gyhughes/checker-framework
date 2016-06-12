package org.checkerframework.checker.index.qual;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

import org.checkerframework.framework.qual.SubtypeOf;

/**
 * This type annotation indicates that an expression's value is
 * less than the upper bound of the array with the given name
 * (i &lt; arr.length).
 *
 * @checker_framework.manual #index-checker Index Checker
 */
@SubtypeOf(UnknownIndex.class)
@Target({ElementType.TYPE_USE, ElementType.TYPE_PARAMETER})
public @interface LTLength {
    /** The array for which this value is within the upper bound. */
    String value();
}
