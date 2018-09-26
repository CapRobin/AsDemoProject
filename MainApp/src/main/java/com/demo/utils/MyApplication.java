package com.demo.utils;

import android.app.Application;

import com.demo.entity.CustomInfo;
import com.demo.entity.MeterInfo;

import org.xutils.x;

/**
 * Copyright © CapRobin
 *
 * Name：MyApplication
 * Describe：程序入口
 * Date：2018-08-28 17:13:12
 * Author: CapRobin@yeah.net
 *
 */
public class MyApplication extends Application {
    public Preferences preference;
    public static MeterInfo meterInfoLists;
    public static CustomInfo mCustomInfo;
    //1：客户登录；2：管理登录
    public static int userType = 2;
    //管理员登录ID
    public static int adminId = 0;

    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
        x.Ext.setDebug(true);
        //初始化本地存储文件
        preference = Preferences.instance(getApplicationContext());
    }
}
