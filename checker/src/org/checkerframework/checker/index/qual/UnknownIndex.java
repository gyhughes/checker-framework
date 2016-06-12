package org.checkerframework.checker.index.qual;

import java.lang.annotation.*;

import org.checkerframework.framework.qual.DefaultQualifierInHierarchy;
import org.checkerframework.framework.qual.SubtypeOf;

/**
 * This type annotation indicates that no information is known about the
 * upper and lower bounds of the value. This annotation is used internally
 * by the type system but should rarely be written by a programmer.
 *
 * @checker_framework.manual #index-checker Index Checker
 */
@SubtypeOf({})
@Target({ElementType.TYPE_USE, ElementType.TYPE_PARAMETER})
@DefaultQualifierInHierarchy
public @interface UnknownIndex {
}
