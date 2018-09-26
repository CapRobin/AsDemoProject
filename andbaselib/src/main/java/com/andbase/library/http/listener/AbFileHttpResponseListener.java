package com.andbase.library.http.listener;

import java.io.File;
import android.content.Context;
import com.andbase.library.http.AbHttpUtil;
import com.andbase.library.util.AbFileUtil;


/**
 * Copyright amsoft.cn
 * Author 还如一梦中
 * Date 2016/6/14 17:54
 * Email 396196516@qq.com
 * Info Http响应监听器，返回文件
 */
public abstract class AbFileHttpResponseListener extends AbHttpResponseListener {

    /** 当前下载的文件. */
    private File file;

	/**
	 * 默认的构造.
	 */
	public AbFileHttpResponseListener() {
		super();
	}

	/**
     * 下载文件的构造,指定文件名称.
     * @param file 文件名称
     */
    public AbFileHttpResponseListener(File file) {
        super();
	    this.file = file;
    }
	
	/**
	 * 下载文件成功.
	 *
	 * @param statusCode the status code
	 * @param file the file
	 */
    public abstract void onSuccess(int statusCode,File file);

   
   /**
    * 成功消息.
    * @param statusCode the status code
    */
    public void sendSuccessMessage(int statusCode){
    	sendMessage(obtainMessage(AbHttpUtil.SUCCESS_MESSAGE, new Object[]{statusCode}));
    }
    
    /**
     * 失败消息.
     * @param statusCode the status code
     * @param error the error
     */
    public void sendFailureMessage(int statusCode,Throwable error){
    	sendMessage(obtainMessage(AbHttpUtil.FAILURE_MESSAGE, new Object[]{statusCode, error}));
    }
    

	/**
	 * 获取文件.
	 * @return the file
	 */
	public File getFile() {
		return file;
	}

	/**
	 * 设置文件.
	 * @param file the new file
	 */
	private void setFile(File file) {
		this.file = file;
		try {
			if(!file.getParentFile().exists()){
			      file.getParentFile().mkdirs();
			}
			file.deleteOnExit();
			file.createNewFile();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	/**
	 * 设置文件.
	 * @param context the context
	 * @param name the name
	 */
	public String setFile(Context context,String name) {
		//生成缓存文件
        if(AbFileUtil.isCanUseSD()){
	    	File file = new File(AbFileUtil.getFileDownloadDir(context) + name);
	    	setFile(file);
			return file.getPath();
        }
		return null;

	}
    
}
