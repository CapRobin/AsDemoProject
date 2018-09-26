
package com.andbase.library.asynctask;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import android.os.Process;
import android.util.Log;

import com.andbase.library.util.AbAppUtil;


/**
 * Copyright amsoft.cn
 * Author 还如一梦中
 * Date 2016/6/14 17:54
 * Email 396196516@qq.com
 * Info 线程工厂
 */

public class AbThreadFactory {

	/** 任务执行器. */
	public static Executor executorService = null;

    /**
     * 获取执行器.
     * @return the executor service
     */
    public static Executor getExecutorService() {
        if (executorService == null) {
            executorService = Executors.newCachedThreadPool();
        }
        Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
        return executorService;
    }

}
