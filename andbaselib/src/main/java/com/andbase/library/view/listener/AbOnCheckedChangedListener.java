package com.andbase.library.view.listener;

import android.widget.CompoundButton;

/**
 * Copyright amsoft.cn
 * Author 还如一梦中
 * Date 2016/6/26 10:54
 * Email 396196516@qq.com
 * Info 自定义tab点击回调接口
 */
public abstract class AbOnCheckedChangedListener {

    public void onCheckedChanged(int position, boolean isChecked){};
    public void onCheckedChanged(CompoundButton buttonView,boolean isChecked){};
}
