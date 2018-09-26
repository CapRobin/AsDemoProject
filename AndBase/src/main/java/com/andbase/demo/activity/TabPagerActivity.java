package com.andbase.demo.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.andbase.demo.R;
import com.andbase.demo.fragment.TestFragment;
import com.andbase.library.app.base.AbBaseActivity;
import com.andbase.library.view.tabpager.AbTabPagerView;

import java.util.ArrayList;
import java.util.List;


public class TabPagerActivity extends AbBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_top);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setTitle(R.string.title_tab_title);
        toolbar.setContentInsetsRelative(0, 0);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        AbTabPagerView tabPagerView = (AbTabPagerView)findViewById(R.id.tabPagerView);

        //TabLayout.MODE_FIXED,TabLayout.MODE_SCROLLABLE
        tabPagerView.setTabMode(TabLayout.MODE_FIXED);
        //缓存数量
        tabPagerView.getViewPager().setOffscreenPageLimit(1);

        TestFragment page1 = new TestFragment();
        TestFragment page2 = new TestFragment();


        List<Fragment> fragmentList = new ArrayList<Fragment>();
        fragmentList.add(page1);
        fragmentList.add(page2);

        String[] tabTexts = new String[]{
                "全部","AndBase"
        };

        tabPagerView.setTabTextSize(18);

        tabPagerView.setTabBackgroundResource(android.R.color.white);

        tabPagerView.setTabs(tabTexts,fragmentList);

        //tabPagerView.getTabLayout().setVisibility(View.GONE);

    }


}
