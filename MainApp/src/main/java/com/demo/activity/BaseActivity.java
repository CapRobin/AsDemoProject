package com.demo.activity;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.demo.R;
import com.demo.utils.Loading;
import com.demo.utils.Preferences;

import org.xutils.DbManager;
import org.xutils.view.annotation.ContentView;
import org.xutils.x;

/**
 * Copyright © CapRobin
 * <p>
 * Name：BaseActivity
 * Describe：程序基类
 * Date：2018-08-28 18:33:59
 * Author: CapRobin@yeah.net
 */
@ContentView(R.layout.activity_base)
public class BaseActivity extends FragmentActivity {
    public BaseActivity mContext;
    public Loading loading;
    private Preferences preference;
    private long lastTiem = 0;
    protected MyBroadcast myBroadcast;
    protected int screenHeight = 0;
    protected int screenWidth = 0;
    protected static DbManager db;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    Toast.makeText(mContext, msg.getData().getString("Msg"), Toast.LENGTH_SHORT).show();
                    break;
                case 1:

                    break;
                case 2:

                    break;
                default:
                    break;
            }
        }

        ;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        mContext = this;
        loading = new Loading(mContext);
        preference = Preferences.instance(this);
        //动态注册广播退出程序
        myBroadcast = new MyBroadcast();
        IntentFilter intentFilter = new IntentFilter("exitApp");
        registerReceiver(myBroadcast, intentFilter);
    }

    /**
     * Describe：Toast提示
     * Params:
     * Return:
     * Date：2018-07-27 12:49:22
     */
    public void setToast(String hintInfo) {
        Toast.makeText(this, hintInfo, Toast.LENGTH_LONG).show();
    }

    /**
     * Copyright © 2018 LJNG All rights reserved.
     * <p>
     * Name：BasicActivity
     * Describe：定义一个广播用于程序退出使用
     * Date：2018-04-23 16:54:07
     * Author: CapRobin@yeah.net
     */
    public class MyBroadcast extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            int closeId = intent.getIntExtra("closeFlag", 0);
            if (closeId == 1) {
                finish();
            }
        }
    }

    /**
     * Describe：退出程序方法
     * Params:
     * Date：2018-04-23 16:58:29
     */
    protected void exitApp(int flag) {
        switch (flag) {
            //系统首页退出
            case 0:
                if (System.currentTimeMillis() - lastTiem > 2000) {
                    lastTiem = System.currentTimeMillis();
                    setToast(getResources().getString(R.string.exit_notice));
                } else {
                    Intent intent = new Intent("exitApp");
                    intent.putExtra("closeFlag", 1);
                    sendBroadcast(intent);
                    setToast(getResources().getString(R.string.exit_ok));
                }
                break;
            //设置页面退出
            case 1:
                Intent intent = new Intent("exitApp");
                intent.putExtra("closeFlag", 1);
                sendBroadcast(intent);
                setToast(getResources().getString(R.string.exit_ok));
                break;
        }
    }

    /**
     * Describe：发送消息刷新UI
     * Params:
     * Date：2018-04-03 17:12:37
     */
    public void sendMsgUpdateUI(int what, String titleMsg) {
        Message msg = mHandler.obtainMessage(what);
        Bundle bundle = new Bundle();
        bundle.putString("Msg", titleMsg);
        msg.setData(bundle);
        mHandler.sendMessage(msg);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(myBroadcast);
    }
}
