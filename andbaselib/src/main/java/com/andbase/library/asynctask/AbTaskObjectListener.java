package com.andbase.library.asynctask;


/**
 * Copyright amsoft.cn
 * Author 还如一梦中
 * Date 2016/6/14 17:54
 * Email 396196516@qq.com
 * Info 数据监听器
 */
public abstract class AbTaskObjectListener extends AbTaskListener{
	
	/**
	 * 执行开始
	 * @return 返回的结果对象
	 */
    public abstract <T extends Object> T getObject();
    
    /**
     * 执行开始后调用.
     * @param obj
     */
    public abstract <T extends Object> void update(T obj); 
    
	
}
