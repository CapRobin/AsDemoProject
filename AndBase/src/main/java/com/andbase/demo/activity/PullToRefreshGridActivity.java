package com.andbase.demo.activity;

import java.util.ArrayList;
import java.util.Random;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.andbase.demo.R;
import com.andbase.demo.adapter.PullToRefreshGridAdapter;
import com.andbase.demo.global.MyApplication;
import com.andbase.demo.model.ImageInfo;
import com.andbase.library.app.base.AbBaseActivity;
import com.andbase.library.asynctask.AbTask;
import com.andbase.library.asynctask.AbTaskItem;
import com.andbase.library.asynctask.AbTaskListener;
import com.andbase.library.util.AbAppUtil;
import com.andbase.library.util.AbToastUtil;
import com.andbase.library.util.AbViewUtil;
import com.andbase.library.view.refresh.AbPullToRefreshView;


public class PullToRefreshGridActivity extends AbBaseActivity implements AbPullToRefreshView.OnHeaderRefreshListener,AbPullToRefreshView.OnFooterLoadListener {
	
	private int currentPage = 1;
	private MyApplication application;
	private ArrayList<ImageInfo> imageInfoList = null;
	private ArrayList<ImageInfo> imageInfoNewList = null;
	private AbPullToRefreshView mAbPullToRefreshView;
	private GridView mGridView = null;
	private PullToRefreshGridAdapter pullToRefreshGridAdapter = null;
	private ArrayList<String> mPhotoList = new ArrayList<String>();
	private int total = 5000;
	private int pageSize = 100;

    @Override
    public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.pull_to_refresh_grid);

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
        
	    
	    for (int i = 0; i < 23; i++) {
            mPhotoList.add("http://www.amsoft.cn/demo/rand/" + i + ".jpg");
		}
	    
		application = (MyApplication) this.getApplication();
		//获取ListView对象
        mAbPullToRefreshView = (AbPullToRefreshView)this.findViewById(R.id.mPullRefreshView);
        mGridView = (GridView)this.findViewById(R.id.mGridView);
        
        //设置监听器
        mAbPullToRefreshView.setOnHeaderRefreshListener(this);
        mAbPullToRefreshView.setOnFooterLoadListener(this);
        
        //设置进度条的样式
        mAbPullToRefreshView.getDefaultHeaderView().setHeaderProgressBarDrawable(this.getResources().getDrawable(R.drawable.progress_circular));
        mAbPullToRefreshView.getFooterView().setFooterProgressBarDrawable(this.getResources().getDrawable(R.drawable.progress_circular));
        //mAbPullListView.getDefaultHeaderView().setHeaderProgressBarDrawable(this.getResources().getDrawable(R.drawable.progress_circular2));
        //mAbPullListView.getFooterView().setFooterProgressBarDrawable(this.getResources().getDrawable(R.drawable.progress_circular2));
        DisplayMetrics dm = AbAppUtil.getDisplayMetrics(this);
        int width = (dm.widthPixels-25)/3;
		mGridView.setColumnWidth(AbViewUtil.scaleValue(this, width));
		mGridView.setGravity(Gravity.CENTER);
		mGridView.setHorizontalSpacing(5);
		
		//Animation animation = AnimationUtils.loadAnimation(this, R.anim.fade);
	    //得到一个LayoutAnimationController对象;
	    //LayoutAnimationController lac = new LayoutAnimationController(animation);
		//mGridView.setLayoutAnimation(lac);
		/*AlphaAnimation animationAlpha = new AlphaAnimation(0.0f,1.0f);  
	    //得到一个LayoutAnimationController对象;
	    LayoutAnimationController lac = new LayoutAnimationController(animationAlpha);
	    //设置控件显示的顺序;
	    lac.setOrder(LayoutAnimationController.ORDER_RANDOM);
	    //设置控件显示间隔时间;
	    lac.setDelay(0.5f);
	    //为View设置LayoutAnimationController属性;
		mGridView.setLayoutAnimation(lac);*/

		mGridView.setNumColumns(GridView.AUTO_FIT);
		mGridView.setPadding(0, 0, 0, 0);
		mGridView.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
		mGridView.setVerticalSpacing(10);
		// ListView数据
        imageInfoList = new ArrayList<ImageInfo>();
		// 使用自定义的Adapter
        pullToRefreshGridAdapter = new PullToRefreshGridAdapter(this, imageInfoList,width,width,imageLoader);
		mGridView.setAdapter(pullToRefreshGridAdapter);

        mAbPullToRefreshView.headerRefreshing();
		
		mGridView.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				AbToastUtil.showToast(PullToRefreshGridActivity.this,""+position);
			}
    		
    	});

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
        //定义两种查询的事件
        final AbTaskItem item = new AbTaskItem();
        item.setListener(new AbTaskListener() {

            @Override
            public void update() {
                imageInfoList.clear();
                if(imageInfoNewList!=null && imageInfoNewList.size()>0){
                    imageInfoList.addAll(imageInfoNewList);
                    pullToRefreshGridAdapter.notifyDataSetChanged();
                    imageInfoNewList.clear();
                }
                mAbPullToRefreshView.onHeaderRefreshFinish();
            }

            @Override
            public void get() {
                try {
                    currentPage = 1;
                    Thread.sleep(1000);
                    imageInfoNewList =  new ArrayList<ImageInfo>() ;
                    
                    for (int i = 0; i < pageSize; i++) {
                        final ImageInfo imageInfo = new ImageInfo();
                        if(i>=mPhotoList.size()){
                            imageInfo.setPath(mPhotoList.get(mPhotoList.size()-1));
                        }else{
                            imageInfo.setPath(mPhotoList.get(i));
                        }

                        imageInfoNewList.add(imageInfo);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    AbToastUtil.showToastInThread(PullToRefreshGridActivity.this,e.getMessage());
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
                if(imageInfoNewList!=null && imageInfoNewList.size()>0){
                    imageInfoList.addAll(imageInfoNewList);
                    pullToRefreshGridAdapter.notifyDataSetChanged();
                    imageInfoNewList.clear();
                }
                mAbPullToRefreshView.onFooterLoadFinish();
            }

            @Override
            public void get() {
                try {
                    currentPage++;
                    Thread.sleep(1000);
                    imageInfoNewList =  new ArrayList<ImageInfo>() ;
                    for (int i = 0; i < pageSize; i++) {
                        final ImageInfo imageInfo = new ImageInfo();
                        imageInfo.setPath(mPhotoList.get(new Random().nextInt(mPhotoList.size())));
                        if((imageInfoList.size()+imageInfoNewList.size()) < total){
                            imageInfoNewList.add(imageInfo);
                        }
                        
                    }
                } catch (Exception e) {
                    currentPage--;
                    imageInfoNewList.clear();
                    AbToastUtil.showToastInThread(PullToRefreshGridActivity.this,e.getMessage());
                }
               
          };
        });
        
        mAbTask.execute(item);
    }


}
