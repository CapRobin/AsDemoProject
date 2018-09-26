
package com.andbase.library.http.model;

import com.andbase.library.config.AbAppConfig;
import com.andbase.library.util.AbStrUtil;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.HttpHostConnectException;

import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

/**
 * Copyright amsoft.cn
 * Author 还如一梦中
 * Date 2016/6/14 17:54
 * Email 396196516@qq.com
 * Info 异常类
 */

public class AbHttpException extends Exception {

	/** 异常码. */
	private int code = -1;
	/** 异常消息. */
	private String message = null;

	/**
	 * 构造异常类.
	 * @param e 异常
	 */
	public AbHttpException(Exception e) {
		super();

		if( e instanceof HttpHostConnectException) {
			code = AbHttpStatus.CONNECT_FAILURE_CODE;
			message = AbAppConfig.UNKNOWN_HOST_EXCEPTION;
		}else if (e instanceof UnknownHostException) {
			code = AbHttpStatus.CONNECT_FAILURE_CODE;
			message = AbAppConfig.UNKNOWN_HOST_EXCEPTION;
		}
		else if (e instanceof ConnectException || e instanceof SocketException) {
			code = AbHttpStatus.CONNECT_FAILURE_CODE;
			message = AbAppConfig.CONNECT_EXCEPTION;
		}else if (e instanceof ConnectTimeoutException || e instanceof SocketTimeoutException) {
			code = AbHttpStatus.CONNECT_TIMEOUT_CODE;
			message = AbAppConfig.CONNECT_TIMEOUT_EXCEPTION;
		}
		else if( e instanceof ClientProtocolException) {
			code = AbHttpStatus.PROTOCOL_FAILURE_CODE;
			message = AbAppConfig.CLIENT_PROTOCOL_EXCEPTION;
		}
		else if( e instanceof NullPointerException) {
			code = AbHttpStatus.PROGRAM_FAILURE_CODE;
			message = AbAppConfig.REMOTE_SERVICE_EXCEPTION;
		}
		else {
            code = AbHttpStatus.UNTREATED_CODE;
			if (e == null || AbStrUtil.isEmpty(e.getMessage())) {
				message = AbAppConfig.REMOTE_SERVICE_EXCEPTION;
			}else{
				message = e.getMessage();
			}
		}

	}

	/**
	 * 用一个消息构造异常类.
	 * @param code
	 * @param message 异常的消息
	 */
	public AbHttpException(int code, String message) {
		super(message);
		this.code = code;
		this.message = message;
	}

	/**
	 * 获取异常信息.
	 * @return the message
	 */
	@Override
	public String getMessage() {
		return message;
	}

	/**
	 * 获取异常码.
	 * @return the code
	 */
	public int getCode() {
		return code;
	}
}
