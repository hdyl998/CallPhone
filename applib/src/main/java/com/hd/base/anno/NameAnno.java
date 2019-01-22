package com.hd.base.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 注解去取名字，用于统计  使用方法地址 http://hejiangtao.iteye.com/blog/1381225
 * Created by liugd on 2017/4/15.
 */
@Retention(RetentionPolicy.RUNTIME) //运行时
@Target(ElementType.TYPE)//注解使用情景用于类
@Inherited
public @interface NameAnno {
    String name() default ""; //这个名字用于传给后台友盟或者talkingdata作统计

    String code() default "";//统计需要的定义的编码
}
