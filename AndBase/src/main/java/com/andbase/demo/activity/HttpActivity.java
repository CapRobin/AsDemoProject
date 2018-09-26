package com.andbase.demo.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.andbase.demo.R;
import com.andbase.library.app.base.AbBaseActivity;
import com.andbase.library.http.AbHttpUtil;
import com.andbase.library.http.listener.AbBinaryHttpResponseListener;
import com.andbase.library.http.listener.AbFileHttpResponseListener;
import com.andbase.library.http.listener.AbStringHttpResponseListener;
import com.andbase.library.http.model.AbRequestParams;
import com.andbase.library.util.AbDialogUtil;
import com.andbase.library.util.AbFileUtil;
import com.andbase.library.util.AbImageUtil;
import com.andbase.library.util.AbToastUtil;
import com.andbase.library.util.AbViewUtil;
import com.andbase.library.view.dialog.AbAlertDialogFragment;
import com.andbase.library.view.progress.AbHorizontalProgressBar;

import java.io.File;

public class HttpActivity extends AbBaseActivity {

    private AbHttpUtil httpUtil = null;

    // ProgressBar进度控制
    private AbHorizontalProgressBar progressBar;
    // 最大100
    private int max = 100;
    private int progress = 0;
    private TextView numberText, maxText;

    private AbAlertDialogFragment  alertDialog;

