package com.andbase.library.view.wheel;

import com.andbase.library.util.AbStrUtil;

import java.util.List;



/**
 * Copyright amsoft.cn
 * Author 还如一梦中
 * Date 2016/6/14 17:54
 * Email 396196516@qq.com
 * Info 轮子适配器（字符串）
 */
public class AbStringWheelAdapter implements AbWheelAdapter {
	
	/** 条目列表. */
	private List<String> items;

	/** 长度. */
	private int length = -1;

	/**
	 * 构造函数.
	 * @param items the items
	 */
	public AbStringWheelAdapter(List<String> items) {
		this.items = items;
        getMaximumLength();
	}


	@Override
	public String getItem(int index) {
		if (index >= 0 && index < items.size()) {
			return items.get(index);
		}
		return null;
	}


	@Override
	public int getItemsCount() {
		return items.size();
	}


	@Override
	public int getMaximumLength() {
		if(length!=-1){
			return length;
		}
		for(int i=0;i<items.size();i++){
			String cur = items.get(i);
			int l = AbStrUtil.strLength(cur);
			if(length<l){
                length = l;
			}
		}
		return length;
	}

}
