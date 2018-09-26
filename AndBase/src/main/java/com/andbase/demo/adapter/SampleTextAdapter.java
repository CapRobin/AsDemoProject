package com.andbase.demo.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.andbase.demo.R;
import com.andbase.library.app.model.AbSampleItem;

import java.util.List;


/**
 * Copyright amsoft.cn
 * Author 还如一梦中
 * Date 2016/6/16 13:27
 * Email 396196516@qq.com
 * Info 只是文本的适配器，没什么
 */
public class SampleTextAdapter extends BaseAdapter {
	private Context context;
	private List<AbSampleItem> data;

	public SampleTextAdapter(Context context, List<AbSampleItem> data) {
		super();
		this.context = context;
		this.data = data;
	}

	public List<AbSampleItem> getData() {
		return data;
	}

	public void setData(List<AbSampleItem> data) {
		this.data = data;
	}


	public SampleTextAdapter(Context context) {
		super();
		this.context = context;
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
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if (convertView == null) {
			convertView = View.inflate(context, R.layout.item_grid_text, null);
			holder = new ViewHolder();
			holder.text = (TextView) convertView.findViewById(R.id.text);
			convertView.setTag(holder);
		}

		else {
			holder = (ViewHolder) convertView.getTag();
		}
		final AbSampleItem item = data.get(position);

		holder.text.setText(item.getText());
		return convertView;
	}

	static class ViewHolder {
		TextView text;
	}

}
