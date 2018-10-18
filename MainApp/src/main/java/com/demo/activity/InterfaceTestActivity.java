package com.demo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.demo.R;
import com.demo.adapter.MyAdapter;
import com.demo.entity.Icon;
import com.demo.utils.LogUtil;
import com.demo.utils.MethodUtil;
import com.demo.utils.Sha256Util;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

@ContentView(R.layout.activity_interface_test)
public class InterfaceTestActivity extends BaseActivity{

    private static final String TAG = "InterfaceTestActivity";
//    SimpleDateFormat
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
    private Date date = null;
    private MyAdapter<Icon> mAdapter;
    private ArrayList mData;
    private String host = "http://192.168.2.211:8080/lggmr/";
    private String dateStr = "";
    private String token = "";

    @ViewInject(R.id.title_back_igv)
    private ImageView title_back_igv;
    @ViewInject(R.id.title_tv)
    private TextView title_tv;
    @ViewInject(R.id.title_right_igv)
    private ImageView title_right_igv;
    @ViewInject(R.id.showText)
    private TextView showText;
    @ViewInject(R.id.showTextTitle)
    private TextView showTextTitle;
    @ViewInject(R.id.grid_photo)
    private GridView grid_photo;
    @ViewInject(R.id.resultLayout)
    private ScrollView resultLayout;


