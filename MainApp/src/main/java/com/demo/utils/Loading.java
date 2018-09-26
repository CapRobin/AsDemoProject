package com.demo.utils;

import android.app.Dialog;
import android.content.Context;
import android.widget.TextView;

import com.demo.R;

/**
 * Copyright © CapRobin
 *
 * Name：Loading
 * Describe：加载等待框
 * Date：2018-08-31 09:05:34
 * Author: CapRobin@yeah.net
 *
 */
public class Loading extends Dialog {
    private TextView loadText;
    public Loading(Context context) {
        super(context, R.style.Dialog);
        setContentView(R.layout.loading_view);
        setCanceledOnTouchOutside(false);
        loadText = (TextView)findViewById(R.id.loading_tv);
    }

    public void setDialogLable(String lableText) {
        loadText.setText(lableText);
    }
}
