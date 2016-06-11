package org.checkerframework.checker.index.qual;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

import org.checkerframework.framework.qual.SubtypeOf;

/**
 * This type annotation indicates that an expression's value is
 * -1 or is within the bounds of the array with the given name
 * (i &ge; -1 &amp;&amp; i &lt; arr.length).
 *
 * @checker_framework.manual #index-checker Index Checker
 */
@SubtypeOf({UnknownIndex.class, LTLength.class})
@Target({ElementType.TYPE_USE, ElementType.TYPE_PARAMETER})
public @interface IndexOrLow {
	/** The array for which this value is an index (or the value is -1). */
	String value() default "";
}
