package com.andbase.library.http;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;


import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.andbase.library.asynctask.AbTask;
import com.andbase.library.asynctask.AbTaskItem;
import com.andbase.library.asynctask.AbTaskListener;
import com.andbase.library.cache.disk.AbDiskCacheEntry;
import com.andbase.library.cache.disk.AbDiskCacheImpl;
import com.andbase.library.cache.http.AbHttpCacheResponse;
import com.andbase.library.config.AbAppConfig;
import com.andbase.library.http.model.AbHttpException;
import com.andbase.library.http.entity.mine.content.StringBody;
import com.andbase.library.http.listener.AbBinaryHttpResponseListener;
import com.andbase.library.http.listener.AbFileHttpResponseListener;
import com.andbase.library.http.listener.AbHttpResponseListener;
import com.andbase.library.http.listener.AbStringHttpResponseListener;
import com.andbase.library.http.model.AbHttpStatus;
import com.andbase.library.http.model.AbJsonParams;
import com.andbase.library.http.model.AbOutputStreamProgress;
import com.andbase.library.http.model.AbRequestParams;
import com.andbase.library.http.ssl.NoSSLTrustManager;
import com.andbase.library.util.AbAppUtil;
import com.andbase.library.util.AbFileUtil;
import com.andbase.library.util.AbLogUtil;
import com.andbase.library.util.AbStrUtil;

import org.apache.http.HttpEntity;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

/**
 * Copyright amsoft.cn
 * Author 还如一梦中
 * Date 2016/6/14 17:54
 * Email 396196516@qq.com
 * Info Http执行工具类，可处理get，post，以及异步处理文件的上传下载
 */

public class AbHttpUtil {

    /** 上下文. */
    private static Context context;

    /** 请求类型. */
    private static final String HTTP_GET = "GET";
    private static final String HTTP_POST = "POST";

    /** Session Id. */
    private String sessionId  = null;

    /** 磁盘缓存. */
    private AbDiskCacheImpl diskCache = null;

    /**################# HTTP Message常量################################*/
    /** 成功. */
    public static final int SUCCESS_MESSAGE = 0;

    /** 失败. */
    public static final int FAILURE_MESSAGE = 1;

    /** 开始. */
    public static final int START_MESSAGE = 4;

    /** 进行中. */
    public static final int PROGRESS_MESSAGE = 6;

    /** 任务. */
    private List<AbTask> taskList = null;

    /**
     * 构造函数，初始化.
     * @param context the context
     */
    public AbHttpUtil(Context context) {
        this.context = context;
        this.diskCache = AbDiskCacheImpl.getInstance(context);
        this.taskList = new ArrayList<AbTask>();
        this.sessionId = AbAppConfig.sessionId;
    }

    /**
     * 获取实例.
     *
     * @param context the context
     * @return single instance of AbHttpUtil
     */
    public static AbHttpUtil getInstance(Context context){
        return new AbHttpUtil(context);
    }


    /**
     * 发送get请求.
     *
     * @param url the url
     * @param responseListener the response listener
     */
    public AbTask get(final String url,final AbHttpResponseListener responseListener){
       return get(url,null,responseListener);
    }


