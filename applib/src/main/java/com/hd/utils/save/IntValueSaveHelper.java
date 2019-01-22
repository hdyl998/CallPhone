package com.hd.utils.save;


import com.hd.cache.SpUtils;

/**
 * Created by Administrator on 2018/3/22.
 */

public class IntValueSaveHelper {
    public String file;
    public String key;
    public int value;

    public int defaultValue;

    public void setDefaultValue(int defaultValue) {
        this.defaultValue = defaultValue;
    }

    public IntValueSaveHelper() {
    }

    public IntValueSaveHelper(String file, String key) {
        setFile(file);
        setKey(key);
        read();
    }


    public void setValue(int value) {
        if (value != this.value) {
            this.value = value;
            save();
        }
    }

    public int getValue() {
        return value;
    }

    public boolean isDefaultValue() {
        return getDefaultValue() == getValue();
    }

    public int getDefaultValue() {
        return defaultValue;
    }


    public void setFile(String file) {
        this.file = file;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void read() {
        value = SpUtils.getInt( file, key, defaultValue);
    }

    /***
     * 自增1
     */
    public void addOne() {
        value++;
        save();
    }


    public void save() {
        SpUtils.putInt( file, key, value);
    }

}
