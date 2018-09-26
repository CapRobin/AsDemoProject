package com.andbase.library.http.model;


/**
 * Copyright amsoft.cn
 * Author 还如一梦中
 * Date 2016/6/14 17:54
 * Email 396196516@qq.com
 * Info HTTP状态码
 */

public class AbHttpStatus {
	
	/** 成功返回码. */
	public static final int SUCCESS_CODE = 200;

	/** 未找到访问地址. */
	public static final int UNFIND_CODE = 404;

	/** 服务出错的HTTP返回码. */
	public static final int SERVER_FAILURE_CODE = 500;
	
	/** 连接失败的HTTP返回码. */
	public static final int CONNECT_FAILURE_CODE = 600;
	
	/** 连接超时的HTTP返回码. */
	public static final int CONNECT_TIMEOUT_CODE = 601;
	
	/** 响应失败的HTTP返回码. */
	public static final int RESPONSE_TIMEOUT_CODE = 602;

	/** 调用协议错误. */
	public static final int PROTOCOL_FAILURE_CODE = 603;

	/** 程序错误. */
	public static final int PROGRAM_FAILURE_CODE = 604;

	/** 线程池已满，拒绝访问. */
	public static final int THREAD_FAILURE_CODE = 605;
	
	/** 未处理的HTTP返回码. */
	public static final int UNTREATED_CODE = 900;

   
}
