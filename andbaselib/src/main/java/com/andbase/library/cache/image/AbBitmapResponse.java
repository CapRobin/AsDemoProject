package  com.andbase.library.cache.image;

import android.graphics.Bitmap;

// TODO: Auto-generated Javadoc
/**
 * 
 * © 2012 amsoft.cn
 * 名称：BitmapResponse.java 
 * 描述：响应实体
 * @author 还如一梦中
 * @date 2015年4月3日 上午9:49:43
 * @version v1.0
 */
public class AbBitmapResponse {
	
	/** Bitmap实体. */
	private Bitmap bitmap;
	
	/** 请求URL. */
	private String requestURL;

	/**
	 * 
	 * 构造.
	 * @param requestURL
	 */
	public AbBitmapResponse(String requestURL) {
		super();
		this.requestURL = requestURL;
	}

	/**
	 * 获取Bitmap.
	 *
	 * @return the bitmap
	 */
	public Bitmap getBitmap() {
		return bitmap;
	}

	/**
	 * 设置Bitmap.
	 *
	 * @param bitmap the new bitmap
	 */
	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}

	/**
	 * 获取请求的URL.
	 *
	 * @return the request url
	 */
	public String getRequestURL() {
		return requestURL;
	}

	/**
	 * 设置请求的URL.
	 *
	 * @param requestURL the new request url
	 */
	public void setRequestURL(String requestURL) {
		this.requestURL = requestURL;
	}

}
