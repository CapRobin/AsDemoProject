package com.usr.usrsimplebleassistent;

import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by Administrator on 2017-10-12.
 */

public class DltGas645 {
    public String sendStr;
    public String recvStr;
    public String addr;
    private int infoLen;//信息区长度
    private String MeterTypeCode = "30";//表类型
    private String ControlCode = "40";//控制码
    private ArrayList<String> funcCode;//功能码
    private ArrayList<String> cmd;//命令
    private ArrayList<String> data;//编程数据

    public DltGas645() {
        this.funcCode = new ArrayList<String>();
        this.cmd = new ArrayList<String>();
        this.data = new ArrayList<String>();

    }

    public DltGas645(String addr, String funcCode, String cmd, String data) {
        this.funcCode = new ArrayList<String>();
        this.cmd = new ArrayList<String>();
        this.data = new ArrayList<String>();
        this.addr = addr;
        this.funcCode.add(funcCode);
        this.cmd.add(cmd);
        this.data.add(data);
    }

    public String CreateFrame() {
        int len = 0;//数据区和信息区长度
        for (String f : funcCode) {
            len = len + f.length();
        }
        for (String f : cmd) {
            len = len + f.length();
        }
        for (String f : data) {
            len = len + f.length();
        }
        len = len + funcCode.size() * 2;
        String infoStr = "";//信息区
        for (int i = 0; i < funcCode.size(); i++) {
            int n = 0;
            if (funcCode.size() > i) {
                infoStr = infoStr + funcCode.get(i);//功能码
            }
            if (cmd.size() > i) {
                n = n + cmd.get(i).length();
            }
            if (data.size() > i) {
                n = n + data.get(i).length();
            }
            infoStr = infoStr + String.format("%02X", n / 2);//数据长度
            if (cmd.size() > i) {
                infoStr = infoStr + tzstrx(cmd.get(i));//命令
            }
            if (data.size() > i) {
                infoStr = infoStr + tzstrx(data.get(i));//编程数据
            }
        }
        this.sendStr = "68" + String.format("%02X", len / 2) + this.MeterTypeCode + tzstrx(addZero(this.addr, 12))
                + "68" + this.ControlCode + infoStr;
        this.sendStr = this.sendStr + this.Checksum(this.sendStr) + "16";
        return this.sendStr;
    }

    private String addZero(String code, int num) {
        String result = code;
        for (int i = 0; i < num - code.length(); i++) {
            result = "0" + result;
        }
        return result;
    }

    private static String tzstrx(String data) {
        byte[] bytes = data.getBytes();
        byte[] bytes1 = new byte[bytes.length];
        for (int i = 0; i < bytes.length / 2; i++) {
            bytes1[i * 2] = bytes[bytes.length - 1 - i * 2 - 1];
            bytes1[i * 2 + 1] = bytes[bytes.length - 1 - i * 2];
        }
        return new String(bytes1);
    }

    public static String Checksum(String data) {
        int total = 0;
        int len = data.length();
        int num = 0;
        while (num < len) {
            String s = data.substring(num, num + 2);
//            System.out.println(s);
            total += Integer.parseInt(s, 16);
            num = num + 2;
        }
        /**
         * 用256求余最大是255，即16进制的FF
         */
        int mod = total % 256;
        String hex = Integer.toHexString(mod);
        len = hex.length();
        //如果不够校验位的长度，补0,这里用的是两位校验
        if (len < 2) {
            hex = "0" + hex;
        }
        return hex.toUpperCase();
    }



    private static String reverbyte(String data) {
        byte[] bytes = data.getBytes();
        byte[] bytes1 = new byte[bytes.length];
        for (int i = 0; i < bytes.length / 2; i++) {
            bytes1[i * 2] = bytes[bytes.length - 1 - i * 2 - 1];
            bytes1[i * 2 + 1] = bytes[bytes.length - 1 - i * 2];
        }
        return new String(bytes1);
    }

    public  int AnalyseData(String recvStr, String addr)//处理接收回来的数据,发送的表号
    {
        if (recvStr.length() == 0)
            return -1;
        int i = recvStr.indexOf("68");
        if (i == -1) {
            return -1;
        }
        if (recvStr.length() <= 26) //至少大于26个字符
        {
            return -1;
        }
        recvStr = recvStr.substring(i, recvStr.length());// 68...68
        int len = Integer.parseInt(recvStr.substring(2, 4), 16) ;//信息区的长度
        if (recvStr.length() < (len * 2 + 26))// 收到的数据长度大于 信息区 + 固定帧的长度
        {
            return -1;//未接收完全
        }
        StringBuilder sb = new StringBuilder();
        sb.append(recvStr);
        if (sb.substring(0, 2).equals("68") == false || sb.substring(18, 20).equals("68") == false) {
            return 1;
        }
        if (sb.substring(6, 18).equals(tzstrx(addr))) {
            return 2;
        }
        int jywStart = 22 + len * 2;
        String jyw = sb.substring(jywStart,jywStart + 2);
        String jyStr = sb.substring(0,jywStart);
        Log.d("jyw",Checksum(jyStr));
        if (Checksum(jyStr).equals(jyw) != true) {
            return 3;
        }
        String endStr = sb.substring(jywStart + 2,jywStart + 4);
        if (endStr.equals("16") ==false)
        {
            return 4;
        }
        this.recvStr = sb.substring(0,jywStart + 4);
        return 0;
    }
}
