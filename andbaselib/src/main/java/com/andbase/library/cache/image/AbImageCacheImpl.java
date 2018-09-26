package  com.andbase.library.cache.image;


import android.content.Context;
import android.graphics.Bitmap;
import android.util.LruCache;

import com.andbase.library.cache.disk.AbDiskCacheEntry;
import com.andbase.library.cache.disk.AbDiskCacheImpl;
import com.andbase.library.cache.http.AbHttpCacheResponse;
import com.andbase.library.config.AbAppConfig;
import com.andbase.library.image.AbImageLoader;
import com.andbase.library.util.AbImageUtil;
import com.andbase.library.util.AbLogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright amsoft.cn
 * Author 还如一梦中
 * Date 2016/6/14 17:54
 * Email 396196516@qq.com
 * Info 图片缓存实现类
 */

public class AbImageCacheImpl implements AbImageCache {

    /**
     * LruCache.
     */
    private static LruCache<String, Bitmap> lruCache;

    /**
     * 待释放的bitmap.
     */
    private static List<Bitmap> releaseBitmapList;

    /**
     * 磁盘缓存.
     */
    public AbDiskCacheImpl diskCache;

    /**
     * 构造方法.
     */
    public AbImageCacheImpl(Context context) {
        super();
        int maxSize = AbAppConfig.MAX_CACHE_SIZE_INBYTES;
        releaseBitmapList = new ArrayList<Bitmap>();
        lruCache = new LruCache<String, Bitmap>(maxSize) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getRowBytes() * value.getHeight();
            }

            @Override
            protected void entryRemoved(boolean evicted, String key, Bitmap oldValue, Bitmap newValue) {
                //
                AbLogUtil.e(AbImageCacheImpl.class, "entryRemoved key:" + key + oldValue);
                releaseBitmapList.add(oldValue);

            }

        };

        this.diskCache = AbDiskCacheImpl.getInstance(context);
    }

    /**
     * 根据key获取缓存中的Bitmap.
     *
     * @param cacheKey the cache key
     * @return the bitmap
     */
    @Override
    public Bitmap getBitmap(String cacheKey) {
        return lruCache.get(cacheKey);
    }

    /**
     * 增加一个Bitmap到缓存中.
     *
     * @param cacheKey the cache key
     * @param bitmap   the bitmap
     */
    @Override
    public void putBitmap(String cacheKey, Bitmap bitmap) {
        lruCache.put(cacheKey, bitmap);
    }

    /**
     * 从缓存中删除一个Bitmap.
     *
     * @param cacheKey the cacheKey
     */
    @Override
    public void removeBitmap(String cacheKey) {
        lruCache.remove(cacheKey);
    }

    /**
     * 释放所有缓存.
     */
    public void clearBitmap() {
        lruCache.evictAll();
    }


    /**
     * 获取用于缓存的Key.
     *
     * @param url       the request url
     * @param maxWidth  the max width
     * @param maxHeight the max height
     * @return the cache key
     */
    public String getCacheKey(String url, int maxWidth, int maxHeight) {
        return new StringBuilder(url.length() + 12).append("#W").append(maxWidth)
                .append("#H").append(maxHeight).append(url).toString();
    }

    /**
     * 获取AbBitmapResponse.
     *
     * @param url
     * @param desiredWidth
     * @param desiredHeight
     * @return
     */
    public AbBitmapResponse getBitmapResponse(String url, int desiredWidth, int desiredHeight,boolean lruCache) {
        AbBitmapResponse bitmapResponse = null;
        try {
            final String cacheKey = getCacheKey(url, desiredWidth, desiredHeight);
            Bitmap bitmap = null;
            //看磁盘
            AbDiskCacheEntry entry = diskCache.get(cacheKey);
            if (entry == null || entry.isExpired()) {
                if (entry == null) {
                    AbLogUtil.i(AbImageLoader.class, "磁盘中没有这个图片");
                } else {
                    if (entry.isExpired()) {
                        AbLogUtil.i(AbImageLoader.class, "磁盘中图片已经过期");
                    }
                }

                AbHttpCacheResponse response = diskCache.getCacheResponse(url, null);
                if (response != null && response.data != null && response.data.length > 0) {
                    bitmap = AbImageUtil.getBitmap(response.data, desiredWidth, desiredHeight);
                    if (bitmap != null) {
                        putBitmap(cacheKey, bitmap);
                        AbLogUtil.i(AbImageLoader.class, "图片缓存成功");
                        diskCache.put(cacheKey, diskCache.parseCacheHeaders(response, AbAppConfig.DISK_CACHE_EXPIRES_TIME));
                    }
                }
            } else {
                //磁盘中有
                byte[] bitmapData = entry.data;
                bitmap = AbImageUtil.getBitmap(bitmapData, desiredWidth, desiredHeight);
                if(lruCache){
                    putBitmap(cacheKey, bitmap);
                }

            }

            bitmapResponse = new AbBitmapResponse(url);
            bitmapResponse.setBitmap(bitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bitmapResponse;
    }

    /**
     * 释放已经被移除的Bitmap
     */
    public void releaseRemovedBitmap(){
        AbImageUtil.releaseBitmapList(releaseBitmapList);
        releaseBitmapList.clear();
    }
}
