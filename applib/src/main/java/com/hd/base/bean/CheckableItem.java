package com.hd.base.bean;

import android.widget.Checkable;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/12/11.
 */

public class CheckableItem implements Checkable, Serializable {
    private boolean isChecked = false;
    private String title;


    public String getTitle() {
        return title;
    }


    public CheckableItem() {

    }

    public CheckableItem(String title) {
        this.title = title;
    }

    public CheckableItem(String title,boolean isChecked) {
        this.title = title;
        this.isChecked=isChecked;
    }

    @Override
    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public boolean isChecked() {
        return isChecked;
    }

    @Override
    public void toggle() {
        setChecked(!isChecked);
    }
}
