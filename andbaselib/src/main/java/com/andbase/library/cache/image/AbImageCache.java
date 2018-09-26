package  com.andbase.library.cache.image;

import android.graphics.Bitmap;

/**
 * Copyright amsoft.cn
 * Author 还如一梦中
 * Date 2016/6/14 17:54
 * Email 396196516@qq.com
 * Info 图片缓存接口
 */

public interface AbImageCache {
	
	/**
	 * Gets the bitmap.
	 *
	 * @param cacheKey the cache key
	 * @return the bitmap
	 */
	public Bitmap getBitmap(String cacheKey);

	/**
	 * Put bitmap.
	 *
	 * @param cacheKey the cache key
	 * @param bitmap the bitmap
	 */
	public void putBitmap(String cacheKey, Bitmap bitmap);


	/**
	 * Removes the bitmap.
	 * @param cacheKey the cacheKey
	 */
	public void removeBitmap(String cacheKey);

	/**
	 * Gets the cache key.
	 *
	 * @param requestUrl the request url
	 * @param maxWidth the max width
	 * @param maxHeight the max height
	 * @return the cache key
	 */
	public String getCacheKey(String requestUrl, int maxWidth, int maxHeight);
}
