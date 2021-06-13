package com.ddd.moodof.adapter.presentation;

import java.lang.annotation.*;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SharedBoardId {
    String value() default "";
}