package com.andbase.demo.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.andbase.demo.R;
import com.andbase.demo.model.ImageUploadInfo;
import com.andbase.library.asynctask.AbTaskItem;
import com.andbase.library.asynctask.AbTaskObjectListener;
import com.andbase.library.asynctask.AbTaskQueue;
import com.andbase.library.image.AbImageLoader;
import com.andbase.library.util.AbFileUtil;
import com.andbase.library.util.AbImageUtil;
import com.andbase.library.util.AbStrUtil;

import java.io.File;
import java.util.List;

/**
 * Copyright amsoft.cn
 * Author 还如一梦中
 * Date 2016/6/16 13:27
 * Email 396196516@qq.com
 * Info 适配器 网络URL的图片和本地,资源文件图片显示.
 */
public class ImageUploadAdapter extends BaseAdapter {

	/** 上下问. */
	private Context context;

	/** 图片的路径. */
	private List<ImageUploadInfo> images = null;

	/** 图片宽度. */
	private int width;

	/** 图片高度. */
	private int height;

	/**图片下载器*/
	private AbImageLoader imageLoader = null;
	private AbTaskQueue task;
	private AdapterView.OnItemClickListener onItemClickListener;

	public ImageUploadAdapter(Context context, List<ImageUploadInfo> images, int width, int height,AbImageLoader imageLoader) {
		this.context = context;
		this.images = images;
		this.width = width;
		this.height = height;
		this.imageLoader = imageLoader;
		this.task = AbTaskQueue.newInstance();
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
        if(convertView == null){
            holder = new ViewHolder();
			convertView = View.inflate(context, R.layout.item_upload_image, null);
			holder.imageView = (ImageView) convertView.findViewById(R.id.image_view);
            holder.deleteBtn =  (Button) convertView.findViewById(R.id.delete_btn);
			holder.parentLayout = (RelativeLayout) convertView.findViewById(R.id.parent_layout);
			holder.parentLayout.setLayoutParams(new LinearLayout.LayoutParams(width, height));
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        holder.imageView.setImageBitmap(null);
        holder.deleteBtn.setVisibility(View.INVISIBLE);

		holder.imageView.setFocusable(false);
		holder.deleteBtn.setFocusable(false);


		holder.deleteBtn.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View view) {
				images.remove(position);
				notifyDataSetChanged();
			}
		});

		loadImage(position,holder.imageView);

        return convertView;
	}


	/**
	 * 增加并改变视图.
	 * @param position the position
	 * @param imageUploadInfo the image paths
	 */
	public void addItem(int position,ImageUploadInfo imageUploadInfo) {
		images.add(position,imageUploadInfo);
		notifyDataSetChanged();
	}

	/**
	 * 增加多条并改变视图.
	 * @param imageUploadInfos the image paths
	 */
	public void addItems(List<ImageUploadInfo> imageUploadInfos) {
		images.addAll(imageUploadInfos);
		notifyDataSetChanged();
	}

	/**
	 * 增加多条并改变视图.
	 */
	public void clearItems() {
		images.clear();
		notifyDataSetChanged();
	}


	/**
	 * View元素.
	 */
	public static class ViewHolder {
		public ImageView imageView;
        public Button deleteBtn;
		public RelativeLayout parentLayout;
	}

	public AdapterView.OnItemClickListener getOnItemClickListener() {
		return onItemClickListener;
	}

	public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
		this.onItemClickListener = onItemClickListener;
	}

    public void loadImage(final int position,final ImageView imageView){

        final ImageUploadInfo imageUploadInfo = images.get(position);
        //AbLogUtil.e(this.context,position+"路径："+image);
		final String image = imageUploadInfo.getPath();
        final String currentTag = image+"_"+position;

        //设置标记
        imageView.setTag(R.id.image_view,currentTag);

        if(!AbStrUtil.isEmpty(image)){
            if(image.indexOf("http://")!=-1){
                //图片的下载
                imageLoader.display(imageView,image,this.width,this.height);

            }else if(AbStrUtil.isNumber(image)){
                //索引图片
                try {
                    int res  = Integer.parseInt(image);
                    imageView.setImageDrawable(context.getResources().getDrawable(res));
                } catch (Exception e) {
                }
            }else {

				final AbTaskItem item = new AbTaskItem();
				item.setPosition(position);
				item.setListener(new AbTaskObjectListener() {
					@Override
					public <T> T getObject() {
						Bitmap bitmap = AbFileUtil.getBitmapFromSD(new File(image), AbImageUtil.CUTIMG, width, height);
						return (T) bitmap;
					}

					@Override
					public <T> void update(T t) {
						if(t == null){
							return;
						}
						Bitmap bitmap = (Bitmap) t;
						String oldTag = (String)imageView.getTag(R.id.image_view);
						if (currentTag.equals(oldTag)) {
							imageView.setImageBitmap(bitmap);
						}
					}
				});
				task.execute(item);

            }
        }



    }

}
