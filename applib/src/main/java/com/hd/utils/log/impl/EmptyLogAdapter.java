package com.hd.utils.log.impl;

/**
 * Created by Administrator on 2018/1/25.
 */

public class EmptyLogAdapter implements ILogAdapter{
    @Override
    public void print(String tag, Object o) {

    }

    @Override
    public void print(Object o) {

    }

    @Override
    public void printString(String tag, String string) {

    }
}
