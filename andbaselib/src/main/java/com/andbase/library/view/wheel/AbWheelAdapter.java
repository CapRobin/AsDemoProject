package com.andbase.library.view.wheel;


/**
 * Copyright amsoft.cn
 * Author 还如一梦中
 * Date 2016/6/14 17:54
 * Email 396196516@qq.com
 * Info 轮子适配器接口
 */
public interface AbWheelAdapter {
	
	/**
	 * 获取条目数量.
	 *
	 */
	public int getItemsCount();
	
	/**
	 * 获取条目的值.
	 * @param index 索引
	 */
	public String getItem(int index);
	
	/**
	 * 获取条目的最大字符长度，中文表示2个.
	 */
	public int getMaximumLength();
}
