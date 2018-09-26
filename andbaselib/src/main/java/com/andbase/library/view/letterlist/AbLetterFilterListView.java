
package com.andbase.library.view.letterlist;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.HeaderViewListAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SectionIndexer;

import com.andbase.library.view.listener.AbOnItemSelectListener;

/**
 * Copyright amsoft.cn
 * Author 还如一梦中
 * Date 2016/6/14 17:54
 * Email 396196516@qq.com
 * Info 一个列表 字符过滤显示
 */
public class AbLetterFilterListView extends RelativeLayout {
	
	/** 上下文. */
	private Context context;
	
	/** 数据列表. */
	private ListView listView;
	
	/** 字母表. */
	private AbLetterView letterView;
	
	/**
	 * 构造函数.
	 *
	 * @param context the context
	 */
	public AbLetterFilterListView(Context context) {
		super(context);
		init(context);
	}

	/**
	 * 构造函数.
	 *
	 * @param context the context
	 * @param attrs the attrs
	 */
	public AbLetterFilterListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	/**
	 * 构造函数.
	 *
	 * @param context the context
	 * @param attrs the attrs
	 * @param defStyle the def style
	 */
	public AbLetterFilterListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}
	
	/**
	 * 初始化.
	 *
	 * @param context the context
	 */
	private void init(Context context) {
		this.context  = context;
		this.setBackgroundResource(android.R.color.white);

		listView = new ListView(context);
		listView.setCacheColorHint(Color.parseColor("#00000000"));
		listView.setDivider(new ColorDrawable(Color.parseColor("#D3D3D3")));
		listView.setDividerHeight(1);
		listView.setHorizontalScrollBarEnabled(false);
		listView.setVerticalScrollBarEnabled(false);
		listView.setBackgroundResource(android.R.color.white);

		this.addView(listView,new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));
		letterView = new AbLetterView(context);

		LayoutParams layoutParams = new LayoutParams(60, ViewGroup.LayoutParams.WRAP_CONTENT);
		layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT,RelativeLayout.TRUE);
		layoutParams.topMargin=10;
		layoutParams.rightMargin=10;
		layoutParams.bottomMargin=10;
		this.addView(letterView,layoutParams);

		letterView.setOnItemSelectListener(new AbOnItemSelectListener() {
			@Override
			public void onSelect(int position) {
				if (listView.getAdapter() != null) {
					SectionIndexer sectionIndexter = null;
					if(listView.getHeaderViewsCount()>0){
						HeaderViewListAdapter listAdapter = (HeaderViewListAdapter) listView.getAdapter();
						sectionIndexter = (SectionIndexer) listAdapter.getWrappedAdapter();
					}else{
						sectionIndexter = (SectionIndexer) listView.getAdapter();

					}
					int positionList = sectionIndexter.getPositionForSection(letterView.getLetter(position));
					listView.setSelection(positionList);
				}
			}
		});

	}

	public ListView getListView() {
		return listView;
	}


	public void setSelection(int position){
		this.listView.setSelection(position);
	}

	public void setAdapter(ListAdapter adapter){
		this.listView.setAdapter(adapter);
	}

	public ListAdapter getAdapter(){
		return this.listView.getAdapter();
	}

	public AbLetterView getLetterView() {
		return letterView;
	}

}
