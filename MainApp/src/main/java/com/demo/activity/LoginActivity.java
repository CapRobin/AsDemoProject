package com.demo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;

import com.demo.R;
import com.demo.constant.Constant;
import com.demo.entity.CustomInfo;
import com.demo.entity.MeterInfo;
import com.demo.utils.LogUtil;
import com.demo.utils.MyApplication;
import com.demo.utils.Preferences;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Copyright © CapRobin
 * <p>
 * Name：LoginActivity
 * Describe：用户登录页面
 * Date：2018-08-28 18:33:39
 * Author: CapRobin@yeah.net
 */
@ContentView(R.layout.activity_login)
public class LoginActivity extends BaseActivity {
    private static final String TAG = "LoginActivity";
//    private List<MeterInfoModel> data;
    private ArrayList<MeterInfo> meterInfoList;
    private ArrayList<CustomInfo> mCustomInfos;
    private int isGetMeterInfoCode = 0;
    private int isLoginCode = 0;

    @ViewInject(R.id.login_image)
    private ImageView login_image;
    @ViewInject(R.id.login_uname)
    private EditText login_uname;
    @ViewInject(R.id.login_psw)
    private EditText login_psw;
    @ViewInject(R.id.is_show_pwd)
    private CheckBox is_show_pwd;
//    @ViewInject(R.id.is_admin)
//    private CheckBox is_admin;
    @ViewInject(R.id.is_remember_pwd)
    private CheckBox is_remember_pwd;
    @ViewInject(R.id.login_btn)
    private Button login_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        mContext = this;
        x.view().inject(this);
        //ButterKnife.bind(this);(ButterKnife引用)
        //加载配置
        initView();
    }

    /**
     * Describe：配置初始化
     * Params:
     * Return:
     * Date：2018-08-31 09:06:47
     */
    private void initView() {
//        data = new ArrayList<MeterInfoModel>();
        String uName = Preferences.mPreferences.getString(Constant.USERNAME);
        String uPwd = Preferences.mPreferences.getString(Constant.USERPASSWORD);
//        boolean isAdminLogin = Preferences.mPreferences.getBoolean(Constant.ISADMINLOGIN);
        boolean isRememberPwd = Preferences.mPreferences.getBoolean(Constant.ISREMEMBERPWD);
        //是否记住密码
        if (isRememberPwd) {
            //记住密码
            login_uname.setText(uName);
            login_psw.setText(uPwd);
            is_remember_pwd.setChecked(true);

            //是否是管理登录
//            if (isAdminLogin) {
//                //管理登录
//                MyApplication.userType = Constant.UserTypeAdmin;
//                is_admin.setChecked(true);
//            } else {
//                //客户登录
//                MyApplication.userType = Constant.UserTypeNormal;
//                is_admin.setChecked(false);
//            }
        } else {
            //未记住密码
            is_remember_pwd.setChecked(false);
//            is_admin.setChecked(false);
//            MyApplication.userType = Constant.UserTypeNormal;
//            Preferences.mPreferences.putBoolean(Constant.ISADMINLOGIN, is_admin.isChecked());

            //客户登录
        }

        //密码是否显示
        is_show_pwd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    //如果选中，显示密码
                    login_psw.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    //否则隐藏密码
                    login_psw.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });
        //管理是否登录
//        is_admin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                if (b) {
//                    //不选中：用户登录(userType = 1)
//                    MyApplication.userType = Constant.UserTypeAdmin;
//                } else {
//                    //已选中：管理登录(userType = 2)
//                    MyApplication.userType = Constant.UserTypeNormal;
//                }
//            }
//        });
    }

    /**
     * Describe：事件处理
     * Params:
     * Return:
     * Date：2018-09-04 10:22:15
     */
    @Event(value = {R.id.login_btn})
    private void OnClick(View view) {
        switch (view.getId()) {
            case R.id.login_btn:
                String uNameStr = login_uname.getText().toString().trim();
                String uPwdStr = login_psw.getText().toString().trim();
                //输入及配置检测
                if (!isInputConfig(uNameStr, uPwdStr)) {
                    return;
                }
//                loginRequest(Constant.LOGIN, uNameStr, uPwdStr, null);
                startActivity(new Intent(mContext, MainActivity.class));

                //保存登录信息到本地
                Preferences.mPreferences.putString(Constant.USERNAME, uNameStr);
                Preferences.mPreferences.putString(Constant.USERPASSWORD, uPwdStr);
//                Preferences.mPreferences.putBoolean(Constant.ISADMINLOGIN, is_admin.isChecked());
                Preferences.mPreferences.putBoolean(Constant.ISREMEMBERPWD, is_remember_pwd.isChecked());
                finish();
                LogUtil.d(TAG, "\nloginRequest保存用户信息---------->>成功");
                break;
        }
    }

    /**
     * Describe：登录请求
     * Params: [uName, uPwd]
     * Return: void
     * Date：2018-08-31 08:49:55
     */
