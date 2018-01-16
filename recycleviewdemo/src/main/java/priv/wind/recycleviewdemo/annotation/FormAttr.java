package priv.wind.recycleviewdemo.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 表单属性注解
 *
 * @author Dongbaicheng
 * @version 2017/12/4
 */

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface FormAttr {
    /**
     * 表头名称
     *
     * @return 表头名称
     */
    String name() default "未命名";

    /**
     * 字段排序序号（从1开始）
     *
     * @return 字段排序
     */
    int sequence() default -1;

    /**
     * 指定列宽
     *
     * @return 列宽
     */
    int width() default 120;
}