    @Event(value = {R.id.title_back_igv,R.id.confirmLayout,R.id.title_right_igv})
    private void Click(View view){
        switch (view.getId()){
            case R.id.title_back_igv:
                finish();
                break;
            case R.id.title_right_igv:
                startActivity(new Intent(mContext,XutilDbTestActivity.class));
                break;
            default:
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        initView();
    }

    /**
     * Describe：配置初始化
     * Params:
     * Return: 
     * Date：2018-09-21 11:10:24
     */
    private void initView() {
        title_back_igv.setVisibility(View.VISIBLE);
        title_right_igv.setVisibility(View.VISIBLE);
        title_tv.setText("接口测试");
        grid_photo = (GridView) findViewById(R.id.grid_photo);
        mData = new ArrayList<Icon>();
        //通用设置
        mData.add(new Icon(R.drawable.interface_bg, "新用户用水及安装信息"));
        mData.add(new Icon(R.drawable.interface_bg, "居民信息变更记录"));
        mData.add(new Icon(R.drawable.interface_bg, "获取更换水表信息"));
        mData.add(new Icon(R.drawable.interface_bg, "获取抄读的记录"));
        mData.add(new Icon(R.drawable.interface_bg, "笑话大全"));
        mData.add(new Icon(R.drawable.interface_bg, "菜谱大全"));
        mAdapter = new MyAdapter<Icon>(mData, R.layout.item_grid) {
            @Override
            public void bindView(MyAdapter.ViewHolder holder, Icon obj) {
                holder.setImageResource(R.id.img_icon, obj.getiId());
                holder.setText(R.id.txt_icon, obj.getiName());
//                holder.setText()
            }
        };

        grid_photo.setAdapter(mAdapter);
        grid_photo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        date = new Date(System.currentTimeMillis());
                        dateStr = dateFormat.format(date);
                        token = Sha256Util.getSHA256StrJava("jingyuan1"+dateStr);
                        getNewCustomerInfor(host+"getNewCustomerInfor",token,dateStr);
                        break;
                    case 1:
                        date = new Date(System.currentTimeMillis());
                        dateStr = dateFormat.format(date);
                        token = Sha256Util.getSHA256StrJava("jingyuan1"+dateStr);
                        getChangedCustomerInfor(host+"getChangedCustomerInfor",token,dateStr);
                        break;
                    case 2:
                        date = new Date(System.currentTimeMillis());
                        dateStr = dateFormat.format(date);
                        token = Sha256Util.getSHA256StrJava("jingyuan1"+dateStr);
                        getChangedMeterInfor(host+"getChangedMeterInfor",token,dateStr);
                        break;
                    case 3:
                        date = new Date(System.currentTimeMillis());
                        dateStr = dateFormat.format(date);
                        token = Sha256Util.getSHA256StrJava("jingyuan1"+dateStr);
                        getMeterData(host+"getMeterData",token,dateStr);
                        break;
                    case 4:
                        //sort=asc&page=1&pagesize=20&time=1518816972&key=09a278604f39a6887f87b26b15b51bd5
                        getJokebook("asc","1","20","1518816972","09a278604f39a6887f87b26b15b51bd5");
                        break;
                    case 5:
                        //sort=asc&page=1&pagesize=20&time=1518816972&key=09a278604f39a6887f87b26b15b51bd5
                        getMenuebook("炒面","json","","30","","c6f3f39da135c670bcc179f910fb0f68");
                        break;
                    default:
                        break;
                }
            }
        });
    }

    /**
     * Describe：获得新开户用水居民及水表安装信息
     * Params:
     * Return: 
     * Date：2018-09-21 11:29:12
     */
    private void getNewCustomerInfor(String url,String token, String date) {
        //参数配置
        LogUtil.d(TAG, "\ngetNewCustomerInfor请求地址---------->>" + url);
        RequestParams params = new RequestParams(url);
        //Get请求参数配置
        params.addQueryStringParameter("token",token);
        params.addQueryStringParameter("date",date);

        //开始请求
        loading.show();
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                LogUtil.d(TAG, "\ngetNewCustomerInfor返回数据---------->>\n" + MethodUtil.formatJson(result));
                try {
                    JSONObject jObject = new JSONObject(result);
                    int errorCode = jObject.getInt("status");
//                    //请求成功
                    if (errorCode == 0) {
                        resultLayout.setVisibility(View.VISIBLE);
                        showText.setText("");
                        showTextTitle.setText("GetNewCustomerInfor请求数据");
                        showText.setText(MethodUtil.formatJson(result));
                    }else {
                        resultLayout.setVisibility(View.GONE);
                        setToast("请求数据失败");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable throwable, boolean b) {
                if (throwable instanceof HttpException) {
                    HttpException he = (HttpException) throwable;
                    int stateCode = he.getCode();
                    String getMsg = he.getMessage();
                    String getResult = he.getResult();
                    LogUtil.d(TAG, "\ngetNewCustomerInfor请求异常---------->>\ngetMsg=" + getMsg + "\ngetResult=" + getResult);
                } else {
                    //其他异常处理
                }
            }

            @Override
            public void onCancelled(CancelledException e) {
                LogUtil.d(TAG, "\ngetNewCustomerInfor取消请求---------->>onCancelled");
            }

            @Override
            public void onFinished() {
                LogUtil.d(TAG, "\ngetNewCustomerInfor请求结束---------->>onFinished");
                loading.dismiss();
            }
        });
    }


    /**
     * Describe：获得居民信息变更的记录
     * Params:
     * Return:
     * Date：2018-09-21 11:29:12
     */
    private void getChangedCustomerInfor(String url,String token, String date) {
        //参数配置
        LogUtil.d(TAG, "\ngetChangedCustomerInfor请求地址---------->>" + url);
        RequestParams params = new RequestParams(url);
        //Get请求参数配置
        params.addQueryStringParameter("token",token);
        params.addQueryStringParameter("date",date);

        //开始请求
        loading.show();
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                LogUtil.d(TAG, "\ngetChangedCustomerInfor返回数据---------->>\n" + MethodUtil.formatJson(result));
                try {
                    JSONObject jObject = new JSONObject(result);
                    int errorCode = jObject.getInt("status");
//                    //请求成功
                    if (errorCode == 0) {
                        resultLayout.setVisibility(View.VISIBLE);
                        showText.setText("");
                        showTextTitle.setText("GetChangedCustomerInfor请求数据");
                        showText.setText(MethodUtil.formatJson(result));
                    }else {
                        resultLayout.setVisibility(View.GONE);
                        setToast("请求数据失败");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable throwable, boolean b) {
                if (throwable instanceof HttpException) {
                    HttpException he = (HttpException) throwable;
                    int stateCode = he.getCode();
                    String getMsg = he.getMessage();
                    String getResult = he.getResult();
                    LogUtil.d(TAG, "\ngetChangedCustomerInfor请求异常---------->>\ngetMsg=" + getMsg + "\ngetResult=" + getResult);
                } else {
                    //其他异常处理
                }
            }

            @Override
            public void onCancelled(CancelledException e) {
                LogUtil.d(TAG, "\ngetChangedCustomerInfor取消请求---------->>onCancelled");
            }

            @Override
            public void onFinished() {
                LogUtil.d(TAG, "\ngetChangedCustomerInfor请求结束---------->>onFinished");
                loading.dismiss();
            }
        });
    }


    /**
     * Describe：获取更换水表的信息
     * Params:
     * Return:
     * Date：2018-09-21 11:29:12
     */
    private void getChangedMeterInfor(String url,String token, String date) {
        //参数配置
        LogUtil.d(TAG, "\ngetChangedMeterInfor请求地址---------->>" + url);
        RequestParams params = new RequestParams(url);
        //Get请求参数配置
        params.addQueryStringParameter("token",token);
        params.addQueryStringParameter("date",date);

        //开始请求
        loading.show();
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                LogUtil.d(TAG, "\ngetChangedMeterInfor返回数据---------->>\n" + MethodUtil.formatJson(result));
                try {
                    JSONObject jObject = new JSONObject(result);
                    int errorCode = jObject.getInt("status");
//                    //请求成功
                    if (errorCode == 0) {
                        resultLayout.setVisibility(View.VISIBLE);
                        showText.setText("");
                        showTextTitle.setText("GetChangedMeterInfor请求数据");
                        showText.setText(MethodUtil.formatJson(result));
                    }else {
                        resultLayout.setVisibility(View.GONE);
                        setToast("请求数据失败");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable throwable, boolean b) {
                if (throwable instanceof HttpException) {
                    HttpException he = (HttpException) throwable;
                    int stateCode = he.getCode();
                    String getMsg = he.getMessage();
                    String getResult = he.getResult();
                    LogUtil.d(TAG, "\ngetChangedMeterInfor请求异常---------->>\ngetMsg=" + getMsg + "\ngetResult=" + getResult);
                } else {
                    //其他异常处理
                }
            }

            @Override
            public void onCancelled(CancelledException e) {
                LogUtil.d(TAG, "\ngetChangedMeterInfor取消请求---------->>onCancelled");
            }

            @Override
            public void onFinished() {
                LogUtil.d(TAG, "\ngetChangedMeterInfor请求结束---------->>onFinished");
                loading.dismiss();
            }
        });
    }


    /**
     * Describe：获取抄读的记录
     * Params:
     * Return:
     * Date：2018-09-21 11:29:12
     */
    private void getMeterData(String url,String token, String date) {
        //参数配置
        LogUtil.d(TAG, "\ngetMeterData请求地址---------->>" + url);
        RequestParams params = new RequestParams(url);
        //Get请求参数配置
        params.addQueryStringParameter("token",token);
        params.addQueryStringParameter("date",date);

        //开始请求
        loading.show();
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                LogUtil.d(TAG, "\ngetMeterData返回数据---------->>\n" + MethodUtil.formatJson(result));
                try {
                    JSONObject jObject = new JSONObject(result);
                    int errorCode = jObject.getInt("status");
//                    //请求成功
                    if (errorCode == 0) {
                        resultLayout.setVisibility(View.VISIBLE);
                        showText.setText("");
                        showTextTitle.setText("GetMeterData请求数据");
                        showText.setText(MethodUtil.formatJson(result));
                    }else {
                        resultLayout.setVisibility(View.GONE);
                        setToast("请求数据失败");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable throwable, boolean b) {
                if (throwable instanceof HttpException) {
                    HttpException he = (HttpException) throwable;
                    int stateCode = he.getCode();
                    String getMsg = he.getMessage();
                    String getResult = he.getResult();
                    LogUtil.d(TAG, "\ngetMeterData请求异常---------->>\ngetMsg=" + getMsg + "\ngetResult=" + getResult);
                } else {
                    //其他异常处理
                }
            }

            @Override
            public void onCancelled(CancelledException e) {
                LogUtil.d(TAG, "\ngetMeterData取消请求---------->>onCancelled");
            }

            @Override
            public void onFinished() {
                LogUtil.d(TAG, "\ngetMeterData请求结束---------->>onFinished");
                loading.dismiss();
            }
        });
    }

    /**
     * Describe：笑话大全
     * Params: [sort：排序类型，desc:指定时间之前发布的，asc:指定时间之后发布的]
     * Params: [page：当前页数,默认1]
     * Params: [pagesize：每次返回条数,默认1,最大20]
     * Params: [time：时间戳（10位），如：1418816972]
     * Params: [key：接口调用AppKey]
     * Return: void
     * Date：2018-10-18 08:54:01
     */
    private void getJokebook(String sort,String page, String pagesize, String time, String key) {
        String url = "http://v.juhe.cn/joke/content/list.php";
        //参数配置
        LogUtil.d(TAG, "\ngetMeterData请求地址---------->>" + url);
        RequestParams params = new RequestParams(url);
        //Get请求参数配置
        params.addQueryStringParameter("sort",sort);
        params.addQueryStringParameter("page",page);
        params.addQueryStringParameter("pagesize",pagesize);
        params.addQueryStringParameter("time",time);
        params.addQueryStringParameter("key",key);

        //开始请求
        loading.show();
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                LogUtil.d(TAG, "\ngetJokebook返回数据---------->>\n" + MethodUtil.formatJson(result));
                try {
                    JSONObject jObject = new JSONObject(result);
                    int errorCode = jObject.getInt("error_code");
//                    //请求成功
                    if (errorCode == 0) {
                        resultLayout.setVisibility(View.VISIBLE);
                        showText.setText("");
                        showTextTitle.setText("getJokebook请求数据");
                        showText.setText(MethodUtil.formatJson(result));
                    }else {
                        resultLayout.setVisibility(View.GONE);
                        setToast("请求数据失败");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable throwable, boolean b) {
                if (throwable instanceof HttpException) {
                    HttpException he = (HttpException) throwable;
                    int stateCode = he.getCode();
                    String getMsg = he.getMessage();
                    String getResult = he.getResult();
                    LogUtil.d(TAG, "\ngetJokebook请求异常---------->>\ngetMsg=" + getMsg + "\ngetResult=" + getResult);
                } else {
                    //其他异常处理
                }
            }

            @Override
            public void onCancelled(CancelledException e) {
                LogUtil.d(TAG, "\ngetJokebook取消请求---------->>onCancelled");
            }

            @Override
            public void onFinished() {
                LogUtil.d(TAG, "\ngetJokebook请求结束---------->>onFinished");
                loading.dismiss();
            }
        });
    }

    /**
     * Describe：菜谱大全
     * Params: [menu：需要查询的菜谱名]
     * Params: [dtype：返回数据的格式,xml或json，默认json]
     * Params: [pn：数据返回起始下标]
     * Params: [rn：数据返回条数，最大30]
     * Params: [albums：albums字段类型，1字符串，默认数组]
     * Params: [key：AppKey]
     * Return: void
     * Date：2018-10-18 08:54:01
     */
    private void getMenuebook(String menu,String dtype, String pn, String rn, String albums, String key) {
        String url = "http://apis.juhe.cn/cook/query.php";
        //参数配置
        LogUtil.d(TAG, "\ngetMenuebook请求地址---------->>" + url);
        RequestParams params = new RequestParams(url);
        //Get请求参数配置
        params.addQueryStringParameter("menu",menu);
        params.addQueryStringParameter("dtype",dtype);
        params.addQueryStringParameter("pn",pn);
        params.addQueryStringParameter("rn",rn);
        params.addQueryStringParameter("albums",albums);
        params.addQueryStringParameter("key",key);

        //开始请求
        loading.show();
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                LogUtil.d(TAG, "\ngetMenuebook返回数据---------->>\n" + MethodUtil.formatJson(result));
                try {
                    JSONObject jObject = new JSONObject(result);
                    int errorCode = jObject.getInt("error_code");
//                    //请求成功
                    if (errorCode == 0) {
                        resultLayout.setVisibility(View.VISIBLE);
                        showText.setText("");
                        showTextTitle.setText("getJokebook请求数据");
                        showText.setText(MethodUtil.formatJson(result));
                    }else {
                        resultLayout.setVisibility(View.GONE);
                        setToast("请求数据失败");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable throwable, boolean b) {
                if (throwable instanceof HttpException) {
                    HttpException he = (HttpException) throwable;
                    int stateCode = he.getCode();
                    String getMsg = he.getMessage();
                    String getResult = he.getResult();
                    LogUtil.d(TAG, "\ngetMenuebook请求异常---------->>\ngetMsg=" + getMsg + "\ngetResult=" + getResult);
                } else {
                    //其他异常处理
                }
            }

            @Override
            public void onCancelled(CancelledException e) {
                LogUtil.d(TAG, "\ngetMenuebook取消请求---------->>onCancelled");
            }

            @Override
            public void onFinished() {
                LogUtil.d(TAG, "\ngetMenuebook请求结束---------->>onFinished");
                loading.dismiss();
            }
        });
    }
}
