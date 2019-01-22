package com.hd.utils.save;

import com.hd.cache.SpUtils;

/**
 * Created by Administrator on 2018/3/22.
 */

public class StringValueSaveHelper {
    public String file;
    public String key;
    public String value;

    public String defaultValue;
//
//    private BaseCache cache;

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public StringValueSaveHelper() {
    }

    public StringValueSaveHelper(String file, String key) {
        setFile(file);
        setKey(key);
        read();
    }

    public void setValue(String value) {
        this.value = value;
        save();
    }

    public String getValue() {
        return value;
    }

    public boolean isDefaultValue() {
        if (getDefaultValue() == null) {
            if (getValue() == null) {
                return true;
            }
            return false;
        }
        return getDefaultValue().equals(getValue());
    }

    public String getDefaultValue() {
        return defaultValue;
    }


    public void setFile(String file) {
        this.file = file;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void read() {
        value = SpUtils.getString( file, key, defaultValue);
    }


    public void save() {
        SpUtils.putString( file, key, value);
    }

}
