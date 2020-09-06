package com.kudlwork.netty.common.parser.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ToFixedLength {

	VariableIndexType type() default VariableIndexType.NONE;

	int startIndex() default 0;

	int size() default 0;
}
