package com.andbase.demo.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.andbase.demo.R;
import com.andbase.library.asynctask.AbTaskItem;
import com.andbase.library.asynctask.AbTaskMultiQueue;
import com.andbase.library.asynctask.AbTaskObjectListener;
import com.andbase.library.cache.image.AbImageCacheImpl;
import com.andbase.library.image.AbImageLoader;
import com.andbase.library.util.AbFileUtil;
import com.andbase.library.util.AbImageUtil;
import com.andbase.library.util.AbLogUtil;
import com.andbase.library.util.AbStrUtil;
import com.andbase.library.view.sample.AbFilterImageView;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * Copyright amsoft.cn
 * Author 还如一梦中
 * Date 2016/6/16 13:27
 * Email 396196516@qq.com
 * Info 适配器 网络URL的图片和本地,资源文件图片显示.
 */
public class PullToRefreshListAdapter extends BaseAdapter {

	/** 上下文. */
	private Context context;

	/** 图片的路径. */
	private List<Map<String, Object>> images = null;

	/** 图片宽度. */
	private int width;

	/** 图片高度. */
	private int height;

	/**图片下载器*/
	private AbImageLoader imageLoader = null;
	private AbImageCacheImpl imageCache = null;
	private AbTaskMultiQueue task;

	public PullToRefreshListAdapter(Context context, List<Map<String, Object>> images, int width, int height) {
		this.context = context;
		this.images = images;
		this.width = width;
		this.height = height;
		this.imageLoader = new AbImageLoader(context);
		this.imageCache = new AbImageCacheImpl(context);
		this.task = AbTaskMultiQueue.getInstance();
	}

	/**
	 * 获取数量.
	 * @return the count
	 */
	public int getCount() {
		return images.size();
	}

	/**
	 * 获取索引位置的路径.
	 * @param position the position
	 * @return the item
	 */
	public Object getItem(int position) {
		return images.get(position);
	}

	/**
	 * 获取位置.
	 * @param position the position
	 * @return the item id
	 */
	public long getItemId(int position) {
		return position;
	}

	/**
	 * 显示View.
	 * @param position the position
	 * @param convertView the convert view
	 * @param parent the parent
	 * @return the view
	 */
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

		final Map<String, Object> map = images.get(position);
		final String image = (String)map.get("itemsIcon");
        holder.title.setText((String)map.get("itemsTitle"));
        holder.intro.setText((String)map.get("itemsText"));

		AbLogUtil.i(this.context,position+"路径："+image);

		final String currentTag = image+"_"+position;

		String oldTag = (String)holder.icon.getTag(R.id.image_view);

		final String cacheKey = image + "w" + width + "h" + height;

		//标记变化才执行
		if(!currentTag.equals(oldTag)){

			holder.icon.setImageBitmap(null);
			holder.icon.setImageDrawable(null);

			//设置标记
			holder.icon.setTag(R.id.image_view,currentTag);

			if(!AbStrUtil.isEmpty(image)){
				if(image.indexOf("http://")!=-1){
					//图片的下载
					imageLoader.display(holder.icon,image,this.width,this.height);

				}else if(AbStrUtil.isNumber(image)){
					//索引图片
					try {
						int res  = Integer.parseInt(image);
						holder.icon.setImageDrawable(context.getResources().getDrawable(res));
					} catch (Exception e) {
					}
				}else {

					Bitmap bitmap = this.imageCache.getBitmap(cacheKey);

					if (bitmap != null) {
						AbLogUtil.i("TAG", "从缓存中取到了数据" + position);
						holder.icon.setImageBitmap(bitmap);
					} else {

						AbLogUtil.i("TAG", "从缓存中没取到数据" + position);
						final AbTaskItem item = new AbTaskItem();
						item.setPosition(position);
						item.setListener(new AbTaskObjectListener() {
							@Override
							public <T> T getObject() {
								Bitmap bitmap = AbFileUtil.getBitmapFromSD(new File(image), AbImageUtil.SCALEIMG, width, height);
								return (T) bitmap;
							}

							@Override
							public <T> void update(T t) {
								if(t == null){
									return;
								}
								Bitmap bitmap = (Bitmap) t;
								imageCache.putBitmap(cacheKey, bitmap);
								if (currentTag.equals(holder.icon.getTag(R.id.image_view))) {
									holder.icon.setImageBitmap(bitmap);
								}

							}
						});
						task.execute(item);
					}
				}
			}

		}

		return convertView;
	}

	static class ViewHolder {
		ImageView icon;
		TextView title;
		TextView intro;
	}

}
