package com.andbase.library.cache.http;

import android.graphics.Bitmap;

import com.andbase.library.http.model.AbHttpStatus;

import java.util.Collections;
import java.util.Map;

/**
 * Copyright amsoft.cn
 * Author 还如一梦中
 * Date 2016/6/14 17:54
 * Email 396196516@qq.com
 * Info 响应实体类
 */
public class AbHttpCacheResponse {
	
	/** 响应码. */
	public final int statusCode;

	/** 响应数据. */
	public final byte[] data;

	/** 响应头. */
	public final Map<String, String> headers;

	/**
	 * 构造.
	 *
	 * @param statusCode the status code
	 * @param data the data
	 * @param headers the headers
	 */
	public AbHttpCacheResponse(int statusCode, byte[] data,
                               Map<String, String> headers) {
		this.statusCode = statusCode;
		this.data = data;
		this.headers = headers;
	}

	/**
	 * 构造.
	 *
	 * @param data the data
	 */
	public AbHttpCacheResponse(byte[] data) {
		this(AbHttpStatus.SUCCESS_CODE, data, Collections.<String, String> emptyMap());
	}

	/**
	 * 构造.
	 *
	 * @param data the data
	 * @param headers the headers
	 */
	public AbHttpCacheResponse(byte[] data, Map<String, String> headers) {
		this(AbHttpStatus.SUCCESS_CODE, data, headers);
	}

}