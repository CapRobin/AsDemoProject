package com.demo.entity;
/**
 * Copyright © CapRobin
 *
 * Name：FreezeDataInfo
 * Describe：冻结数据实体类
 * Date：2018-09-04 13:31:53
 * Author: CapRobin@yeah.net
 *
 */
public class FreezeDataInfo {
    //区域名称
    private String AREANAME;
    //用户表号
    private String METER_SERIAL_NUM;
    //客户名称
    private String CUSTOMER_NAME;
    //客户地址
    private String CUSTOMER_ADDRESS;
    //抄表最小日期
    private String MIN_TIME;
    //抄表最小读数
    private String MIN_RAWVAL;
    //抄表最大日期
    private String MAX_TIME;
    //抄表最大度数
    private String MAX_RAWVAL;
    //用量(kW/h)
    private String AMOUNTUSE;

    public String getAREANAME() {
        return AREANAME;
    }

    public void setAREANAME(String AREANAME) {
        this.AREANAME = AREANAME;
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

    public String getMIN_TIME() {
        return MIN_TIME;
    }

    public void setMIN_TIME(String MIN_TIME) {
        this.MIN_TIME = MIN_TIME;
    }

    public String getMIN_RAWVAL() {
        return MIN_RAWVAL;
    }

    public void setMIN_RAWVAL(String MIN_RAWVAL) {
        this.MIN_RAWVAL = MIN_RAWVAL;
    }

    public String getMAX_TIME() {
        return MAX_TIME;
    }

    public void setMAX_TIME(String MAX_TIME) {
        this.MAX_TIME = MAX_TIME;
    }

    public String getMAX_RAWVAL() {
        return MAX_RAWVAL;
    }

    public void setMAX_RAWVAL(String MAX_RAWVAL) {
        this.MAX_RAWVAL = MAX_RAWVAL;
    }

    public String getAMOUNTUSE() {
        return AMOUNTUSE;
    }

    public void setAMOUNTUSE(String AMOUNTUSE) {
        this.AMOUNTUSE = AMOUNTUSE;
    }
}
