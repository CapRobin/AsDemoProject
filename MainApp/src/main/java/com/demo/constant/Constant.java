package com.demo.constant;

/**
 * Copyright © CapRobin
 * <p>
 * Name：Constant
 * Describe：常量工具类
 * Date：2018-08-17 10:10:58
 * Author: CapRobin@yeah.net
 */
public class Constant {

    public static int UserTypeNormal=0x01;
    public static int UserTypeAdmin=0x02;


    public static String HOST = "http://192.168.2.211:8080/lggmr";
    //public static String HOST = "http://192.168.0.20:8091/lggmr";

    //存储文件名称
    public static final String NAME = "dahua";
    //账号
    public static final String USERNAME = "uName";
    //密码
    public static final String USERPASSWORD = "uPwd";
    //用户类型
    public static final String USERTYPE = "uType";
    //是否第一次登录
    public static final String ISFIRSTLOGIN = "isFirstLogin";

    /*-----------接口常量-----------*/
    //系统登录
    public static String LOGIN = HOST + "/appLogin";
    //修改密码
    public static String UPDATEPWD = HOST + "/appUpdatePwd";
    //获取所有表信息
    public static String METERINFO = HOST + "/appUserMeters";
    //冻结数据查询
    public static String QUERYFREEZEDATA = HOST + "/appQueryFreezeData";
    //余额查询
    public static String QUERYBALANCE = HOST + "/queryBalance";
    //拉闸合闸
    public static String SWICHPOWER = HOST + "/pullSwitchMeter";
}
