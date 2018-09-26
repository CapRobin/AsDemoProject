package com.andbase.demo.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;

import com.andbase.demo.R;
import com.andbase.demo.adapter.SampleTextAdapter;
import com.andbase.library.app.base.AbBaseActivity;
import com.andbase.library.app.model.AbSampleItem;
import com.andbase.library.util.AbToastUtil;
import com.andbase.library.view.listener.AbOnItemClickListener;
import com.andbase.library.view.sample.AbSampleGridView;

import java.util.ArrayList;

public class SampleGridViewActivity extends AbBaseActivity {

    ArrayList<AbSampleItem> paramList = new ArrayList<AbSampleItem>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample_gird_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setTitle(R.string.title_sample_grid_view);
        //toolbar.setContentInsetsRelative(0, 0);
        toolbar.setNavigationIcon(R.drawable.ic_back);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        for(int i=0;i< 250 ;i++){
            paramList.add(new AbSampleItem("item"+i,"ITEM "+i,null,String.valueOf(i)));
        }

        AbSampleGridView gridView = (AbSampleGridView)findViewById(R.id.grid_view);
        gridView.setPadding(10,10);
        gridView.setColumn(4);
        SampleTextAdapter sampleTextAdapter = new SampleTextAdapter(this,paramList);
        gridView.setAdapter(sampleTextAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AbSampleItem item =  paramList.get(position);
                AbToastUtil.showToast(SampleGridViewActivity.this,item.getText()+":"+item.getValue());
            }
        });


    }


}
