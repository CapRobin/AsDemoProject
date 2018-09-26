package com.andbase.library.model;

/**
 * Copyright amsoft.cn
 * Author 还如一梦中
 * Date 2016/6/14 17:54
 * Email 396196516@qq.com
 * Info CPU信息
 */

public class AbCPUInfo {

	public String User;

	public String System;

	public String IOW;

	public String IRQ;

	public AbCPUInfo() {
		super();
	}

	public AbCPUInfo(String user, String system, String iOW, String iRQ) {
		super();
		User = user;
		System = system;
		IOW = iOW;
		IRQ = iRQ;
	}

}
