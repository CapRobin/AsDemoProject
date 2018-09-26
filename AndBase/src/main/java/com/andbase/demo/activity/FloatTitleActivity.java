package com.andbase.demo.activity;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.andbase.demo.R;
import com.andbase.library.app.base.AbBaseActivity;
import com.andbase.library.view.listener.AbOnScrollListener;
import com.andbase.library.view.sample.AbScrollView;


public class FloatTitleActivity extends AbBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_float_title);

        final AbScrollView scrollView =  (AbScrollView)findViewById(R.id.index_scroll);
        final LinearLayout top_bar =  (LinearLayout)findViewById(R.id.top_bar);

        //背景不显示
        top_bar.getBackground().setAlpha(0);

        final  RelativeLayout.LayoutParams layoutParams =  (RelativeLayout.LayoutParams)top_bar.getLayoutParams();
        final int topMargin = layoutParams.topMargin;

        final RelativeLayout searchLayout = (RelativeLayout)findViewById(R.id.search_layout);

        scrollView.setOnScrollListener(new AbOnScrollListener() {

            @Override
            public void onScrollPosition(int position) {

            }

            @Override
            public void onScrollY(int scrollY) {


                if(scrollY == 0){
                    //恢复位置
                    layoutParams.topMargin = topMargin;
                    top_bar.getBackground().setAlpha(0);
                    searchLayout.setBackgroundResource(R.drawable.bg_rect_white_translucent);

                }else if(scrollY > topMargin){
                    //到达顶部
                    layoutParams.topMargin = 0;
                    //保留点透明  最大255
                    top_bar.getBackground().setAlpha(255);
                    searchLayout.setBackgroundResource(R.drawable.bg_rect_white);
                    searchLayout.getBackground().setAlpha(255);


                }else{
                    //中间区域
                    layoutParams.topMargin = topMargin-scrollY;
                    int alpha = 255 - (int) (layoutParams.topMargin * (255/topMargin));
                    top_bar.getBackground().setAlpha(alpha);

                }
                top_bar.setLayoutParams(layoutParams);
            }
        });
    }

}
