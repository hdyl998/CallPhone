package com.hd.utils.save;


import com.hd.cache.SpUtils;

/**
 * Created by Administrator on 2018/3/22.
 */

public class LongValueSaveHelper {
    public String file;
    public String key;
    public long value;

    public long defaultValue;

    public void setDefaultValue(long defaultValue) {
        this.defaultValue = defaultValue;
    }

    public LongValueSaveHelper() {
    }

    public LongValueSaveHelper(String file, String key) {
        setFile(file);
        setKey(key);
        read();
    }


    public void setValue(long value) {
        if (value != this.value) {
            this.value = value;
            save();
        }
    }

    public long getValue() {
        return value;
    }

    public boolean isDefaultValue() {
        return getDefaultValue() == getValue();
    }

    public long getDefaultValue() {
        return defaultValue;
    }


    public void setFile(String file) {
        this.file = file;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void read() {
        value = SpUtils.getLong( file, key, defaultValue);
    }

    /***
     * 自增1
     */
    public void addOne() {
        value++;
        save();
    }


    public void save() {
        SpUtils.putLong(file, key, value);
    }

}
