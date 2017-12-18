package priv.wind.recycleviewdemo.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 表头名称注解
 * @author Dongbaicheng
 * @version 2017/12/4
 */

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface HeaderName {
    String name() default "未命名";
}
