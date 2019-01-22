package com.hd.utils.log.impl;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.hd.utils.log.Logger;

/**
 * Created by Administrator on 2018/1/25.
 */

public class OutLogAdapter implements ILogAdapter {
    @Override
    public void print(String tag, Object o) {
        if (o == null) {
            Logger.t(tag).e("NULL");
        }
        if (o instanceof String) {
            String oo = o + "";
            if (oo.startsWith("{") || oo.startsWith("[")) {
                Logger.t(tag).json((String) o);
            } else
                Logger.t(tag).e(oo);
        } else if (o instanceof Integer || o instanceof Long || o instanceof Double || o instanceof Boolean || o instanceof Float || o instanceof Character) {
            Logger.t(tag).e(o + "");
        } else {
            Logger.t(tag).json(JSON.toJSONString(o));
        }
    }

    @Override
    public void print(Object o) {
        print("lgdx", o);
    }

    @Override
    public void printString(String tag, String string) {
        if(string==null){
            string="null";
        }
        Log.e(tag, string);
    }
}
