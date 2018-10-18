package com.demo.activity;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.demo.R;
import com.demo.adapter.MenuListAdapter;
import com.demo.constant.Constant;
import com.demo.entity.ListMenu;
import com.demo.utils.Preferences;
import com.donkingliang.banner.CustomBanner;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;


/**
 * Copyright © CapRobin
 * <p>
 * Name：MainActivity
 * Describe：程序首页
 * Date：2018-08-28 18:34:12
 * Author: CapRobin@yeah.net
 */
@ContentView(R.layout.activity_main)
public class MainActivity extends BaseActivity {
    //列表Item图标
    //private int[] icons = null;
    //列表Item标题
    private String[] titles = null;
    //列表Item介绍
    private String[] intros = null;
    private List<ListMenu> menuList;

    @ViewInject(R.id.title_back_igv)
    private ImageView title_back_igv;
    @ViewInject(R.id.title_tv)
    private TextView title_tv;
    @ViewInject(R.id.title_right_igv)
    private ImageView title_right_igv;
    @ViewInject(R.id.itemMenuList)
    private ListView itemMenuList;
    @ViewInject(R.id.banner)
    private CustomBanner banner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        //初始化配置
        initView();
    }

    /**
     * Describe：初始化配置
     * Params:
     * Return:
     * Date：2018-09-25 15:50:22
     */
    private void initView() {
        //设置登录成功标志(非第一次登录)
        Preferences.mPreferences.putBoolean(Constant.ISFIRSTLOGIN,true);
        title_right_igv.setVisibility(View.VISIBLE);
        title_tv.setText("Android案例");
        //首页轮播设置
        intiViewBanner();
        //设置案例列表
        setCaseItem();
    }

    /**
     * Describe：点击事件处理
     * Params:
     * Return:
     * Date：2018-09-26 11:27:58
     */
    @Event(value = {R.id.title_right_igv})
    private void Click(View v) {
        switch (v.getId()) {
            case R.id.title_right_igv:
                startActivity(new Intent(mContext, SettingActivity.class));
                break;
            case 2:
                break;
            case 3:
                break;
            default:
                break;
        }
    }


    /**
     * Describe：首页轮播设置
     * Params:
     * Date：2018-04-11 10:52:16
     */
    private void intiViewBanner() {
        banner = (CustomBanner) findViewById(R.id.banner);
        ArrayList<String> images = new ArrayList<>();
//        images.add(getUriFromDrawableRes(this, R.drawable.banner_01).toString());
        images.add(getUriFromDrawableRes(this, R.mipmap.banner_01).toString());
        images.add(getUriFromDrawableRes(this, R.mipmap.banner_02).toString());
        images.add(getUriFromDrawableRes(this, R.mipmap.banner_03).toString());
        images.add(getUriFromDrawableRes(this, R.mipmap.banner_04).toString());
        images.add(getUriFromDrawableRes(this, R.mipmap.banner_05).toString());
        images.add(getUriFromDrawableRes(this, R.mipmap.banner_06).toString());
        images.add(getUriFromDrawableRes(this, R.mipmap.banner_07).toString());

        banner.setPages(new CustomBanner.ViewCreator<String>() {
            @Override
            public View createView(Context context, int position) {
                ImageView imageView = new ImageView(context);
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                return imageView;
            }

            @Override
            public void updateUI(Context context, View view, int position, String entity) {
                Glide.with(context).load(entity).into((ImageView) view);
            }
        }, images)
//                //设置指示器为普通指示器
//                .setIndicatorStyle(CustomBanner.IndicatorStyle.ORDINARY)
//                //设置两个点图片作为翻页指示器，不设置则没有指示器，可以根据自己需求自行配合自己的指示器,不需要圆点指示器可用不设
//                .setIndicatorRes(R.drawable.shape_point_select, R.drawable.shape_point_unselect)
//                //设置指示器的方向
                .setIndicatorGravity(CustomBanner.IndicatorGravity.RIGHT)
//                //设置指示器的指示点间隔
//                .setIndicatorInterval(20)
                //设置自动翻页
                .startTurning(5000);
    }

    /**
     * Describe：获取Drawable文件的URL
     * Params:
     * Date：2018-04-11 11:01:11
     */
    public Uri getUriFromDrawableRes(Context context, int id) {
        Resources resources = context.getResources();
        String path = ContentResolver.SCHEME_ANDROID_RESOURCE + "://"
                + resources.getResourcePackageName(id) + "/"
                + resources.getResourceTypeName(id) + "/"
                + resources.getResourceEntryName(id);
        return Uri.parse(path);
    }

    /**
     * Describe：设置案例项目
     * Params:
     * Return:
     * Date：2018-09-26 11:24:43
     */
    private void setCaseItem() {
        //列表Item图标
        //icons = new int[]{R.mipmap.ic_launcher};
        //列表Item标题
        titles = new String[]{
                "XUtils3数据库使用",
                "接口测试",
                "功能项03",
                "功能项04",
                "功能项05"
        };
        //列表Item介绍
        intros = new String[]{"案例说明"};
        menuList = new ArrayList<ListMenu>();

        //循环加载列表项目
        for (int i = 0; i < titles.length; i++) {
            ListMenu menu = new ListMenu();
//            menu.setIcon(icons[0]);
            menu.setTitle(titles[i]);
            menu.setIntro(intros[0]);
            menuList.add(menu);
        }
        MenuListAdapter menuListAdapter = new MenuListAdapter(this, menuList, itemMenuList);
        itemMenuList.setAdapter(menuListAdapter);
        itemMenuList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = null;
                switch (position) {
                    case 0:
                        intent = new Intent(mContext, XutilDbActivity.class);
                        break;
                    case 1:
                        intent = new Intent(mContext, InterfaceTestActivity.class);
                        break;
                    case 2:
                        Toast.makeText(MainActivity.this, menuList.get(position).getTitle(), Toast.LENGTH_LONG).show();
                        break;
                    case 3:
                        Toast.makeText(MainActivity.this, menuList.get(position).getTitle(), Toast.LENGTH_LONG).show();
                        break;
                    case 4:
                        Toast.makeText(MainActivity.this, menuList.get(position).getTitle(), Toast.LENGTH_LONG).show();
                        break;
                }
                startActivity(intent);
            }
        });
    }

    /**
     * Describe：退出系统监听
     * Params:
     * Date：2018-04-23 17:50:11
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == event.KEYCODE_BACK) {
            exitApp(0);
        }
        return true;
    }
}

