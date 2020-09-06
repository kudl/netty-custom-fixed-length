package com.kudlwork.netty.common.parser.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface FixedLengthElement {

    int seq();

    int maxLength();

    char padChar() default ' ';

    PadMode padMode() default PadMode.RIGHT;
}
