package com.hd.base.datacount;

/**
 * Note：None
 * Created by lgd on 2019/1/4 14:23
 * E-Mail Address：986850427@qq.com
 */
public  class EventInjectHelper {


    private static IEventInject inject=new EmptyEventInject();


    public static void setInject(IEventInject inject) {
        EventInjectHelper.inject = inject;
    }


    public static IEventInject getInject() {
        return inject;
    }
}
