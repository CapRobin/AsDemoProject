package com.andbase.demo.global;

import android.webkit.JavascriptInterface;

public interface AndH5Interface {

	/**拍照，js回调 takePictureCallBack(localPath) */
	@JavascriptInterface
	public abstract boolean takePicture(int cameraId, int orientation);
	
	/**下载文件*/
	@JavascriptInterface
	public abstract String getPictureFile(String requestUrl, String localPath);
	
	/**开始录像 ,js回调 videoRecoderCallback(localPath)*/
	@JavascriptInterface
	public abstract boolean startVideoRecoder(int cameraId, int orientation);
	
	/**获取录制的文件*/
	@JavascriptInterface
	public abstract String getVideoRecoderFile(String requestUrl, String localPath);
	
}