//    private void loginRequest(String url, final String uName, String uPwd, String userInfo) {
//        final String name = uName;
//        final String password = uPwd;
//        //参数配置
//        LogUtil.d(TAG, "\nloginRequest请求地址---------->>" + url);
//        RequestParams params = new RequestParams(url);
//        //Post请求参数配置
//        Map<String, String> map = new HashMap<String, String>();
//        map.put("userName", uName);
//        map.put("passWord", uPwd);
//        map.put("userType", String.valueOf(MyApplication.userType));
//        //map.put("userInfo",userInfo);
//        JSONObject jsonObject = new JSONObject(map);
//        String jsonStr = jsonObject.toString();
//        params.addQueryStringParameter("ngMeter", jsonStr);
//        //Get请求参数设置
//        //params.addQueryStringParameter("uPwd",uPwd);
//        //开始请求
//        loading.show();
//        x.http().post(params, new Callback.CommonCallback<String>() {
//            @Override
//            public void onSuccess(String result) {
//                LogUtil.d(TAG, "\nloginRequest返回数据---------->>\n" + result);
//                try {
//                    JSONObject jObject = new JSONObject(result);
//                    isLoginCode = Integer.valueOf(jObject.getString("errorCode").trim());
//                    if (isLoginCode == 1) {
//                        if (MyApplication.userType == Constant.UserTypeAdmin) {
//                            MyApplication.adminId = Integer.valueOf(jObject.getString("adminId").trim());
//                            //请求管理员表信息
//                            getUserMeterInfo(Constant.METERINFO, name);
//                        } else {
//                            //普通用户获取客户信息处理
//                            String consuList = jObject.getString("dataList");
//                            GsonBuilder gsonB = new GsonBuilder();
//                            Gson gson = gsonB.create();
//                            mCustomInfos = gson.fromJson(consuList, new TypeToken<ArrayList<CustomInfo>>() {
//                            }.getType());
//                            if (mCustomInfos.size() > 0) {
//                                MyApplication.mCustomInfo = mCustomInfos.get(0);
//                                startActivity(new Intent(mContext, MainActivity.class));
//                                setToast(getResources().getString(R.string.login_request_success));
//                            } else {
//                                setToast(getResources().getString(R.string.login_request_failed));
//                            }
//                        }
//                        //保存登录信息到本地
//                        Preferences.mPreferences.putString(Constant.USERNAME, name);
//                        Preferences.mPreferences.putString(Constant.USERPASSWORD, password);
//                        Preferences.mPreferences.putBoolean(Constant.ISADMINLOGIN, is_admin.isChecked());
//                        Preferences.mPreferences.putBoolean(Constant.ISREMEMBERPWD, is_remember_pwd.isChecked());
//                        LogUtil.d(TAG, "\nloginRequest保存用户信息---------->>成功");
//                    } else {
//                        setToast(getResources().getString(R.string.login_request_failed));
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onError(Throwable throwable, boolean b) {
//                if (throwable instanceof HttpException) {
//                    HttpException he = (HttpException) throwable;
//                    int stateCode = he.getCode();
//                    String getMsg = he.getMessage();
//                    String getResult = he.getResult();
//                    LogUtil.d(TAG, "\nloginRequest请求异常---------->>\ngetMsg=" + getMsg + "\ngetResult=" + getResult);
//                } else {
//                    //其他异常处理
//                }
//            }
//
//            @Override
//            public void onCancelled(CancelledException e) {
//                LogUtil.d(TAG, "\nloginRequest取消请求---------->>onCancelled");
//            }
//
//            @Override
//            public void onFinished() {
//                LogUtil.d(TAG, "\nloginRequest请求结束---------->>onFinished");
//                loading.dismiss();
//            }
//        });
//    }

    /**
     * Describe：输入配置检测
     * Params:
     * Return:
     * Date：2018-08-09 17:22:05
     */
    private boolean isInputConfig(String uName, String uPwd) {
        boolean isGoOn = true;
        //用户名是否为空
        if (TextUtils.isEmpty(uName)) {
            setToast(getResources().getString(R.string.login_name_null));
            return false;
        }
//        if (MyApplication.userType == 1) {
//            //用户名是否为合法手机号
//            if (!MethodUtil.isMobileNo(uName)) {
//                setToast(getResources().getString(R.string.login_name_illegal));
//                return false;
//            }
//        }
        //密码是否为空
        if (TextUtils.isEmpty(uPwd)) {
            setToast(getResources().getString(R.string.login_pwd_null));
            return false;
        }
        return isGoOn;
    }

    /**
     * Describe：退出系统监听
     * Params:
     * Date：2018-04-23 17:50:11
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == event.KEYCODE_BACK) {
            exitApp(0);
        }
        return true;
    }
}
