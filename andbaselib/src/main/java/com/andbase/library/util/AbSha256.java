package com.andbase.library.util;

import org.apache.commons.codec.binary.Hex;

import java.security.MessageDigest;

/**
 * Copyright amsoft.cn
 * Author 还如一梦中
 * Date 2016/6/14 17:54
 * Email 396196516@qq.com
 * Info sha256工具类
 */
public class AbSha256 {

    /**
     * sha256 加密
     */
    public static String sha256(String text) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(text.getBytes("UTF-8"));
            return new String(Hex.encodeHex(hash));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
