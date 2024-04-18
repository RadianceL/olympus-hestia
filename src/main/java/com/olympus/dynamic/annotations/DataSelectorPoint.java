package com.olympus.dynamic.annotations;

import java.lang.annotation.*;

/**
 * 数据库选择注解
 *
 * @author eddie.lys
 * @since 2024/4/18
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DataSelectorPoint {

    String datasource() default "default";
}
