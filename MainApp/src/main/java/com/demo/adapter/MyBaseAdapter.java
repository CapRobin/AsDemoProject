package com.demo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

public abstract class MyBaseAdapter extends BaseAdapter {
	
	protected Context context;
	protected LayoutInflater inflater;
	protected List<?> data;
	private int resouceId;
	
	public MyBaseAdapter(Context context, List<?> data, int resourceId){
		this.context = context;
		this.data = data;
		this.inflater = LayoutInflater.from(context);
		this.resouceId = resourceId;
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		return data.get(position);
	}	

	@Override
	public long getItemId(int position) {
		return convertId(position,data.get(position));
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView == null){
			convertView = inflater.inflate(resouceId, null, false);
			newView(convertView);
		}
		holderView(convertView,data.get(position));
		return convertView;
	}
	
	public void setData(List<?> data){
		this.data = data;
	}
	
	public List<?> getData(){
		return data;
	}
	
	/**
	 * 用于覆盖，在各个其他adapter里边返回id,默认返回position
	 * @param position
	 * @param object
	 * @return
	 */
	protected long convertId(int position,Object object){
		return position;
	}

	/**
	 * 第一次创建的时�?调用
	 * @param convertView
	 */
	protected abstract void newView(View convertView);
	
	/**
	 * 用于数据赋�?等等
	 * @param convertView
	 * @param itemObject
	 */
	protected abstract void holderView(View convertView, Object itemObject);


}
