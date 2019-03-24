package com.examle.core.common.extension;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface Activate {
    String[] group() default {};
    String[] value() default {};
    @Deprecated
    String[] before() default {};
    @Deprecated
    String[] after() default {};
    int order() default 0;
}
