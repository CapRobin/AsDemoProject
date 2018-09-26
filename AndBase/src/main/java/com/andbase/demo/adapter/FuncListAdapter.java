package com.andbase.demo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.andbase.demo.R;
import com.andbase.demo.model.FuncMenu;
import com.andbase.library.image.AbImageLoader;

import java.util.ArrayList;
import java.util.List;

public class FuncListAdapter extends BaseAdapter {
	private List<FuncMenu> list = new ArrayList<FuncMenu>();
	private Context context;
	private AbImageLoader imageLoader;

	public FuncListAdapter(Context context, List<FuncMenu> list) {
		super();
		this.context = context;
		this.list = list;
		imageLoader = AbImageLoader.getInstance(context);
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public void clearItems() {
		list.clear();
	}

	public void addItems(List<FuncMenu> items) {
		list.clear();
		if (items!=null) {
			list.addAll(items);
			
		}
	}

	public void addMoreItems(List<FuncMenu> items) {
		list.addAll(items);
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.item_list, null);
			holder.icon = (ImageView) convertView.findViewById(R.id.item_icon);
			holder.title = (TextView) convertView.findViewById(R.id.item_title);
			holder.intro = (TextView) convertView.findViewById(R.id.item_intro);
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		FuncMenu menu = list.get(position);


		holder.title.setText(menu.getTitle());
		holder.intro.setText(menu.getIntro());

		return convertView;
	}

	static class ViewHolder {
		ImageView icon;
		TextView title;
		TextView intro;
	}

}
