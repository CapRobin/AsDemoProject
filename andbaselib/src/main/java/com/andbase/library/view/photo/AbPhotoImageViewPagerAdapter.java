package com.andbase.library.view.photo;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.andbase.library.asynctask.AbTaskItem;
import com.andbase.library.asynctask.AbTaskMultiQueue;
import com.andbase.library.asynctask.AbTaskObjectListener;
import com.andbase.library.cache.image.AbImageCacheImpl;
import com.andbase.library.image.AbImageLoader;
import com.andbase.library.util.AbFileUtil;
import com.andbase.library.util.AbImageUtil;
import com.andbase.library.util.AbStrUtil;

import java.io.File;
import java.util.LinkedList;
import java.util.List;


/**
 * Copyright amsoft.cn
 * Author 还如一梦中
 * Date 2016/6/16 13:27
 * Email 396196516@qq.com
 * Info ViewPager适配器
 */
public class AbPhotoImageViewPagerAdapter extends PagerAdapter{

	/** 上下文. */
	private Context context;

	/** View列表. */
	final LinkedList<AbPhotoImageView> viewCache = new LinkedList<AbPhotoImageView>();

	private List<String> urlPathList = null;

	private AbPhotoImageViewPager photoImageViewPager = null;

	/** 图片加载. */
	private AbImageLoader imageLoader = null;

	private AbTaskMultiQueue task;

	private AbImageCacheImpl imageCache = null;

	/**
	 * 构造函数.
	 * @param context
	 * @param urlPathList
	 */
	public AbPhotoImageViewPagerAdapter(Context context, AbPhotoImageViewPager photoImageViewPager, List<String>  urlPathList,AbImageLoader imageLoader) {
		this.context = context;
		this.urlPathList = urlPathList;
		this.photoImageViewPager = photoImageViewPager;
		this.imageLoader = imageLoader;
		this.imageCache = new AbImageCacheImpl(context);
		this.task = AbTaskMultiQueue.getInstance();
	}

	/**
	 * 获取数量.
	 * @return the count
	 */
	@Override
	public int getCount() {
		return this.urlPathList.size();
	}

	/**
	 * Object是否对应这个View.
	 * @param view the arg0
	 * @param obj the arg1
	 * @return true, if is view from object
	 */
	@Override
	public boolean isViewFromObject(View view, Object obj) {
		return view == (obj);
	}

	/**
	 * 显示View.
	 * @param container the container
	 * @param position the position
	 * @return the object
	 */
	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		final AbPhotoImageView photoImageView;
		if (viewCache.size() > 0) {
			photoImageView = viewCache.remove();
			photoImageView.setImageBitmap(null);
			photoImageView.reset();
		} else {
			photoImageView = new AbPhotoImageView(context);
		}
		final String urlPath = this.urlPathList.get(position);
		loadImage(position,photoImageView,urlPath);

		container.addView(photoImageView);
		return photoImageView;
	}

	/**
	 * 移除View.
	 * @param container the container
	 * @param position the position
	 * @param object the object
	 */
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		AbPhotoImageView photoImageView = (AbPhotoImageView) object;
		container.removeView(photoImageView);
		viewCache.add(photoImageView);
	}
	
	/**
	 * 很重要，否则不能notifyDataSetChanged.
	 * @param object the object
	 * @return the item position
	 */
	@Override
	public int getItemPosition(Object object) {
		return POSITION_NONE;
	}


	@Override
	public void setPrimaryItem(ViewGroup container, int position, Object object) {
		final AbPhotoImageView photoImageView = (AbPhotoImageView) object;
		this.photoImageViewPager.setMainImageView(photoImageView);
		final String urlPath = this.urlPathList.get(position);
		loadImage(position,photoImageView,urlPath);
	}


	public void  loadImage(final int position,final AbPhotoImageView photoImageView,final String urlPath){

		final String cacheKey = urlPath + "wall" + "hall";

		if(!AbStrUtil.isEmpty(urlPath)) {
			if (urlPath.indexOf("http://") != -1 || urlPath.indexOf("https://") != -1 || urlPath.indexOf("www.") != -1) {
				//图片的下载
				this.imageLoader.display(photoImageView, urlPath);
			} else if (AbStrUtil.isNumber(urlPath)) {
				//索引图片
				try {
					int res = Integer.parseInt(urlPath);
					photoImageView.setImageDrawable(context.getResources().getDrawable(res));
				} catch (Exception e) {
				}
			} else {

				Bitmap bitmap = this.imageCache.getBitmap(cacheKey);
				if (bitmap != null) {
                    photoImageView.setImageBitmap(bitmap);
				} else {
					final AbTaskItem item = new AbTaskItem();
					item.setListener(new AbTaskObjectListener() {
						@Override
						public <T> T getObject() {
							Bitmap bitmap = AbFileUtil.getBitmapFromSD(new File(urlPath), AbImageUtil.SCALEIMG, 1280, 720);
							return (T) bitmap;
						}

						@Override
						public <T> void update(T t) {
							if(t == null){
								return;
							}
							Bitmap bitmap = (Bitmap) t;
							imageCache.putBitmap(cacheKey, bitmap);
							photoImageView.setImageBitmap(bitmap);
						}
					});
					task.execute(item);
				}

			}
		}
	}
}
