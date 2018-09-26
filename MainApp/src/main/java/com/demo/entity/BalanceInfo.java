package com.demo.entity;

/**
 * Copyright © CapRobin
 *
 * Name：BalanceInfo
 * Describe：余额信息
 * Date：2018-07-24 16:41:59
 * Author: CapRobin@yeah.net
 *
 */
public class BalanceInfo {

    //ID
    private int CUSTOMER_ID;
    //表地址
    private String METER_SERIAL_NUM;
    //户名
    private String CUSTOMER_NAME;
    //手机号
    private String PHONE1;
    //表余额
    private String CURRENT_BALANCE;
    //装表地址
    private String CUSTOMER_ADDRESS;

    public int getCUSTOMER_ID() {
        return CUSTOMER_ID;
    }

    public void setCUSTOMER_ID(int CUSTOMER_ID) {
        this.CUSTOMER_ID = CUSTOMER_ID;
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

    public String getPHONE1() {
        return PHONE1;
    }

    public void setPHONE1(String PHONE1) {
        this.PHONE1 = PHONE1;
    }

    public String getCURRENT_BALANCE() {
        return CURRENT_BALANCE;
    }

    public void setCURRENT_BALANCE(String CURRENT_BALANCE) {
        this.CURRENT_BALANCE = CURRENT_BALANCE;
    }

    public String getCUSTOMER_ADDRESS() {
        return CUSTOMER_ADDRESS;
    }

    public void setCUSTOMER_ADDRESS(String CUSTOMER_ADDRESS) {
        this.CUSTOMER_ADDRESS = CUSTOMER_ADDRESS;
    }
}
