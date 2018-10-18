package com.demo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.demo.R;
import com.demo.constant.Constant;
import com.demo.utils.LogUtil;
import com.demo.utils.MyApplication;
import com.demo.utils.Preferences;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.HashMap;
import java.util.Map;

/**
 * Copyright © CapRobin
 *
 * Name：UpdatePwdActivity
 * Describe：修改密码
 * Date：2018-08-31 13:57:48
 * Author: CapRobin@yeah.net
 *
 */
@ContentView(R.layout.activity_update_pwd)
public class UpdatePwdActivity extends BaseActivity {
    private static final String TAG = "UpdatePwdActivity";
    @ViewInject(R.id.title_back_igv)
    private ImageView title_back_igv;
    @ViewInject(R.id.title_tv)
    private TextView title_tv;
    @ViewInject(R.id.title_right_igv)
    private ImageView title_right_igv;
    @ViewInject(R.id.old_pwd_tv)
    private TextView old_pwd_tv;
    @ViewInject(R.id.new_pwd_tv)
    private TextView new_pwd_tv;
    @ViewInject(R.id.confirm_new_psw_tv)
    private TextView confirm_new_psw_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        initView();
    }

    /**
     * Describe：初始化配置
     * Params:
     * Return: 
     * Date：2018-08-31 14:04:19
     */
    
    private void initView() {
        title_back_igv.setVisibility(View.VISIBLE);
        title_tv.setText(getResources().getText(R.string.upda_pwd_title));
        Preferences.mPreferences.putBoolean(Constant.ISFIRSTLOGIN,true);
    }

    @Event(value = {R.id.title_back_igv,R.id.confirm_submit_btn})
    private void click(View view){
        switch (view.getId()){
            case R.id.title_back_igv:
                finish();
                break;
            case R.id.confirm_submit_btn:
                String oldPwdStr = old_pwd_tv.getText().toString().trim();
                String newPwdStr = new_pwd_tv.getText().toString().trim();
                String confirmNewPwdStr = confirm_new_psw_tv.getText().toString().trim();
                if(oldPwdStr.isEmpty()){
                    setToast("当前密码不能为空");
                    return;
                }
                if(newPwdStr.isEmpty()){
                    setToast("更改密码不能为空");
                    return;
                }
                if(newPwdStr.length() <6 || newPwdStr.length() > 20){
                    setToast("密码长度应设置在6~20位之间");
                    return;
                }
                if(!newPwdStr.equals(confirmNewPwdStr)){
                    setToast("两次密码输入不符，请重新输入");
                    return;
                }
//                String userNameStr = Preferences.mPreferences.getString(Constant.USERNAME);
                String userNameStr = MyApplication.mCustomInfo.getPHONE1();
                String meterNumStr = MyApplication.mCustomInfo.getMETER_SERIAL_NUM();
                //开始修改密码
                updatePwdRequest(Constant.UPDATEPWD,userNameStr,meterNumStr,oldPwdStr,newPwdStr);
                break;
            case 3:
                break;
            default:
                break;
        }

    }


    /**
     * Describe：更改密码请求
     * Params: [url, oldPwd, newPwd]
     * Return: void
     * Date：2018-09-05 18:33:05
     */
    private void updatePwdRequest(String url, String userName, String meterNum, String oldPwd, String newPwd) {
        //参数配置
        LogUtil.d(TAG, "\nupdatePwdRequest请求地址---------->>" + url);
        RequestParams params = new RequestParams(url);
        //Post请求参数配置
        Map<String, String> map = new HashMap<String, String>();
        map.put("userName", userName);
        map.put("meterNum", meterNum);
        map.put("oldPwd", oldPwd);
        map.put("newPwd", newPwd);
        //map.put("userInfo",userInfo);
        JSONObject jsonObject = new JSONObject(map);
        String jsonStr = jsonObject.toString();
        params.addQueryStringParameter("ngMeter", jsonStr);

        //Get请求参数设置
        //params.addQueryStringParameter("uPwd",uPwd);
        //params.addQueryStringParameter("userInfo",userInfo);
        //params.addQueryStringParameter("uName",uName);

        //开始请求
        loading.show();
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                LogUtil.d(TAG, "\nupdatePwdRequest返回数据---------->>\n" + result);
                try {
                    JSONObject jObject = new JSONObject(result);
                    int isLoginCode = Integer.valueOf(jObject.getString("errorCode").trim());
                    if (isLoginCode == 1) {
                        setToast(getResources().getString(R.string.update_pwd_success));
                        finish();
                        startActivity(new Intent(mContext,LoginActivity.class));
                    } else {
                        setToast(getResources().getString(R.string.updata_pwd_failed));
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
                    LogUtil.d(TAG, "\nupdatePwdRequest请求异常---------->>\ngetMsg=" + getMsg + "\ngetResult=" + getResult);
                } else {
                    //其他异常处理
                }
            }

            @Override
            public void onCancelled(CancelledException e) {
                LogUtil.d(TAG, "\nupdatePwdRequest取消请求---------->>onCancelled");
            }

            @Override
            public void onFinished() {
                LogUtil.d(TAG, "\nupdatePwdRequest请求结束---------->>onFinished");
                loading.dismiss();
            }
        });
    }
}
