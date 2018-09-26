package com.demo.entity;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2016-10-21.
 */

public class MeterInfoModel {


    //  表类型
    private String MeterType;
    //表地址
    private String COMM_ADDRESS;
    //  户名
    private String CUSTOMER_NAME;
    //手机号
    private String PHONE1;
    //终端地址
    private String TERMINAL_ADDRESS;

    private String AREANAME;

    public MeterInfoModel(String meterType, String COMM_ADDRESS, String CUSTOMER_NAME, String PHONE1, String TERMINAL_ADDRESS, String AREANAME) {
        MeterType = meterType;
        this.COMM_ADDRESS = COMM_ADDRESS;
        this.CUSTOMER_NAME = CUSTOMER_NAME;
        this.PHONE1 = PHONE1;
        this.TERMINAL_ADDRESS = TERMINAL_ADDRESS;
        this.AREANAME = AREANAME;
    }

    public MeterInfoModel() {
    }


    public void of(JSONObject data) throws JSONException {
        this.setCOMM_ADDRESS(data.getString("COMM_ADDRESS"));
//        this.setMeterType(data.getString("MeterType"));
        this.setCUSTOMER_NAME(data.getString("CUSTOMER_NAME"));
        this.setPHONE1(data.getString("PHONE1"));
        this.setTERMINAL_ADDRESS(data.getString("TERMINAL_ADDRESS"));
        this.setAREANAME(data.getString("AREANAME"));
    }

    public String getMeterType() {
        return MeterType;
    }

    public void setMeterType(String meterType) {
        MeterType = meterType;
    }

    public String getCOMM_ADDRESS() {
        return COMM_ADDRESS;
    }

    public void setCOMM_ADDRESS(String COMM_ADDRESS) {
        this.COMM_ADDRESS = COMM_ADDRESS;
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

    public String getTERMINAL_ADDRESS() {
        return TERMINAL_ADDRESS;
    }

    public void setTERMINAL_ADDRESS(String TERMINAL_ADDRESS) {
        this.TERMINAL_ADDRESS = TERMINAL_ADDRESS;
    }

    public String getAREANAME() {
        return AREANAME;
    }

    public void setAREANAME(String AREANAME) {
        this.AREANAME = AREANAME;
    }

}
