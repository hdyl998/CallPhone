package com.hd.utils.text;

/**
 * Created by liugd on 2017/4/12.
 */

public class DefaultTextStyle extends ITextStyle {

    public DefaultTextStyle(String string) {
        super(string);
    }

    @Override
    public CharSequence getString() {
        return mStr;
    }
}
