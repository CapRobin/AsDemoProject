package com.andbase.demo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.andbase.demo.R;
import com.andbase.demo.global.MyApplication;
import com.andbase.library.app.base.AbBaseActivity;

/**
 * 名称：PullToRefreshActivity
 * 描述：下拉刷新分页
 * 
 * @author 还如一梦中
 * @date 2011-12-13
 * @version
 */
public class PullToRefreshActivity extends AbBaseActivity {

	private MyApplication application;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.pull_to_refresh_main);

		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		toolbar.setTitle(R.string.title_pull_to_refresh);
		toolbar.setContentInsetsRelative(0, 0);
		toolbar.setNavigationIcon(R.drawable.ic_back);
		toolbar.setNavigationOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		application = (MyApplication) this.getApplication();
		Button mListView = (Button) this.findViewById(R.id.mListView);
		Button mSampleView = (Button) this.findViewById(R.id.mSampleView);
		Button mGridView = (Button) this.findViewById(R.id.mGridView);

		mSampleView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(PullToRefreshActivity.this,
						PullToRefreshViewActivity.class);
				startActivity(intent);
			}
		});

		mListView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(PullToRefreshActivity.this,
						PullToRefreshListActivity.class);
				startActivity(intent);
			}
		});

		mGridView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(PullToRefreshActivity.this,
						PullToRefreshGridActivity.class);
				startActivity(intent);
			}
		});

	}

}