    private AbAlertDialogFragment  uploadingDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_http);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setTitle(R.string.title_base);
        toolbar.setContentInsetsRelative(0, 0);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        httpUtil = AbHttpUtil.getInstance(this);

        Button getBtn  = (Button)this.findViewById(R.id.getBtn);
        Button getCacheBtn  = (Button)this.findViewById(R.id.getCacheBtn);
        Button postBtn  = (Button)this.findViewById(R.id.postBtn);
        Button byteBtn  = (Button)this.findViewById(R.id.byteBtn);
        Button fileDownBtn  = (Button)this.findViewById(R.id.fileBtn);
        Button fileUploadBtn  = (Button)this.findViewById(R.id.fileUploadBtn);


        //get请求
        getBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // 一个url地址
                String url = "http://www.baidu.com";

                httpUtil.get(url,new AbStringHttpResponseListener() {

                    //获取数据成功会调用这里
                    @Override
                    public void onSuccess(int statusCode, String content) {

                        View view =  View.inflate(HttpActivity.this,R.layout.view_alert_dialog,null);
                        AbViewUtil.setText(view,R.id.dialog_title,"提示:"+statusCode);
                        AbViewUtil.setText(view,R.id.dialog_message,content);
                        Button button =  AbViewUtil.findViewById(view,R.id.dialog_button);
                        View linearLayout1=  AbViewUtil.findViewById(view,R.id.btn_layout_1);
                        View linearLayout2=  AbViewUtil.findViewById(view,R.id.btn_layout_2);
                        linearLayout1.setVisibility(View.VISIBLE);
                        linearLayout2.setVisibility(View.GONE);
                        button.setText("确定");
                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                AbDialogUtil.removeDialog(HttpActivity.this);
                            }
                        });

                        AbDialogUtil.showAlertDialog(view);
                    }

                    // 失败，调用
                    @Override
                    public void onFailure(int statusCode, String content,
                                          Throwable error) {

                        AbToastUtil.showToast(HttpActivity.this,error.getMessage());
                    }

                    // 开始执行前
                    @Override
                    public void onStart() {
                        //显示进度框
                        AbDialogUtil.showProgressDialog(HttpActivity.this,0,"正在查询...");
                    }


                    // 完成后调用，失败，成功
                    @Override
                    public void onFinish() {
                        AbDialogUtil.removeDialog(HttpActivity.this);
                    };

                });

            }
        });

        //带缓存的get请求
        getCacheBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // 一个url地址
                String url = "http://www.baidu.com";

                httpUtil.getWithCache(url, new AbStringHttpResponseListener() {

                    //获取数据成功会调用这里
                    @Override
                    public void onSuccess(int statusCode, String content) {
                        View view =  View.inflate(HttpActivity.this,R.layout.view_alert_dialog,null);
                        AbViewUtil.setText(view,R.id.dialog_title,"提示:"+statusCode);
                        AbViewUtil.setText(view,R.id.dialog_message,content);
                        Button button =  AbViewUtil.findViewById(view,R.id.dialog_button);
                        View linearLayout1=  AbViewUtil.findViewById(view,R.id.btn_layout_1);
                        View linearLayout2=  AbViewUtil.findViewById(view,R.id.btn_layout_2);
                        linearLayout1.setVisibility(View.VISIBLE);
                        linearLayout2.setVisibility(View.GONE);
                        button.setText("确定");
                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                AbDialogUtil.removeDialog(HttpActivity.this);
                            }
                        });

                        AbDialogUtil.showAlertDialog(view);
                    }

                    // 失败，调用
                    @Override
                    public void onFailure(int statusCode, String content,
                                          Throwable error) {
                        AbToastUtil.showToast(HttpActivity.this,error.getMessage());
                    }

                    // 开始执行前
                    @Override
                    public void onStart() {
                        //显示进度框
                        AbDialogUtil.showProgressDialog(HttpActivity.this,0,"正在查询...");
                    }


                    // 完成后调用，失败，成功
                    @Override
                    public void onFinish() {
                        AbDialogUtil.removeDialog(HttpActivity.this);
                    };

                });
            }
        });

        //post请求
        postBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String url = "http://www.baidu.com";
                // 绑定参数
                AbRequestParams params = new AbRequestParams();
                params.put("phone", "15150509567");
                params.put("param2", "2");
                params.put("param3", "10");
                httpUtil.post(url,params, new AbStringHttpResponseListener() {

                    // 获取数据成功会调用这里
                    @Override
                    public void onSuccess(int statusCode, String content) {
                        View view =  View.inflate(HttpActivity.this,R.layout.view_alert_dialog,null);
                        AbViewUtil.setText(view,R.id.dialog_title,"提示:"+statusCode);
                        AbViewUtil.setText(view,R.id.dialog_message,content);
                        Button button =  AbViewUtil.findViewById(view,R.id.dialog_button);
                        View linearLayout1=  AbViewUtil.findViewById(view,R.id.btn_layout_1);
                        View linearLayout2=  AbViewUtil.findViewById(view,R.id.btn_layout_2);
                        linearLayout1.setVisibility(View.VISIBLE);
                        linearLayout2.setVisibility(View.GONE);
                        button.setText("确定");
                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                AbDialogUtil.removeDialog(HttpActivity.this);
                            }
                        });

                        AbDialogUtil.showAlertDialog(view);

                    };

                    // 开始执行前
                    @Override
                    public void onStart() {
                        //显示进度框
                        AbDialogUtil.showProgressDialog(HttpActivity.this,0,"正在查询...");
                    }

                    // 失败，调用
                    @Override
                    public void onFailure(int statusCode, String content,
                                          Throwable error) {
                        AbToastUtil.showToast(HttpActivity.this,error.getMessage());
                    }

                    // 完成后调用，失败，成功
                    @Override
                    public void onFinish() {
                        AbDialogUtil.removeDialog(HttpActivity.this);
                    };

                });
            }
        });

        //字节数组(图片或文件也可)下载
        byteBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String url = "http://img4.duitang.com/uploads/item/201405/28/20140528203047_y5Swe.thumb.700_0.jpeg";
                httpUtil.get(url, new AbBinaryHttpResponseListener() {

                    // 获取数据成功会调用这里
                    @Override
                    public void onSuccess(int statusCode, byte[] content) {

                        Bitmap bitmap = AbImageUtil.bytes2Bimap(content);
                        ImageView view = new ImageView(HttpActivity.this);
                        view.setImageBitmap(bitmap);
                        AbDialogUtil.showAlertDialog(view);
                    }

                    // 开始执行前
                    @Override
                    public void onStart() {
                        //打开进度框
                        View view = LayoutInflater.from(HttpActivity.this).inflate(R.layout.progress_bar_horizontal, null, false);
                        progressBar = (AbHorizontalProgressBar) view.findViewById(R.id.progressBar);
                        numberText = (TextView) view.findViewById(R.id.numberText);
                        maxText = (TextView) view.findViewById(R.id.maxText);

                        maxText.setText(progress+"/"+String.valueOf(max));
                        progressBar.setMax(max);
                        progressBar.setProgress(progress);

                        alertDialog = AbDialogUtil.showAlertDialog(view);
                    }

                    // 失败，调用
                    @Override
                    public void onFailure(int statusCode, String content,
                                          Throwable error) {
                        AbToastUtil.showToast(HttpActivity.this,error.getMessage());
                    }

                    // 下载进度
                    @Override
                    public void onProgress(long bytesWritten, long totalSize) {
                        maxText.setText(bytesWritten/(totalSize/max)+"/"+max);
                        progressBar.setProgress((int)(bytesWritten/(totalSize/max)));
                    }

                    // 完成后调用，失败，成功
                    @Override
                    public void onFinish() {
                        AbDialogUtil.removeDialog(HttpActivity.this);
                    };

                });
            }
        });

        //文件下载
        fileDownBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String url = "http://img4.duitang.com/uploads/item/201405/28/20140528203047_y5Swe.thumb.700_0.jpeg";

                httpUtil.get(url, new AbFileHttpResponseListener() {


                    // 获取数据成功会调用这里
                    @Override
                    public void onSuccess(int statusCode, File file) {

                        Bitmap bitmap = AbFileUtil.getBitmapFromSD(file);
                        ImageView view = new ImageView(HttpActivity.this);
                        view.setImageBitmap(bitmap);

                        AbDialogUtil.showAlertDialog(view);
                    }

                    // 开始执行前
                    @Override
                    public void onStart() {
                        //打开进度框
                        View view = LayoutInflater.from(HttpActivity.this).inflate(R.layout.progress_bar_horizontal, null, false);
                        progressBar = (AbHorizontalProgressBar) view.findViewById(R.id.progressBar);
                        numberText = (TextView) view.findViewById(R.id.numberText);
                        maxText = (TextView) view.findViewById(R.id.maxText);

                        maxText.setText(progress+"/"+String.valueOf(max));
                        progressBar.setMax(max);
                        progressBar.setProgress(progress);

                        alertDialog = AbDialogUtil.showAlertDialog(view);
                    }

                    // 失败，调用
                    @Override
                    public void onFailure(int statusCode, String content,
                                          Throwable error) {

                        AbToastUtil.showToast(HttpActivity.this,error.getMessage());
                    }

                    // 下载进度
                    @Override
                    public void onProgress(long bytesWritten, long totalSize) {
                        maxText.setText(bytesWritten/(totalSize/max)+"/"+max);
                        progressBar.setProgress((int)(bytesWritten/(totalSize/max)));
                    }

                    // 完成后调用，失败，成功
                    public void onFinish() {
                        AbDialogUtil.removeDialog(HttpActivity.this);
                    };

                });
            }
        });

        //文件上传
        fileUploadBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //已经在后台上传
                if(uploadingDialog!=null){
                    AbDialogUtil.showDialog(HttpActivity.this,uploadingDialog);
                    return;
                }
                String url = "http://192.168.1.124:8080/demo/upload.do";

                AbRequestParams params = new AbRequestParams();

                try {
                    //多文件上传添加多个即可
                    File pathRoot = Environment.getExternalStorageDirectory();
                    String path = pathRoot.getAbsolutePath();
                    params.put("data1","你好");
                    params.put("data2","100");
                    //参数随便加，在sd卡根目录放图片
                    File file1 = new File(path+"/1.jpg");
                    File file2 = new File(path+"/1.txt");
                    //文件名称可能是中文
                    params.put(file1.getName(),file1);
                    params.put(file2.getName(),file2);

                    //注意：框架默认将所有参数：URLDecoder.encode(fileName,HTTP.UTF_8)
                    //所以服务端要解析中文要用到 URLDecoder.decode(fileName,HTTP.UTF_8)

                    //只支持最多2个文件域，因为会产生流中断的异常，所以你需要传递更多，请分次数上传
                    //File file3 = new File(path+"/3.log");
                    //File file4 = new File(path+"/1.jpg");
                    //params.put(file3.getName(),file3);
                    //params.put(file4.getName(),file4);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                httpUtil.post(url, params, new AbStringHttpResponseListener() {


                    @Override
                    public void onSuccess(int statusCode, String content) {

                        AbToastUtil.showToast(HttpActivity.this,"onSuccess");
                    }

                    // 开始执行前
                    @Override
                    public void onStart() {
                        //打开进度框
                        View view = LayoutInflater.from(HttpActivity.this).inflate(R.layout.progress_bar_horizontal, null, false);
                        progressBar = (AbHorizontalProgressBar) view.findViewById(R.id.progressBar);
                        numberText = (TextView) view.findViewById(R.id.numberText);
                        maxText = (TextView) view.findViewById(R.id.maxText);

                        maxText.setText(progress+"/"+String.valueOf(max));
                        progressBar.setMax(max);
                        progressBar.setProgress(progress);

                        uploadingDialog = AbDialogUtil.showAlertDialog(view);
                    }

                    @Override
                    public void onFailure(int statusCode, String content,
                                          Throwable error) {

                        AbToastUtil.showToast(HttpActivity.this,error.getMessage());
                    }

                    // 进度
                    @Override
                    public void onProgress(long bytesWritten, long totalSize) {
                        maxText.setText(bytesWritten/(totalSize/max)+"/"+max);
                        progressBar.setProgress((int)(bytesWritten/(totalSize/max)));
                    }

                    // 完成后调用，失败，成功，都要调用
                    public void onFinish() {
                        AbDialogUtil.removeDialog(HttpActivity.this);
                    };

                });
            }
        });

    }

}