    /**
     * 发送get请求.
     * @param url the url
     * @param params the params
     * @param responseListener the response listener
     */
    public AbTask get(final String url,final AbRequestParams params,final AbHttpResponseListener responseListener) {

        responseListener.setHandler(new ResponderHandler(responseListener));
        responseListener.onStart();
        AbLogUtil.e(context,"[HTTP]:onStart:" + url);
        AbTask task = AbTask.newInstance();
        taskList.add(task);
        AbTaskItem item = new AbTaskItem();
        item.setListener(new AbTaskListener(){
            @Override
            public void get() {
                try {
                    AbLogUtil.e(context,"[HTTP]:execute start...");
                    doRequest(url,HTTP_GET,params,responseListener);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        });
        task.execute(item);
        return task;
    }

    /**
     * 发送post请求.
     * @param url the url
     * @param responseListener the response listener
     */
    public AbTask post(final String url,final AbHttpResponseListener responseListener) {
        return post(url,null,responseListener);
    }

    /**
     * 发送post请求.
     * @param url the url
     * @param params the params
     * @param responseListener the response listener
     */
    public AbTask post(final String url,final AbRequestParams params,final AbHttpResponseListener responseListener) {
        responseListener.setHandler(new ResponderHandler(responseListener));
        responseListener.onStart();
        AbLogUtil.e(context,"[HTTP]:onStart:" + url);
        final AbTask task = AbTask.newInstance();
        taskList.add(task);
        AbTaskItem item = new AbTaskItem();
        item.setListener(new AbTaskListener(){
            @Override
            public void get() {
                try {
                    AbLogUtil.e(context,"[HTTP]:execute start...");
                    doRequest(url,HTTP_POST,params,responseListener);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void update() {
                taskList.remove(task);
            }
        });
        task.execute(item);

        return task;
    }

    /**
     * 发送get请求(有缓存).
     *
     * @param url the url
     * @param responseListener the response listener
     */
    public AbTask getWithCache(final String url,final AbHttpResponseListener responseListener) {
        return getWithCache(url,responseListener);
    }


    /**
     * 发送get请求(有缓存).
     *
     * @param url the url
     * @param params the params
     * @param responseListener the response listener
     */
    public AbTask getWithCache(final String url,final AbRequestParams params,final AbHttpResponseListener responseListener) {

        this.sessionId = AbAppConfig.sessionId;
        responseListener.setHandler(new ResponderHandler(responseListener));
        responseListener.onStart();
        final AbTask task = AbTask.newInstance();
        taskList.add(task);
        AbTaskItem item = new AbTaskItem();
        item.setListener(new AbTaskListener(){

            @Override
            public void update() {
                taskList.remove(task);
            }


            @Override
            public void get() {
                try {
                    AbLogUtil.e(context,"[HTTP]:execute start...");
                    String httpUrl = url;
                    //HttpGet连接对象
                    if(params!=null){
                        if(url.indexOf("?")==-1){
                            httpUrl += "?";
                        }
                        httpUrl += params.getParamString();
                    }

                    //查看本地缓存
                    final String cacheKey = diskCache.getCacheKey(httpUrl);

                    //看磁盘
                    AbDiskCacheEntry entry = diskCache.get(cacheKey);

                    if(!AbAppUtil.isNetworkAvailable(context)){
                        //没网络

                        if(entry == null){
                            //缓存不存在
                            AbLogUtil.i(AbHttpUtil.class, "无网络，磁盘中无缓存文件");

                            Thread.sleep(200);
                            responseListener.sendFailureMessage(AbHttpStatus.CONNECT_FAILURE_CODE,AbAppConfig.CONNECT_EXCEPTION, new AbHttpException(AbHttpStatus.CONNECT_FAILURE_CODE,AbAppConfig.CONNECT_EXCEPTION));
                            return;
                        }else{
                            AbLogUtil.i(AbHttpUtil.class, "无网络，磁盘中有缓存文件");
                            //磁盘中有数据
                            byte [] httpData = entry.data;
                            String responseBody = new String(httpData);
                            ((AbStringHttpResponseListener)responseListener).sendSuccessMessage(AbHttpStatus.SUCCESS_CODE, responseBody);
                            AbLogUtil.i(context, "[HTTP GET CACHED]:"+httpUrl+",result："+responseBody);

                        }
                    }else{

                        //有网络先下载，下载失败返回缓存
                        AbHttpCacheResponse response = diskCache.getCacheResponse(httpUrl,sessionId);

                        if(response!=null){
                            String responseBody = new String(response.data);
                            AbLogUtil.i(context, "[HTTP GET]:"+httpUrl+",result："+responseBody);
                            AbDiskCacheEntry entryNew = diskCache.parseCacheHeaders(response,AbAppConfig.DISK_CACHE_EXPIRES_TIME);
                            if(entryNew!=null){
                                diskCache.put(cacheKey,entryNew);
                                AbLogUtil.i(context, "HTTP 缓存成功");
                            }else{
                                AbLogUtil.i(context, "HTTP 缓存失败，parseCacheHeaders失败");
                            }

                            ((AbStringHttpResponseListener)responseListener).sendSuccessMessage(AbHttpStatus.SUCCESS_CODE, responseBody);
                        }else{
                            if(entry == null){
                                //缓存不存在
                                AbLogUtil.i(AbHttpUtil.class, "有网络，磁盘中无缓存文件");
                                responseListener.sendFailureMessage(AbHttpStatus.SERVER_FAILURE_CODE, AbAppConfig.REMOTE_SERVICE_EXCEPTION,new AbHttpException(AbHttpStatus.SERVER_FAILURE_CODE, AbAppConfig.REMOTE_SERVICE_EXCEPTION));
                            }else{
                                if(entry.isExpired()){
                                    //缓存过期
                                    AbLogUtil.i(AbHttpUtil.class, "有网络，磁盘中缓存已经过期");
                                    responseListener.sendFailureMessage(AbHttpStatus.SERVER_FAILURE_CODE, AbAppConfig.REMOTE_SERVICE_EXCEPTION,new AbHttpException(AbHttpStatus.SERVER_FAILURE_CODE, AbAppConfig.REMOTE_SERVICE_EXCEPTION));
                                }else{
                                    //磁盘中有数据
                                    byte [] httpData = entry.data;
                                    String responseBody = new String(httpData);
                                    ((AbStringHttpResponseListener)responseListener).sendSuccessMessage(AbHttpStatus.SUCCESS_CODE, responseBody);
                                    AbLogUtil.i(context, "[HTTP GET CACHED]:"+httpUrl+",result："+responseBody);
                                }

                            }

                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        });
        task.execute(item);

        return task;
    }

    /**
     * 发送Json POST请求
     * @param url
     * @return
     */
    public AbTask postJson(final String url, final AbStringHttpResponseListener responseListener) {
        return postJson(url,responseListener);
    }


    /**
     * 发送Json POST请求
     * @param url
     * @param params
     * @return
     */
    public AbTask postJson(final String url, final AbJsonParams params, final AbStringHttpResponseListener responseListener) {

        this.sessionId = AbAppConfig.sessionId;
        responseListener.setHandler(new ResponderHandler(responseListener));
        responseListener.onStart();
        final AbTask task = AbTask.newInstance();
        taskList.add(task);
        AbTaskItem item = new AbTaskItem();
        item.setListener(new AbTaskListener(){

            @Override
            public void update() {
                taskList.remove(task);
            }

            @Override
            public void get() {
                HttpURLConnection httpURLConnection = null;
                InputStream inputStream = null;
                try {
                    if(!AbAppUtil.isNetworkAvailable(context)){
                        Thread.sleep(200);
                        responseListener.sendFailureMessage(AbHttpStatus.CONNECT_FAILURE_CODE,AbAppConfig.CONNECT_EXCEPTION, new AbHttpException(AbHttpStatus.CONNECT_FAILURE_CODE,AbAppConfig.CONNECT_EXCEPTION));
                        return;
                    }

                    String resultString = null;
                    httpURLConnection = openConnection(url);
                    httpURLConnection.setRequestMethod(HTTP_POST);
                    if(!AbStrUtil.isEmpty(sessionId)){
                        httpURLConnection.setRequestProperty("Cookie", "JSESSIONID="+sessionId);
                    }
                    httpURLConnection.setConnectTimeout(AbAppConfig.DEFAULT_CONNECT_TIMEOUT);
                    httpURLConnection.setReadTimeout(AbAppConfig.DEFAULT_READ_TIMEOUT);
                    httpURLConnection.setDoOutput(true);
                    StringBody body = null;
                    if(params!=null){
                        httpURLConnection.setRequestProperty("connection", "keep-alive");
                        httpURLConnection.setRequestProperty("Content-Type", "application/json");
                        body = StringBody.create(params.getJson(),"application/json", Charset.forName("UTF-8"));
                        body.writeTo(httpURLConnection.getOutputStream(),null);
                    }else{
                        httpURLConnection.connect();
                    }
                    if(body!=null){
                        AbLogUtil.i(context, "[HTTP POST]:"+url+",body:"+params.getJson());
                    }else{
                        AbLogUtil.i(context, "[HTTP POST]:"+url+",body:无");
                    }

                    int code = httpURLConnection.getResponseCode();
                    if (code == AbHttpStatus.SUCCESS_CODE){
                        inputStream = httpURLConnection.getInputStream();
                        resultString = readString(httpURLConnection,responseListener,true);
                        responseListener.sendSuccessMessage(AbHttpStatus.SUCCESS_CODE, resultString);
                    }else{
                        inputStream = httpURLConnection.getErrorStream();
                        resultString = readString(httpURLConnection,null,false);
                        AbHttpException exception = new AbHttpException(code,resultString);
                        responseListener.sendFailureMessage(exception.getCode(),exception.getMessage(),exception);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    AbLogUtil.i(context, "[HTTP POST]:"+url+",error："+e.getMessage());
                    //发送失败消息
                    AbHttpException exception = new AbHttpException(e);
                    responseListener.sendFailureMessage(exception.getCode(),exception.getMessage(),exception);
                } finally {
                    try{
                        if(inputStream!=null){
                            inputStream.close();
                        }
                    }catch(Exception e){
                    }
                    if (httpURLConnection != null) {
                        httpURLConnection.disconnect();
                    }
                }
            }

        });
        task.execute(item);
        taskList.add(task);
        return task;

    }

    /**
     * 发送get/post请求
     * @param url
     * @param params
     * @param responseListener
     */
    public void doRequest(final String url,final String requestMethod, final AbRequestParams params, final AbHttpResponseListener responseListener) {
        this.sessionId = AbAppConfig.sessionId;
        HttpURLConnection httpURLConnection = null;
        InputStream inputStream = null;

        try {
            if(!AbAppUtil.isNetworkAvailable(context)){
                Thread.sleep(200);
                responseListener.sendFailureMessage(AbHttpStatus.CONNECT_FAILURE_CODE,AbAppConfig.CONNECT_EXCEPTION, new AbHttpException(AbHttpStatus.CONNECT_FAILURE_CODE,AbAppConfig.CONNECT_EXCEPTION));
                return;
            }

            httpURLConnection = openConnection(url);
            httpURLConnection.setRequestMethod(requestMethod);
            httpURLConnection.setUseCaches(false);
            if(!AbStrUtil.isEmpty(sessionId)){
                httpURLConnection.setRequestProperty("Cookie", "JSESSIONID="+sessionId);
            }

            httpURLConnection.setConnectTimeout(AbAppConfig.DEFAULT_CONNECT_TIMEOUT);
            httpURLConnection.setReadTimeout(AbAppConfig.DEFAULT_READ_TIMEOUT);
            httpURLConnection.setDoOutput(true);

            httpURLConnection.setRequestProperty("connection", "keep-alive");
            if(params != null){
                //使用NameValuePair来保存要传递的Post参数设置字符集
                HttpEntity httpEntity = params.getEntity();
                //请求httpRequest
                if(params.getFileParams().size()>0){
                    httpURLConnection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + params.boundaryString());
                    long totalSize = httpEntity.getContentLength();
                    httpEntity.writeTo(new AbOutputStreamProgress(httpURLConnection.getOutputStream(),totalSize,responseListener,true));
                }else{
                    //没文件
                    httpURLConnection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
                    httpEntity.writeTo(httpURLConnection.getOutputStream());
                }
            }else{
                //没参数
                httpURLConnection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
                httpURLConnection.connect();
            }

            int code = httpURLConnection.getResponseCode();
            AbLogUtil.e(context,"[HTTP]:Response Code = " + code);
            if (code == AbHttpStatus.SUCCESS_CODE){
                AbLogUtil.e(context,"[HTTP]:onSuccess");
                if(responseListener instanceof AbStringHttpResponseListener){
                    //字符串
                    AbStringHttpResponseListener stringHttpResponseListener =  (AbStringHttpResponseListener)responseListener;
                    String resultString = readString(httpURLConnection ,responseListener,true);
                    stringHttpResponseListener.sendSuccessMessage(AbHttpStatus.SUCCESS_CODE, resultString);

                }else if(responseListener instanceof AbBinaryHttpResponseListener){
                    //字节
                    AbBinaryHttpResponseListener binaryHttpResponseListener =  (AbBinaryHttpResponseListener)responseListener;
                    byte[] resultByte = readByteArray(httpURLConnection ,responseListener,true);
                    binaryHttpResponseListener.sendSuccessMessage(AbHttpStatus.SUCCESS_CODE, resultByte);

                }else if(responseListener instanceof AbFileHttpResponseListener){
                    //文件
                    AbFileHttpResponseListener fileHttpResponseListener =  (AbFileHttpResponseListener)responseListener;
                    String fileName = AbFileUtil.getCacheFileNameFromUrl(url, httpURLConnection);
                    writeToFile(context,httpURLConnection,fileName,fileHttpResponseListener,true);
                    fileHttpResponseListener.sendSuccessMessage(AbHttpStatus.SUCCESS_CODE, fileName);
                }

            }else{
                inputStream = httpURLConnection.getErrorStream();
                String resultString = readString(httpURLConnection,null,false);
                if(AbStrUtil.isEmpty(resultString)){
                    if(code ==404){
                        resultString = AbAppConfig.NOT_FOUND_EXCEPTION;
                    }else if(code == 500){
                        resultString = AbAppConfig.REMOTE_SERVICE_EXCEPTION;
                    }else{
                        resultString = AbAppConfig.UNTREATED_EXCEPTION;
                    }
                }
                AbHttpException exception = new AbHttpException(code,resultString);
                responseListener.sendFailureMessage(exception.getCode(),exception.getMessage(),exception);
                AbLogUtil.e(context,"[HTTP]:onFailure");
            }

        } catch (Exception e) {
            e.printStackTrace();
            AbLogUtil.e(context, "[HTTP POST]:"+url+",error："+e.getMessage());
            //发送失败消息
            AbHttpException exception = new AbHttpException(e);
            responseListener.sendFailureMessage(exception.getCode(),exception.getMessage(),exception);
        } finally {
            try{
                if(inputStream!=null){
                    inputStream.close();
                }
            }catch(Exception e){
            }
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
        }
    }

    /**
     * 发送get/post请求(无线程)
     * @param url
     * @param params
     * @param responseListener
     */
    public void doRequestWithoutThread(final String url,final String requestMethod, final AbRequestParams params, final AbHttpResponseListener responseListener) {
        this.sessionId = AbAppConfig.sessionId;
        HttpURLConnection httpURLConnection = null;
        InputStream inputStream = null;
        responseListener.onStart();
        AbLogUtil.e(context,"[HTTP]:onStart:" + url);
        try {
            if(!AbAppUtil.isNetworkAvailable(context)){
                Thread.sleep(200);
                responseListener.onFailure(AbHttpStatus.CONNECT_FAILURE_CODE,AbAppConfig.CONNECT_EXCEPTION, new AbHttpException(AbHttpStatus.CONNECT_FAILURE_CODE,AbAppConfig.CONNECT_EXCEPTION));
                return;
            }
            httpURLConnection = openConnection(url);
            httpURLConnection.setRequestMethod(requestMethod);
            httpURLConnection.setUseCaches(false);
            if(!AbStrUtil.isEmpty(sessionId)){
                httpURLConnection.setRequestProperty("Cookie", "JSESSIONID="+sessionId);
            }
            httpURLConnection.setConnectTimeout(AbAppConfig.DEFAULT_CONNECT_TIMEOUT);
            httpURLConnection.setReadTimeout(AbAppConfig.DEFAULT_READ_TIMEOUT);
            httpURLConnection.setDoOutput(true);

            httpURLConnection.setRequestProperty("connection", "keep-alive");
            if(params != null){
                //使用NameValuePair来保存要传递的Post参数设置字符集
                HttpEntity httpEntity = params.getEntity();
                //请求httpRequest
                if(params.getFileParams().size()>0){
                    httpURLConnection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + params.boundaryString());
                    long totalSize = httpEntity.getContentLength();
                    httpEntity.writeTo(new AbOutputStreamProgress(httpURLConnection.getOutputStream(),totalSize,responseListener,false));
                }else{
                    //没文件
                    httpURLConnection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
                    httpEntity.writeTo(httpURLConnection.getOutputStream());
                }
            }else{
                //没参数
                httpURLConnection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
                httpURLConnection.connect();
            }

            int code = httpURLConnection.getResponseCode();
            AbLogUtil.e(context,"[HTTP]:Response Code = " + code);
            if (code == AbHttpStatus.SUCCESS_CODE){
                AbLogUtil.e(context,"[HTTP]:onSuccess");
                if(responseListener instanceof AbStringHttpResponseListener){
                    //字符串
                    String resultString = readString(httpURLConnection ,responseListener,false);
                    AbStringHttpResponseListener stringHttpResponseListener =  (AbStringHttpResponseListener)responseListener;
                    stringHttpResponseListener.onSuccess(AbHttpStatus.SUCCESS_CODE, resultString);

                }else if(responseListener instanceof AbBinaryHttpResponseListener){
                    //字节
                    byte[] resultByte = readByteArray(httpURLConnection ,responseListener,false);
                    AbBinaryHttpResponseListener binaryHttpResponseListener =  (AbBinaryHttpResponseListener)responseListener;
                    binaryHttpResponseListener.onSuccess(AbHttpStatus.SUCCESS_CODE, resultByte);

                }else if(responseListener instanceof AbFileHttpResponseListener){
                    //文件
                    String fileName = AbFileUtil.getCacheFileNameFromUrl(url, httpURLConnection);
                    AbFileHttpResponseListener fileHttpResponseListener =  (AbFileHttpResponseListener)responseListener;
                    writeToFile(context,httpURLConnection,fileName,fileHttpResponseListener,false);

                    fileHttpResponseListener.onSuccess(AbHttpStatus.SUCCESS_CODE, fileName);
                }

            }else{
                inputStream = httpURLConnection.getErrorStream();
                String resultString = readString(httpURLConnection,null,false);
                if(AbStrUtil.isEmpty(resultString)){
                    if(code == 404){
                        resultString = AbAppConfig.NOT_FOUND_EXCEPTION;
                    }else if(code == 500){
                        resultString = AbAppConfig.REMOTE_SERVICE_EXCEPTION;
                    }else{
                        resultString = AbAppConfig.UNTREATED_EXCEPTION;
                    }
                }
                AbHttpException exception = new AbHttpException(code,resultString);
                AbLogUtil.e(context,"[HTTP]:onFailure");
                responseListener.onFailure(exception.getCode(),exception.getMessage(),exception);
            }
            AbLogUtil.e(context,"[HTTP]:onFinish");
            responseListener.onFinish();
        } catch (Exception e) {
            e.printStackTrace();
            AbLogUtil.e(context, "[HTTP POST]:"+url+",error："+e.getMessage());
            //发送失败消息
            AbHttpException exception = new AbHttpException(e);
            responseListener.onFailure(exception.getCode(),exception.getMessage(),exception);
        } finally {
            try{
                if(inputStream!=null){
                    inputStream.close();
                }
            }catch(Exception e){
            }
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
        }
    }


    /**
     * 将流的数据写入文件并回调进度.
     * @param context the context
     * @param httpURLConnection the httpURLConnection
     * @param name the name
     * @param responseListener the response listener
     */
    private void writeToFile(Context context,HttpURLConnection httpURLConnection,String name,AbFileHttpResponseListener responseListener,boolean isThread){

        if(httpURLConnection == null){
            return;
        }

        responseListener.setFile(context,name);

        InputStream inStream = null;
        FileOutputStream outStream = null;
        try {
            inStream = httpURLConnection.getInputStream();
            int contentLength = httpURLConnection.getContentLength();
            outStream = new FileOutputStream(responseListener.getFile());
            if (inStream != null) {

                byte[] tmp = new byte[1024];
                int l, count = 0;
                while ((l = inStream.read(tmp)) != -1 && !Thread.currentThread().isInterrupted()) {
                    count += l;
                    outStream.write(tmp, 0, l);
                    if(isThread){
                        responseListener.sendProgressMessage(count, contentLength);
                    }else{
                        responseListener.onProgress(count,contentLength);
                    }
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        } finally {
            try {
                if(inStream!=null){
                    inStream.close();
                }
                if(outStream!=null){
                    outStream.flush();
                    outStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 从流中读取字符串
     * @param httpURLConnection
     * @param responseListener
     * @return
     */
    private String readString(HttpURLConnection httpURLConnection,AbHttpResponseListener responseListener,boolean isThread) {
        StringBuffer stringBuffer = new StringBuffer();
        try {
            InputStream is = httpURLConnection.getInputStream();
            if (is != null) {
                //一定要是字符流  否则中文被截取成字节后会坏掉
                InputStreamReader isr = new InputStreamReader(is,"UTF-8");
                int contentLength = httpURLConnection.getContentLength();
                int len, count = 0;
                char[] buffer = new char[1024];
                while((len = isr.read(buffer)) > 0){
                    count += len;
                    if(responseListener!=null){
                        if(isThread){
                            responseListener.sendProgressMessage(count, contentLength);
                        }else{
                            responseListener.onProgress(count,contentLength);
                        }

                    }
                    stringBuffer.append(new String(buffer, 0,len));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return stringBuffer.toString();
    }

    /**
     * 从流中获取字节数据
     * @param httpURLConnection
     * @param responseListener
     * @return
     */
    private byte[] readByteArray(HttpURLConnection httpURLConnection,AbHttpResponseListener responseListener,boolean isThread) {

        InputStream inStream = null;
        ByteArrayOutputStream outSteam = null;
        try {
            inStream = httpURLConnection.getInputStream();
            outSteam = new ByteArrayOutputStream();
            int contentLength = httpURLConnection.getContentLength();
            if (inStream != null) {
                int l, count = 0;
                byte[] buffer = new byte[1024];
                while((l = inStream.read(buffer))!=-1){
                    count += l;
                    outSteam.write(buffer,0,l);
                    if(isThread){
                        responseListener.sendProgressMessage(count, contentLength);
                    }else{
                        responseListener.onProgress(count,contentLength);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            try {
                if(inStream!=null){
                    inStream.close();
                }
                if(outSteam!=null){
                    outSteam.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return outSteam.toByteArray();

    }

    /**
     * 响应处理
     */
    private static class ResponderHandler extends Handler {

        /** 响应数据. */
        private Object[] response;

        /** 响应消息监听. */
        private AbHttpResponseListener responseListener;

        /**
         * 响应消息处理.
         *
         * @param responseListener the response listener
         */
        public ResponderHandler(AbHttpResponseListener responseListener) {
            this.responseListener = responseListener;
        }

        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {

                case SUCCESS_MESSAGE:

                    response = (Object[]) msg.obj;

                    if (response != null){
                        if(responseListener instanceof AbStringHttpResponseListener){
                            AbStringHttpResponseListener stringHttpResponseListener =  (AbStringHttpResponseListener)responseListener;
                            if(response.length >= 2){
                                stringHttpResponseListener.onSuccess((Integer) response[0],(String)response[1]);
                            }else{
                                AbLogUtil.i(context, "SUCCESS_MESSAGE "+AbAppConfig.MISSING_PARAMETERS);
                            }

                        }else if(responseListener instanceof AbBinaryHttpResponseListener){
                            AbBinaryHttpResponseListener binaryHttpResponseListener =  (AbBinaryHttpResponseListener)responseListener;
                            if(response.length >= 2){
                                binaryHttpResponseListener.onSuccess((Integer) response[0],(byte[])response[1]);
                            }else{
                                AbLogUtil.i(context, "SUCCESS_MESSAGE "+AbAppConfig.MISSING_PARAMETERS);
                            }
                        }else if(responseListener instanceof AbFileHttpResponseListener){
                            AbFileHttpResponseListener fileHttpResponseListener =  (AbFileHttpResponseListener)responseListener;
                            if(response.length >= 1){
                                fileHttpResponseListener.onSuccess((Integer) response[0],fileHttpResponseListener.getFile());
                            }else{
                                AbLogUtil.i(context, "SUCCESS_MESSAGE "+AbAppConfig.MISSING_PARAMETERS);
                            }

                        }
                    }
                    AbLogUtil.e(context,"[HTTP]:onFinish");
                    responseListener.onFinish();
                    break;
                case FAILURE_MESSAGE:

                    response = (Object[]) msg.obj;
                    if (response != null && response.length >= 3){
                        //异常转换为可提示的
                        AbHttpException exception = new AbHttpException((Exception) response[2]);
                        responseListener.onFailure((Integer) response[0], (String) response[1], exception);
                    }else{
                        AbLogUtil.i(context, "FAILURE_MESSAGE "+AbAppConfig.MISSING_PARAMETERS);
                    }
                    AbLogUtil.e(context,"[HTTP]:onFinish");
                    responseListener.onFinish();

                    break;
                case START_MESSAGE:
                    responseListener.onStart();
                    break;
                case PROGRESS_MESSAGE:
                    response = (Object[]) msg.obj;
                    if (response != null && response.length >= 2){
                        responseListener.onProgress((Long) response[0], (Long) response[1]);
                    }else{
                        AbLogUtil.i(context, "PROGRESS_MESSAGE "+AbAppConfig.MISSING_PARAMETERS);
                    }
                    break;
                default:
                    break;
            }
        }

    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
        AbAppConfig.sessionId = sessionId;
    }

    /**
     * 发送get请求（无线程）.
     *
     * @param url the url
     * @param params the params
     * @param responseListener the response listener
     */
    public void getWithoutThread(final String url,final AbRequestParams params,final AbHttpResponseListener responseListener) {
        doRequestWithoutThread(url,HTTP_GET,params,responseListener);
    }

    /**
     * 发送post请求（无线程）.
     *
     * @param url the url
     * @param params the params
     * @param responseListener the response listener
     */
    public void postWithoutThread(final String url,final AbRequestParams params,final AbHttpResponseListener responseListener) {
        doRequestWithoutThread(url,HTTP_POST,params,responseListener);
    }

    /**
     * 取消当前所有
     */
    public void cancelCurrentTask(){
        try{
            AbTask task = null;
            for(int i =0;i<taskList.size();i++){
                task = taskList.get(i);
                task.cancel(true);
                taskList.remove(task);
                i--;
            }
            AbLogUtil.e("AbHttpUtil","[AbHttpUtil]取消了当前任务");
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 获取连接 自验证  验证
     * @param url
     * @return
     * @throws Exception
     */
    public static HttpURLConnection openConnection(String url) throws Exception {
        HttpURLConnection httpURLConnection = null;
        URL requestUrl = new URL(url);
        if (!requestUrl.getProtocol().toLowerCase().equals("https")) {
            httpURLConnection = (HttpURLConnection) requestUrl.openConnection();
            return httpURLConnection;
        }

        if(AbAppConfig.trustMode == 0){
            TrustManager[] trustAllCerts = new TrustManager[1];
            TrustManager tm = new NoSSLTrustManager();
            trustAllCerts[0] = tm;
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, null);
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier((HostnameVerifier) tm);

        }else if(AbAppConfig.trustMode == 1){
            SSLContext sslContext = SSLContext.getInstance("TLS");
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(getKeyStore(AbAppConfig.caRes));
            sslContext.init( null, trustManagerFactory.getTrustManagers(), new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {

                @Override
                public boolean verify(String hostname, SSLSession sslsession) {

                    if("localhost".equals(hostname)){
                        return true;
                    } else {
                        return false;
                    }
                }
            });

        }

        httpURLConnection = (HttpURLConnection) requestUrl.openConnection();
        return httpURLConnection;

    }


    public static KeyStore getKeyStore(int raw){
        KeyStore keyStore = null;
        InputStream inputStream = null;
        try {
            keyStore = KeyStore.getInstance("BKS");
            // 从资源文件中读取你自己创建的那个包含证书的 keystore 文件
            //这个参数改成你的 keystore 文件名
            inputStream = context.getResources().openRawResource(raw);
            // 用 keystore 的密码跟证书初始化 trusted
            keyStore.load(inputStream, AbAppConfig.caPassword.toCharArray());
        }catch(Exception e){
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return keyStore;
    }


}
