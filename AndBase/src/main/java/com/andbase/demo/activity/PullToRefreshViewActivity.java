package com.andbase.demo.activity;

import java.util.Random;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.andbase.demo.R;
import com.andbase.demo.global.MyApplication;
import com.andbase.library.app.base.AbBaseActivity;
import com.andbase.library.asynctask.AbTask;
import com.andbase.library.asynctask.AbTaskItem;
import com.andbase.library.asynctask.AbTaskListener;
import com.andbase.library.util.AbDialogUtil;
import com.andbase.library.view.refresh.AbPullToRefreshView;


public class PullToRefreshViewActivity extends AbBaseActivity implements AbPullToRefreshView.OnHeaderRefreshListener,AbPullToRefreshView.OnFooterLoadListener {
	
	private MyApplication application;
	private AbPullToRefreshView mAbPullToRefreshView = null;
	private TextView mTextView = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pull_to_refresh_view);
        application = (MyApplication)this.getApplication();

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
        
	    //获取ListView对象
        mAbPullToRefreshView = (AbPullToRefreshView)this.findViewById(R.id.mPullRefreshView);
        mTextView = (TextView)this.findViewById(R.id.mTextView);
        
        //设置监听器
        mAbPullToRefreshView.setOnHeaderRefreshListener(this);
        mAbPullToRefreshView.setOnFooterLoadListener(this);
        
        //设置进度条的样式
        mAbPullToRefreshView.getDefaultHeaderView().setHeaderProgressBarDrawable(this.getResources().getDrawable(R.drawable.progress_circular));
        mAbPullToRefreshView.getFooterView().setFooterProgressBarDrawable(this.getResources().getDrawable(R.drawable.progress_circular));

        refreshTask();
    }

	@Override
	protected void onResume() {
		super.onResume();
	}
	
	public void onPause() {
		super.onPause();
	}
	
	@Override
    public void onFooterLoad(AbPullToRefreshView view) {
	    loadMoreTask();
    }
	
    @Override
    public void onHeaderRefresh(AbPullToRefreshView view) {
        refreshTask();
    }
	
	
	public void refreshTask(){
		AbTask mAbTask = AbTask.newInstance();
        final AbTaskItem item = new AbTaskItem();
        item.setListener(new AbTaskListener() {

            @Override
            public void update() {
            	AbDialogUtil.removeDialog(PullToRefreshViewActivity.this);
                mTextView.setText("This is "+new Random().nextInt(100));
                mAbPullToRefreshView.onHeaderRefreshFinish();
            }

            @Override
            public void get() {
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                }
          };
        });
        
        mAbTask.execute(item);
    }
	
	public void loadMoreTask(){
		AbTask mAbTask = AbTask.newInstance();
        final AbTaskItem item = new AbTaskItem();
        item.setListener(new AbTaskListener() {

            @Override
            public void update() {
            	AbDialogUtil.removeDialog(PullToRefreshViewActivity.this);
                mTextView.append("+"+new Random().nextInt(100));
                mAbPullToRefreshView.onFooterLoadFinish();
            }

            @Override
            public void get() {
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                }
          };
        });
        
        mAbTask.execute(item);
    }
   
}


