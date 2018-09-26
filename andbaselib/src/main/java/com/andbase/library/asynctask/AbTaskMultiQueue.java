package com.andbase.library.asynctask;

import java.util.ArrayList;
import java.util.List;


/**
 * Copyright amsoft.cn
 * Author 还如一梦中
 * Date 2016/6/14 17:54
 * Email 396196516@qq.com
 * Info 多任务队列
 */
public class AbTaskMultiQueue {

	/** 单例对象. */
	private static AbTaskMultiQueue taskMultiQueue = null;

	/** 最大队列数. */
	private int  maxQueueCount = 5;

	/** 请求队列. */
	private List<AbTaskQueue> taskQueueList;

	/**
	 * 构造图片下载器.
	 */
	public AbTaskMultiQueue() {
		this.taskQueueList = new ArrayList<AbTaskQueue>();
	}


	/**
	 *
	 * 获得一个实例.
	 * @return
	 */
	public static AbTaskMultiQueue getInstance() {
		if (taskMultiQueue == null) {
			taskMultiQueue = new AbTaskMultiQueue();
		}
		return taskMultiQueue;
	}

	/**
	 *
	 * 增加到最少的队列中.
	 * @param item
	 */
	public void execute(AbTaskItem item){
		int minQueueIndex = 0;
		if(taskQueueList.size() == 0){
			AbTaskQueue queue = AbTaskQueue.newInstance();
			taskQueueList.add(queue);
			queue.execute(item);
		}else{
			int minSize = 0;
			for(int i=0;i<taskQueueList.size();i++){
				AbTaskQueue queue = taskQueueList.get(i);
				int size = queue.getTaskItemListSize();
				if(i==0){
					minSize = size;
					minQueueIndex = i;
				}else{
					if(size < minSize){
						minSize = size;
						minQueueIndex = i;
					}
				}
			}
			if(taskQueueList.size() < maxQueueCount && minSize > 2){
				AbTaskQueue queue = AbTaskQueue.newInstance();
				taskQueueList.add(queue);
				queue.execute(item);
			}else{
				AbTaskQueue minQueue = taskQueueList.get(minQueueIndex);
				minQueue.execute(item);
			}

		}

		/*for(int i=0;i<taskQueueList.size();i++){
			AbTaskQueue queue = taskQueueList.get(i);
			int size = queue.getTaskItemListSize();
			AbLogUtil.i(AbImageLoader.class, "线程队列["+i+"]的任务数："+size);
		}*/

	}

	/**
	 *
	 * 释放资源.
	 */
	public void cancelAll(){
		for(int i=0;i<taskQueueList.size();i++){
			AbTaskQueue queue = taskQueueList.get(i);
			queue.cancel(true);
		}
		taskQueueList.clear();
	}

	public int getMaxQueueCount() {
		return maxQueueCount;
	}

	public void setMaxQueueCount(int maxQueueCount) {
		this.maxQueueCount = maxQueueCount;
	}
}

