package com.andbase.library.http.model;

import com.andbase.library.util.AbJsonUtil;
import com.andbase.library.util.AbStrUtil;

/**
 * Copyright amsoft.cn
 * Author 还如一梦中
 * Date 2016/6/14 17:54
 * Email 396196516@qq.com
 * Info 结果
 */

public class AbResult {
	
	/** 返回码：成功. */
    public static final int RESULT_OK = 0;
    
    /** 返回码：失败. */
    public static final int RESULT_ERROR = -1;

	/** 结果code. */
	private int resultCode;
	
	/** 结果 message. */
	private String resultMessage;

    /** 其他object 1. */
    private Object object1;

    /** 其他object 2. */
    private Object object2;
	
    /**
     * 构造
     */
	public AbResult() {
		super();
	}

	/**
	 * 构造
	 * @param resultCode
	 * @param resultMessage
	 */
	public AbResult(int resultCode, String resultMessage) {
		super();
		this.resultCode = resultCode;
		this.resultMessage = resultMessage;
	}
	
	/**
	 * 用json构造自己
	 * @param json
	 */
	public AbResult(String json) {
		super();
		try{
			if(!AbStrUtil.isEmpty(json)){
				AbResult result = AbJsonUtil.fromJson(json, AbResult.class);
				this.resultCode = result.getResultCode();
				this.resultMessage = result.getResultMessage();
			}else{
				this.resultCode = AbResult.RESULT_ERROR;
				this.resultMessage = "这是接口问题，服务器200，但返回结果为空，不应该出现的错误";
			}
		}catch(Exception e){
			this.resultCode = AbResult.RESULT_ERROR;
			this.resultMessage = "这是接口问题，服务器200，但返回结果格式错误:"+e.getMessage();
		}



	}

	/**
	 * 获取返回码.
	 *
	 * @return the result code
	 */
	public int getResultCode() {
		return resultCode;
	}

	/**
	 * 设置返回码.
	 *
	 * @param resultCode the new result code
	 */
	public void setResultCode(int resultCode) {
		this.resultCode = resultCode;
	}

	/**
	 * 获取返回信息.
	 *
	 * @return the result message
	 */
	public String getResultMessage() {
		return resultMessage;
	}

	/**
	 * 设置返回信息.
	 *
	 * @param resultMessage the new result message
	 */
	public void setResultMessage(String resultMessage) {
		this.resultMessage = resultMessage;
	}
	
	/**
	 * 
	 * 转换成json.
	 * @return
	 */
	public String toJson(){
		return AbJsonUtil.toJson(this);
	}

    /**
     *
     * 获取返回对象1.
     * @return
     */
    public Object getObject1() {
        return object1;
    }

    /**
     *
     * 设置返回对象1.
     * @return
     */
    public void setObject1(Object object1) {
        this.object1 = object1;
    }

    /**
     *
     * 获取返回对象1.
     * @return
     */
    public Object getObject2() {
        return object2;
    }

    /**
     *
     * 设置返回对象2.
     * @return
     */
    public void setObject2(Object object2) {
        this.object2 = object2;
    }
}
