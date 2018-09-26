package com.andbase.library.view.listener;

/**
 * Copyright amsoft.cn
 * Author 还如一梦中
 * Date 2016/6/16 15:26
 * Email 396196516@qq.com
 * Info 选择事件监听器
 */
public abstract class AbOnItemSelectListener {

    /**
     * 被点击.
     * @param position the position
     */
    public void onSelect(int position){};

    /**
     * 被点击.
     * @param position1 the position
     * @param position2 the position
     */
    public void onSelect(int position1,int position2){};

}
