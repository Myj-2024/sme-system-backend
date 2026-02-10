package com.sme.annotation;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequirePermission {
    /**
     * 权限编码数组
     */
    String[] value();

    /**
     * 逻辑关系：AND-必须包含所有权限，OR-包含其中一个即可
     */
    Logical logical() default Logical.OR;

    enum Logical {
        AND, OR
    }
}