package org.checkerframework.checker.index.qual;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

import org.checkerframework.framework.qual.SubtypeOf;

/**
 * This type annotation indicates that an expression's value is
 * possibly one beyond the bounds of the array with the given name
 * (i &ge; 0 &amp;&amp; i &lt; arr.length + 1).
 *
 * @checker_framework.manual #index-checker Index Checker
 */
@SubtypeOf({UnknownIndex.class, NonNegative.class})
@Target({ElementType.TYPE_USE, ElementType.TYPE_PARAMETER})
public @interface IndexOrHigh {
	/** The array for which this value is an index
	 * (or the value is the array's upper bound). */
	String value() default "";
}
