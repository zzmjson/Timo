package com.linln.modules.system.service;

import java.lang.annotation.*;

import static java.lang.annotation.RetentionPolicy.RUNTIME;


@Documented//说明该注解将被包含在javadoc中
@Target({ElementType.METHOD, ElementType.TYPE})//目标下的方法和类型
@Retention(RUNTIME)// 注解会在class字节码文件中存在，在运行时可以通过反射获取到
@Inherited//说明子类可以继承父类中的该注解   //
public  @interface   DoneTime {
    String param() default "";
}