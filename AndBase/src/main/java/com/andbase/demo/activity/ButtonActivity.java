package com.andbase.demo.activity;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.andbase.demo.R;
import com.andbase.library.app.base.AbBaseActivity;
import com.andbase.library.util.AbDialogUtil;

public class ButtonActivity extends AbBaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_button);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setTitle(R.string.title_button);
        toolbar.setContentInsetsRelative(0, 0);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Button orangeBtn = (Button)this.findViewById(R.id.orange_btn);
        orangeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AbDialogUtil.showProgressDialog(ButtonActivity.this,-1,"加载中...");

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        AbDialogUtil.removeDialog(ButtonActivity.this);
                    }
                },1000);
            }
        });

    }


}

