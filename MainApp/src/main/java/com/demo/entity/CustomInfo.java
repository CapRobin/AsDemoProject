package com.demo.entity;

/**
 * Copyright © CapRobin
 *
 * Name：CustomInfo
 * Describe：客户信息
 * Date：2018-09-06 14:14:20
 * Author: CapRobin@yeah.net
 *
 */
public class CustomInfo {
    //用户ID
    private int ID;
    //用户表号
    private String METER_SERIAL_NUM;
    //用户姓名
    private String CUSTOMER_NAME;
    //用户地址
    private String CUSTOMER_ADDRESS;
    //手机号码(普通用户账号)
    private String PHONE1;
    //创建时间
    private String CREATE_DATE;
    //表名称
    private String METER_NAME;
    //表类型
    private String METER_TYPE;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getMETER_SERIAL_NUM() {
        return METER_SERIAL_NUM;
    }

    public void setMETER_SERIAL_NUM(String METER_SERIAL_NUM) {
        this.METER_SERIAL_NUM = METER_SERIAL_NUM;
    }

    public String getCUSTOMER_NAME() {
        return CUSTOMER_NAME;
    }

    public void setCUSTOMER_NAME(String CUSTOMER_NAME) {
        this.CUSTOMER_NAME = CUSTOMER_NAME;
    }

    public String getCUSTOMER_ADDRESS() {
        return CUSTOMER_ADDRESS;
    }

    public void setCUSTOMER_ADDRESS(String CUSTOMER_ADDRESS) {
        this.CUSTOMER_ADDRESS = CUSTOMER_ADDRESS;
    }

    public String getPHONE1() {
        return PHONE1;
    }

    public void setPHONE1(String PHONE1) {
        this.PHONE1 = PHONE1;
    }

    public String getCREATE_DATE() {
        return CREATE_DATE;
    }

    public void setCREATE_DATE(String CREATE_DATE) {
        this.CREATE_DATE = CREATE_DATE;
    }

    public String getMETER_NAME() {
        return METER_NAME;
    }

    public void setMETER_NAME(String METER_NAME) {
        this.METER_NAME = METER_NAME;
    }

    public String getMETER_TYPE() {
        return METER_TYPE;
    }

    public void setMETER_TYPE(String METER_TYPE) {
        this.METER_TYPE = METER_TYPE;
    }
}
