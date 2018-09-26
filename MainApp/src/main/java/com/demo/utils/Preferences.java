package com.demo.utils;


import android.content.Context;
import android.content.SharedPreferences;

import com.demo.constant.Constant;

/**
 * Copyright © CapRobin
 * <p>
 * Name：Preferences
 * Describe：保存参数
 * Date：2018-08-09 11:58:19
 * Author: CapRobin@yeah.net
 */
public class Preferences {

    public static Preferences mPreferences;
    private SharedPreferences spf;

    public static Preferences instance(Context context) {
        if (mPreferences == null) {
            mPreferences = new Preferences(context);
        }
        return mPreferences;
    }

    private Preferences(Context context) {
        spf = context.getSharedPreferences(Constant.NAME, Context.MODE_PRIVATE);
    }


    public void putBoolean(String key, boolean value) {
        spf.edit().putBoolean(key, value).commit();
    }

    public boolean getBoolean(String key) {
        return spf.getBoolean(key, false);
    }

    public void putString(String key, String value) {
        spf.edit().putString(key, value).commit();
    }

    public String getString(String key) {
        return spf.getString(key, "");
    }

    public void putInt(String key, int value) {
        spf.edit().putInt(key, value).commit();
    }

    public void putLong(String key, long value) {
        spf.edit().putLong(key, value).commit();
    }

    public int getInt(String key) {
        return spf.getInt(key, 0);
    }

    public int getInt(String key, int defaultValue) {
        return spf.getInt(key, defaultValue);
    }

    public long getLong(String key) {
        return spf.getLong(key, 0);
    }

    public long getLong(String key, long def) {
        return spf.getLong(key, def);
    }

    public void clearData() {
        spf.edit().clear().commit();
    }

    public void remove(String key) {
        spf.edit().remove(key).commit();
    }

    public void commit() {
        spf.edit().commit();
    }

}
