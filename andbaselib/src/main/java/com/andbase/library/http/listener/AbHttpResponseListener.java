package com.andbase.library.http.listener;

import android.os.Handler;
import android.os.Message;
import com.andbase.library.http.AbHttpUtil;


/**
 * Copyright amsoft.cn
 * Author 还如一梦中
 * Date 2016/6/14 17:54
 * Email 396196516@qq.com
 * Info 通用Http响应监听器  默认返回Object类型
 */
public abstract class AbHttpResponseListener {
	
    /** The Handler. */
    private Handler handler;
    
    /**
     * 空构造函数.
     */
    public AbHttpResponseListener() {
		super();
	}

	/**
	 * 获取数据开始.
	 */
    public abstract void onStart();
    
    
    /**
	 * 完成后调用.
	 */
    public abstract void onFinish();

    /**
     * 获取数据成功.
     *
     * @param statusCode the status code
     * @param content the content
     */
    public void onSuccess(int statusCode,Object content){};

    
    /**
     * 获取数据失败.
     *
     * @param statusCode the status code
     * @param content the content
     * @param error the error
     */
    public abstract void onFailure(int statusCode, String content,Throwable error);
    
    /**
     * 显示进度.
     *
     * @param bytesWritten the bytes written
     * @param totalSize the total size
     */
    public void onProgress(long bytesWritten, long totalSize) {}

    
    /**
     * 开始消息.
     */
    public void sendStartMessage(){
    	sendMessage(obtainMessage(AbHttpUtil.START_MESSAGE, null));
    }
    
    /**
     * 进度消息.
     *
     * @param bytesWritten the bytes written
     * @param totalSize the total size
     */
    public void sendProgressMessage(long bytesWritten, long totalSize) {
        sendMessage(obtainMessage(AbHttpUtil.PROGRESS_MESSAGE, new Object[]{bytesWritten, totalSize}));
    }

    /**
     * 成功消息.
     *
     * @param statusCode the status code
     * @param content the content
     */
    public void sendSuccessMessage(int statusCode,Object content){
        sendMessage(obtainMessage(AbHttpUtil.SUCCESS_MESSAGE, new Object[]{statusCode, content}));
    }
    
    /**
     * 失败消息.
     *
     * @param statusCode the status code
     * @param content the content
     * @param error the error
     */
    public void sendFailureMessage(int statusCode,String content,Throwable error){
    	sendMessage(obtainMessage(AbHttpUtil.FAILURE_MESSAGE, new Object[]{statusCode,content, error}));
    }
    
    /**
     * 发送消息.
     * @param msg the msg
     */
    public void sendMessage(Message msg) {
        if (msg != null) {
        	msg.sendToTarget();
        }
    }
    
    /**
     * 构造Message.
     * @param responseMessage the response message
     * @param response the response
     * @return the message
     */
    protected Message obtainMessage(int responseMessage, Object response) {
        Message msg;
        if (handler != null) {
            msg = handler.obtainMessage(responseMessage, response);
        } else {
            msg = Message.obtain();
            if (msg != null) {
                msg.what = responseMessage;
                msg.obj = response;
            }
        }
        return msg;
    }

	/**
	 * 获取Handler.
	 * @return the handler
	 */
	public Handler getHandler() {
		return handler;
	}

	/**
	 * 设置Handler.
	 * @param handler the new handler
	 */
	public void setHandler(Handler handler) {
		this.handler = handler;
	}

}
