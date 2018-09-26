package com.andbase.library.asynctask;

/**
 * Copyright amsoft.cn
 * Author 还如一梦中
 * Date 2016/6/14 17:54
 * Email 396196516@qq.com
 * Info 数据监听器
 */
public class AbTaskListener {

	/**
	 * 执行开始.
	 * 
	 * @return 返回的结果对象
	 */
	public void get() {
	};

	/**
	 * 执行开始后调用.
	 * */
	public void update() {
	};

	/**
	 * 监听进度变化.
	 * 
	 * @param values the values
	 */
	public void onProgressUpdate(Integer... values) {
	};

}
