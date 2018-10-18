package com.demo.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.demo.R;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * Copyright © CapRobin
 * <p>
 * Name：SettingActivity
 * Describe：系统设置
 * Date：2018-09-10 11:14:30
 * Author: CapRobin@yeah.net
 */
@ContentView(R.layout.activity_setting)
public class SettingActivity extends BaseActivity {

    @ViewInject(R.id.title_back_igv)
    private ImageView title_back_igv;
    @ViewInject(R.id.title_tv)
    private TextView title_tv;
    @ViewInject(R.id.title_right_igv)
    private ImageView title_right_igv;

    @ViewInject(R.id.about_us)
    private RelativeLayout about_us;
    @ViewInject(R.id.reportus)
    private RelativeLayout reportus;
    @ViewInject(R.id.officialnews)
    private RelativeLayout officialnews;
    @ViewInject(R.id.check_update)
    private RelativeLayout check_update;
    @ViewInject(R.id.help)
    private RelativeLayout help;
    @ViewInject(R.id.exit_user_login)
    private Button exit_user_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        initView();
    }

    /**
     * Describe：初始化配置
     * Params:
     * Return:
     * Date：2018-09-10 11:15:21
     */
    private void initView() {
        title_back_igv.setVisibility(View.VISIBLE);
        title_tv.setText(getResources().getString(R.string.sys_setting_title));

    }

    @Event(value = {R.id.title_back_igv,R.id.about_us,R.id.reportus,R.id.officialnews,R.id.check_update,R.id.help,R.id.exit_user_login})
    private void click(View view){
        switch (view.getId()){
            case R.id.title_back_igv:
                finish();
                break;
            case R.id.about_us:
//                startActivity(new Intent(mContext,AboutUsActivity.class));
                break;
            case R.id.reportus:
                break;
            case R.id.officialnews:
                break;
            case R.id.check_update:
                break;
            case R.id.help:
                break;
            case R.id.exit_user_login:
                dialogExit(mContext);
                break;
            default:
                break;
        }
    }


}
