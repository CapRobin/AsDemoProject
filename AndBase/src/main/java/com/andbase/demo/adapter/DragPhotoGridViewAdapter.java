package com.andbase.demo.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.andbase.demo.R;
import com.andbase.demo.model.ImageUploadInfo;
import com.andbase.library.asynctask.AbTaskItem;
import com.andbase.library.asynctask.AbTaskObjectListener;
import com.andbase.library.asynctask.AbTaskQueue;
import com.andbase.library.cache.image.AbImageCacheImpl;
import com.andbase.library.image.AbImageLoader;
import com.andbase.library.util.AbImageUtil;
import com.andbase.library.util.AbStrUtil;
import com.andbase.library.util.AbViewUtil;
import com.andbase.library.view.draggrid.AbDragGridView;
import com.andbase.library.view.draggrid.AbDragGridViewAdapter;

import java.io.File;
import java.util.List;

public class DragPhotoGridViewAdapter extends AbDragGridViewAdapter {

    /** 上下问. */
    private Context context;

    private AbDragGridView dragGridView;

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

    public DragPhotoGridViewAdapter(Context context, AbDragGridView dragGridView, List<ImageUploadInfo> images, int width, int height,AbImageLoader imageLoader) {
        this.context = context;
        this.images = images;
        this.width = width;
        this.height = height;
        this.dragGridView = dragGridView;
        this.imageLoader = imageLoader;
        this.task = AbTaskQueue.newInstance();
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public ImageUploadInfo getItem(int position) {
        return images.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final ViewHolder holder;
        if(convertView == null){
            holder = new ViewHolder();
            convertView = View.inflate(context, R.layout.item_drag_image, null);
            holder.imageView = (ImageView) convertView.findViewById(R.id.image_view);
            holder.deleteBtn =  (Button) convertView.findViewById(R.id.delete_btn);
            holder.parentLayout = (RelativeLayout) convertView.findViewById(R.id.parent_layout);
            holder.parentLayout.setLayoutParams(new LinearLayout.LayoutParams(width, height));
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        final ImageUploadInfo imageUploadInfo = images.get(position);
        if(imageUploadInfo.isCamBtn()){
            holder.deleteBtn.setVisibility(View.INVISIBLE);
        }else{
            holder.deleteBtn.setVisibility(View.VISIBLE);
        }
        holder.deleteBtn.setFocusable(false);
        holder.imageView.setFocusable(false);
        /*holder.imageView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(onItemClickListener!=null){
                    onItemClickListener.onItemClick(null,view,position,position);
                }
            }
        });*/

        holder.deleteBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                images.remove(position);
                //限制最大6个
                if(containCam()==-1){
                    ImageUploadInfo imageUploadInfo1 =  new ImageUploadInfo(String.valueOf(R.drawable.cam_photo));
                    imageUploadInfo1.setCamBtn(true);
                    addItem(getCount(),imageUploadInfo1);
                }

                AbViewUtil.setAbsListViewHeight(dragGridView,3,220,10);
                notifyDataSetChanged();
            }
        });

        holder.imageView.setImageBitmap(null);
        //hide时隐藏Text
        if(position != hidePosition) {
            loadImage(position,holder);
        }else{
            holder.deleteBtn.setVisibility(View.INVISIBLE);
        }

        convertView.setId(position);

        return convertView;
    }

    public void removeView(int pos) {
        images.remove(pos);
        notifyDataSetChanged();
    }

    //更新拖动时的gridView
    public void swapView(int draggedPos, int destPos) {
        //从前向后拖动，其他item依次前移
        if(draggedPos < destPos) {
            images.add(destPos+1, getItem(draggedPos));
            images.remove(draggedPos);
        }
        //从后向前拖动，其他item依次后移
        else if(draggedPos > destPos) {
            images.add(destPos, getItem(draggedPos));
            images.remove(draggedPos+1);
        }
        hidePosition = destPos;
        notifyDataSetChanged();
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
     * 改变视图.
     * @param position the position
     * @param imageUploadInfo the image paths
     */
    public void setItem(int position,ImageUploadInfo imageUploadInfo) {
        images.set(position,imageUploadInfo);
        notifyDataSetChanged();
    }

    /**
     * 增加多条并改变视图.
     */
    public void clearItems() {
        images.clear();
        notifyDataSetChanged();
    }

    public int containCam(){
        int pos = -1;
        for(int i=0;i<images.size();i++){
            ImageUploadInfo imageUploadInfo = images.get(i);
            if(imageUploadInfo.isCamBtn()){
                pos = i;
                break;
            }
        }
        return pos;
    }


    /**
     * View元素.
     */
    public static class ViewHolder {
        public ImageView imageView;
        public Button deleteBtn;
        public RelativeLayout parentLayout;
        public String imageTag;
    }

    public AdapterView.OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void loadImage(final int position,final ViewHolder holder){

        final ImageUploadInfo imageUploadInfo = images.get(position);
        //AbLogUtil.e(this.context,position+"路径："+image);
        final String image = imageUploadInfo.getPath();
        final String currentTag = image+"_"+position;
        //设置标记
        holder.imageTag = currentTag;

        if(!AbStrUtil.isEmpty(image)){
            if(image.indexOf("http://")!=-1){
                //图片的下载
                imageLoader.display(holder.imageView,image,this.width,this.height);

            }else if(AbStrUtil.isNumber(image)){
                //索引图片
                try {
                    int res  = Integer.parseInt(image);
                    holder.imageView.setImageDrawable(context.getResources().getDrawable(res));
                } catch (Exception e) {
                }
            }else {
                final AbTaskItem item = new AbTaskItem();
                item.setPosition(position);
                item.setListener(new AbTaskObjectListener() {
                    @Override
                    public <T> T getObject() {
                        Bitmap bitmap = AbImageUtil.getThumbnail(new File(image),width, height);
                        return (T) bitmap;
                    }

                    @Override
                    public <T> void update(T t) {
                        if (t == null) {
                            return;
                        }
                        Bitmap bitmap = (Bitmap) t;
                        if (currentTag.equals(holder.imageTag)) {
                            holder.imageView.setImageBitmap(bitmap);
                            //AbLogUtil.e("TAG", "显示：" + position+","+bitmap);
                        }
                    }
                });
                task.execute(item);
            }
        }

    }

}
