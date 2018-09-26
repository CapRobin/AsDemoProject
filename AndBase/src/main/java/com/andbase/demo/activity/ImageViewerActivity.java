package com.andbase.demo.activity;



import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.andbase.demo.R;
import com.andbase.library.app.base.AbBaseActivity;

import com.andbase.library.image.AbImageLoader;
import com.andbase.library.view.photo.AbPhotoImageViewPager;
import com.andbase.library.view.photo.AbPhotoImageViewPagerAdapter;

import java.util.List;


public class ImageViewerActivity extends AbBaseActivity {

    private AbPhotoImageViewPager photoImageViewPager = null;
    private AbPhotoImageViewPagerAdapter photoImageViewPagerAdapter = null;
    private List<String> urlPath = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置布局
        setContentView(R.layout.activity_image_viewer);

        urlPath = getIntent().getStringArrayListExtra("PATH");
        int position = getIntent().getIntExtra("POSITION",0);

        photoImageViewPager = (AbPhotoImageViewPager) findViewById(R.id.view_pager);
        photoImageViewPagerAdapter = new AbPhotoImageViewPagerAdapter(this,photoImageViewPager,urlPath,imageLoader);
        photoImageViewPager.setAdapter(photoImageViewPagerAdapter);

        ImageView backBtn = (ImageView) this.findViewById(R.id.back_btn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        final TextView  imageCount = (TextView) this.findViewById(R.id.image_count);
        imageCount.setText((position+1)+"/"+urlPath.size());
        photoImageViewPager.setCurrentItem(position);

        photoImageViewPager.setOnPageChangeListener(new AbPhotoImageViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                imageCount.setText((i+1)+"/"+urlPath.size());
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
	}


}
