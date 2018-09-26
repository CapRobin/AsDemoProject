package com.andbase.library.cache.disk;

import java.util.Collections;
import java.util.Map;

/**
 * Copyright amsoft.cn
 * Author 还如一梦中
 * Date 2016/6/14 17:54
 * Email 396196516@qq.com
 * Info 磁盘缓存接口
 */

public interface AbDiskCache {

    /**
     * Retrieves an entry from the cache.
     * @param key Cache key
     * @return An {@link AbDiskCacheEntry} or null in the event of a cache miss
     */
    public AbDiskCacheEntry get(String key);

    /**
     * Adds or replaces an entry to the cache.
     * @param key Cache key
     * @param entry Data to store and metadata for cache coherency, TTL, etc.
     */
    public void put(String key, AbDiskCacheEntry entry);

    /**
     * Performs any potentially long-running actions needed to initialize the cache;
     * will be called from a worker thread.
     */
    public void initialize();


    /**
     * Removes an entry from the cache.
     * @param key Cache key
     */
    public void remove(String key);

    /**
     * Empties the cache.
     */
    public void clear();


    /**
     * 获取缓存的Key.
     * @param url
     * @return key
     */
    public String getCacheKey(String url);



}
