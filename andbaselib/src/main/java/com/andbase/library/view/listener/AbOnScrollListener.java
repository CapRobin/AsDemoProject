package com.andbase.library.view.listener;

/**
 * Copyright amsoft.cn
 * Author 还如一梦中
 * Date 2016/6/16 15:29
 * Email 396196516@qq.com
 * Info 滚动事件监听器
 */
public abstract  class AbOnScrollListener {

    /**
     * 滚动事件
     * @param position 位置
     */
    public void onScrollPosition(int position){};

    /**
     * 滚动事件
     * @param scrollY Y滚动的距离
     */
    public void onScrollY(int scrollY){};


}
