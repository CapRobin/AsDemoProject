package com.andbase.library.http.model;

import com.andbase.library.http.listener.AbHttpResponseListener;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Copyright amsoft.cn
 * Author 还如一梦中
 * Date 2016/9/23 17:54
 * Email 396196516@qq.com
 * Info 带进度的输出流
 */
public class AbOutputStreamProgress extends OutputStream {

    private OutputStream outputStream;
    private long bytesWritten = 0;
    private long totalSize = 0;
    private boolean isThread = false;
    private AbHttpResponseListener responseListener;

    public AbOutputStreamProgress(OutputStream outputStream,long totalSize , AbHttpResponseListener responseListener,boolean isThread) {
        this.outputStream = outputStream;
        this.responseListener = responseListener;
        this.totalSize = totalSize;
        this.isThread = isThread;
    }
    @Override
    public void write(int b) throws IOException {
        outputStream.write(b);
        bytesWritten++;
        if(isThread){
            responseListener.sendProgressMessage(bytesWritten, totalSize);
        }else{
            responseListener.onProgress(bytesWritten,totalSize);
        }

    }
    @Override
    public void write(byte[] b) throws IOException {
        outputStream.write(b);
        bytesWritten += b.length;
        if(isThread){
            responseListener.sendProgressMessage(bytesWritten, totalSize);
        }else{
            responseListener.onProgress(bytesWritten,totalSize);
        }
    }
    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        outputStream.write(b, off, len);
        bytesWritten += len;
        if(isThread){
            responseListener.sendProgressMessage(bytesWritten, totalSize);
        }else{
            responseListener.onProgress(bytesWritten,totalSize);
        }
    }
    @Override
    public void flush() throws IOException {
        outputStream.flush();
    }
    @Override
    public void close() throws IOException {
        outputStream.close();
    }
}