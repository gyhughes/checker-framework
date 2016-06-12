package org.checkerframework.checker.index.qual;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

import org.checkerframework.framework.qual.SubtypeOf;

/**
 * This type annotation indicates that an expression's value is
 * within the bounds of the array with the given name
 * (i &ge; 0 &amp;&amp; i &lt; arr.length).
 *
 * @checker_framework.manual #index-checker Index Checker
 */
@SubtypeOf({NonNegative.class, IndexOrHigh.class, IndexOrLow.class, LTLength.class})
@Target({ElementType.TYPE_USE, ElementType.TYPE_PARAMETER})
public @interface IndexFor {
    /** The array for which this value is an index. */
    String value() default "";
}
