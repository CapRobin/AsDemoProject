package com.demo.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.demo.R;
import com.demo.activity.MainActivity;
import com.demo.entity.ListMenu;

import java.util.List;

/**
 * Copyright © 2018 LJNG All rights reserved.
 *
 * Name：MenuListAdapter
 * Describe：案例项目适配器
 * Date：2018-07-01 18:09:54
 * Author: CapRobin@yeah.net
 *
 */
public class MenuListAdapter extends MyBaseAdapter {
    private Context mHisInfoActivity;
    private List<ListMenu> mList;

    public MenuListAdapter(Context context, List<?> data, ListView view) {
        super(context, data, R.layout.item_demo_item);
        this.mHisInfoActivity = context;
//        mList = data;

        //设置ListView线条的颜色
        view.setDivider(new ColorDrawable(Color.GRAY));
        view.setDividerHeight(1);
    }


    @Override
    protected void newView(View convertView) {
        ViewHolder viewHolder = new ViewHolder();
        viewHolder.itemImage = (ImageView) convertView.findViewById(R.id.itemImage);
        viewHolder.itemTitle = (TextView) convertView.findViewById(R.id.itemTitle);
        viewHolder.itemIntroduce = (TextView) convertView.findViewById(R.id.itemIntroduce);
        convertView.setTag(viewHolder);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void holderView(View convertView, Object itemObject) {
        ViewHolder viewHolder = (ViewHolder) convertView.getTag();

        ListMenu dto = (ListMenu) itemObject;
//        viewHolder.itemImage.setImageDrawable(context.getResources().getDrawable(dto.getIcon()));
        viewHolder.itemTitle.setText(dto.getTitle());
        viewHolder.itemIntroduce.setText(dto.getIntro());
    }

    private class ViewHolder {
        ImageView itemImage;
        TextView itemTitle;
        TextView itemIntroduce;
    }
}