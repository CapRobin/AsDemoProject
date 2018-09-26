package com.andbase.demo.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.andbase.demo.R;
import com.andbase.demo.adapter.PullToRefreshListAdapter;
import com.andbase.demo.global.MyApplication;
import com.andbase.library.app.base.AbBaseActivity;
import com.andbase.library.asynctask.AbTask;
import com.andbase.library.asynctask.AbTaskItem;
import com.andbase.library.asynctask.AbTaskListListener;
import com.andbase.library.util.AbLogUtil;
import com.andbase.library.util.AbToastUtil;
import com.andbase.library.view.listener.AbOnScrollListener;
import com.andbase.library.view.refresh.AbPullToRefreshView;
import com.andbase.library.view.sample.AbScrollListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class FloatTabActivity extends AbBaseActivity implements AbPullToRefreshView.OnHeaderRefreshListener, AbPullToRefreshView.OnFooterLoadListener {

    private MyApplication application;
    private List<Map<String, Object>> list = null;
    private AbPullToRefreshView mAbPullToRefreshView = null;
    private AbScrollListView mListView = null;
    private int currentPage = 1;
    private ArrayList<String> mPhotoList = new ArrayList<String>();
    private PullToRefreshListAdapter pullToRefreshListAdapter = null;
    private int total = 50000;
    private int pageSize = 15;

    private LinearLayout top_bar = null;
    private int headerHeight = 300;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_float_tab);
        application = (MyApplication)this.getApplication();

        top_bar =  (LinearLayout)findViewById(R.id.top_bar);

        final  RelativeLayout.LayoutParams layoutParams =  (RelativeLayout.LayoutParams)top_bar.getLayoutParams();
        top_bar.setVisibility(View.INVISIBLE);

        // 获取ListView对象
        mAbPullToRefreshView = (AbPullToRefreshView) this.findViewById(R.id.mPullRefreshView);

        for (int i = 0; i < 23; i++) {
            mPhotoList.add("http://www.amsoft.cn/demo/rand/" + i + ".jpg");
        }


        mListView = (AbScrollListView) this.findViewById(R.id.mListView);
        mListView.setOnScrollListener(onScrollListener);
        mListView.addHeaderView(View.inflate(this,R.layout.activity_float_tab_list_header,null));
        mListView.setHeaderHeight(headerHeight);
        // ListView数据
        list = new ArrayList<Map<String, Object>>();

        // 设置监听器
        mAbPullToRefreshView.setOnHeaderRefreshListener(this);
        mAbPullToRefreshView.setOnFooterLoadListener(this);

        // 设置进度条的样式
        mAbPullToRefreshView.getDefaultHeaderView().setHeaderProgressBarDrawable(
                this.getResources().getDrawable(R.drawable.progress_circular));
        mAbPullToRefreshView.getFooterView().setFooterProgressBarDrawable(
                this.getResources().getDrawable(R.drawable.progress_circular));

        // 使用自定义的Adapter
        pullToRefreshListAdapter = new PullToRefreshListAdapter(this, list,200,200);
        mListView.setAdapter(pullToRefreshListAdapter);

        // item被点击事件
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
            }
        });

        refreshTask();

    }

    @Override
    public void onFooterLoad(AbPullToRefreshView view) {
        loadMoreTask();
    }

    @Override
    public void onHeaderRefresh(AbPullToRefreshView view) {
        refreshTask();

    }

    private AbOnScrollListener onScrollListener = new  AbOnScrollListener(){
        @Override
        public void onScrollPosition(int position) {

        }

        @Override
        public void onScrollY(int scrollY) {

            if(scrollY == 0){
                //恢复位置
                top_bar.setVisibility(View.INVISIBLE);

            }else if(scrollY >= headerHeight){
                //到达顶部
                top_bar.setVisibility(View.VISIBLE);

            }else{
                //中间区域
                top_bar.setVisibility(View.INVISIBLE);
            }
        }
    };

    public void refreshTask() {
        AbLogUtil.prepareLog(PullToRefreshListActivity.class);
        AbTask mAbTask = AbTask.newInstance();
        final AbTaskItem item = new AbTaskItem();
        item.setListener(new AbTaskListListener() {
            @Override
            public List<?> getList() {
                List<Map<String, Object>> newList = null;
                try {
                    Thread.sleep(1000);
                    currentPage = 1;
                    newList = new ArrayList<Map<String, Object>>();
                    Map<String, Object> map = null;

                    for (int i = 0; i < pageSize; i++) {
                        map = new HashMap<String, Object>();
                        map.put("itemsIcon", mPhotoList.get(i));
                        map.put("itemsTitle", "item" + (i + 1));
                        map.put("itemsText", "item..." + (i + 1));
                        newList.add(map);

                    }
                } catch (Exception e) {
                }
                return newList;
            }

            @Override
            public void update(List<?> paramList) {

                //通知Dialog
                AbLogUtil.d(PullToRefreshListActivity.class, "返回", true);
                List<Map<String, Object>> newList = (List<Map<String, Object>>) paramList;
                list.clear();
                if (newList != null && newList.size() > 0) {
                    list.addAll(newList);
                    pullToRefreshListAdapter.notifyDataSetChanged();
                    newList.clear();
                }
                mAbPullToRefreshView.onHeaderRefreshFinish();
            }

        });

        mAbTask.execute(item);
    }

    public void loadMoreTask() {
        AbTask mAbTask = AbTask.newInstance();
        final AbTaskItem item = new AbTaskItem();
        item.setListener(new AbTaskListListener() {

            @Override
            public void update(List<?> paramList) {
                List<Map<String, Object>> newList = (List<Map<String, Object>>) paramList;
                if (newList != null && newList.size() > 0) {
                    list.addAll(newList);
                    pullToRefreshListAdapter.notifyDataSetChanged();
                    newList.clear();
                }
                mAbPullToRefreshView.onFooterLoadFinish();

            }

            @Override
            public List<?> getList() {
                List<Map<String, Object>> newList = null;
                try {
                    currentPage++;
                    Thread.sleep(1000);
                    newList = new ArrayList<Map<String, Object>>();
                    Map<String, Object> map = null;

                    for (int i = 0; i < pageSize; i++) {
                        map = new HashMap<String, Object>();
                        map.put("itemsIcon", mPhotoList.get(i));
                        map.put("itemsTitle", "item上拉"
                                + ((currentPage - 1) * pageSize + (i + 1)));
                        map.put("itemsText", "item上拉..."
                                + ((currentPage - 1) * pageSize + (i + 1)));
                        if ((list.size() + newList.size()) < total) {
                            newList.add(map);
                        }
                    }

                } catch (Exception e) {
                    currentPage--;
                    newList.clear();
                    AbToastUtil.showToastInThread(
                            FloatTabActivity.this, e.getMessage());
                }
                return newList;
            };
        });

        mAbTask.execute(item);
    }

}
