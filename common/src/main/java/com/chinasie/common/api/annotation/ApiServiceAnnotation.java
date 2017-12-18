package com.chinasie.common.api.annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by 古永财 on 2016-12-11.
 */

@Target(ElementType.TYPE) // 这是一个对类的注解，还可以是包、类、变量等很多东西
@Retention(RetentionPolicy.RUNTIME) // 保留时间，一般注解就是为了框架开发时代替配置文件使用，JVM运行时用反射取参数处理，所以一般都为RUNTIME类型
@Documented // 用于描述其它类型的annotation应该被作为被标注的程序成员的公共API，因此可以被例如javadoc此类的工具文档化
public @interface ApiServiceAnnotation {

    //---调用服务的API类型 如:"ApiType":"Platform.Domain.Common.Security.UserController,Platform.Domain.Common"
    String ApiType() default "";

}

