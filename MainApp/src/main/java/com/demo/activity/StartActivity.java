package com.demo.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.util.DisplayMetrics;
import android.util.Log;

import com.demo.R;
import com.demo.constant.Constant;
import com.demo.utils.PermissionsChecker;
import com.demo.utils.Preferences;

import org.xutils.DbManager;
import org.xutils.db.table.TableEntity;
import org.xutils.view.annotation.ContentView;
import org.xutils.x;

import java.io.File;
import java.util.List;

/**
 * Copyright © CapRobin
 * <p>
 * Name：StartActivity
 * Describe：APP启动页面
 * Date：2018-07-26 08:58:02
 * Author: CapRobin@yeah.net
 */
@ContentView(R.layout.activity_start)
public class StartActivity extends BaseActivity {

    private Context mContext;
    // 权限检测器
    private PermissionsChecker mPermissionsChecker;
    // 权限请求码
    private static final int REQUEST_CODE = 0;

    // 所需的全部权限(此处获取SD看读写权限)
    static final String[] PERMISSIONS = new String[]{
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.MODIFY_AUDIO_SETTINGS,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CALL_PHONE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        mContext = this;
        initView();
    }

    /**
     * Describe：初始化配置
     * Params:
     * Return:
     * Date：2018-08-31 15:54:52
     */
    private void initView() {
        //获取屏幕的宽高度
        DisplayMetrics dm = getResources().getDisplayMetrics();
        screenHeight = dm.heightPixels;
        screenWidth = dm.widthPixels;
        //权限检测
        mPermissionsChecker = new PermissionsChecker(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 缺少权限时, 进入权限配置页面
        if (mPermissionsChecker.lacksPermissions(PERMISSIONS)) {
            startPermissionsActivity();
        } else {
            dbSeting();
            loadSystemProperty();
        }
    }


    /**
     * Describe：加载数据库配置
     * Params:
     * Return: 
     * Date：2018-09-22 16:16:54
     */
    
    private void dbSeting() {
        /**
         * 初始化DaoConfig配置
         */
        DbManager.DaoConfig daoConfig = new DbManager.DaoConfig()
                //设置数据库名，默认xutils.db
                .setDbName("myapp.db")
                //设置数据库路径，默认存储在app的私有目录
                .setDbDir(new File("/mnt/sdcard/"))
                //设置数据库的版本号
                .setDbVersion(2)
                //设置数据库打开的监听
                .setDbOpenListener(new DbManager.DbOpenListener() {
                    @Override
                    public void onDbOpened(DbManager db) {
                        //开启数据库支持多线程操作，提升性能，对写入加速提升巨大
                        db.getDatabase().enableWriteAheadLogging();
                    }
                })
                //设置数据库更新的监听
                .setDbUpgradeListener(new DbManager.DbUpgradeListener() {
                    @Override
                    public void onUpgrade(DbManager db, int oldVersion, int newVersion) {
                    }
                })
                //设置表创建的监听
                .setTableCreateListener(new DbManager.TableCreateListener() {
                    @Override
                    public void onTableCreated(DbManager db, TableEntity<?> table){
                        Log.i("JAVA", "onTableCreated：" + table.getName());
                    }
                });
        //设置是否允许事务，默认true
        //.setAllowTransaction(true)

        db = x.getDb(daoConfig);
    }

    private void startPermissionsActivity() {
        PermissionsActivity.startActivityForResult(this, REQUEST_CODE, PERMISSIONS);
    }

    /**
     * Describe：检查网络状态
     * Params:
     * Date：2018-03-27 08:33:46
     */

    private void loadSystemProperty() {
        startHandler(1000);
    }

    /**
     * Describe：加载相关数据
     * Params:
     * Return:
     * Date：2018-07-26 10:11:57
     */
    private void loadData() {
        //加载相关数据
        toMainActivity();
    }

    private void startHandler(int time) {
        Handler handle = new Handler();
        handle.postDelayed(new Runnable() {
            @Override
            public void run() {
                toMainActivity();
            }
        }, time);
    }


    /**
     * Describe：跳转至首页
     * Params:
     * Return:
     * Date：2018-07-26 10:15:08
     */
    private void toMainActivity() {
        if (!Preferences.mPreferences.getBoolean(Constant.ISFIRSTLOGIN)) {
            startActivity(new Intent(mContext, WelcomeActivity.class));
        } else {
            startActivity(new Intent(mContext, LoginActivity.class));
        }
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 拒绝时, 关闭页面, 缺少主要权限, 无法运行
        if (requestCode == REQUEST_CODE && resultCode == PermissionsActivity.PERMISSIONS_DENIED) {
            finish();
        }
    }
}
