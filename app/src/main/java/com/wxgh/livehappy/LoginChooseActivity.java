package com.wxgh.livehappy;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.wxgh.livehappy.model.ReturnJson;
import com.wxgh.livehappy.model.TencentUserToken;
import com.wxgh.livehappy.utils.ConstantManger;
import com.wxgh.livehappy.utils.StaticManger;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginChooseActivity extends AppCompatActivity {

    private LinearLayout linXin, linQq, linPhone;//新浪、qq、手机号
    private Tencent mTencent;
    String tencentAppID = "1105461627";//tencent的appid
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_choose);
        initView();
    }

    /**
     * 初始化
     */
    private void initView() {
        linXin = (LinearLayout) findViewById(R.id.lin_xin);
        linQq = (LinearLayout) findViewById(R.id.lin_qq);
        linPhone = (LinearLayout) findViewById(R.id.lin_phone);
        linXin.setOnClickListener(click);
        linQq.setOnClickListener(click);
        linPhone.setOnClickListener(click);
    }

    /**
     * 单击事件
     */
    View.OnClickListener click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.lin_xin://新浪点击
                    break;
                case R.id.lin_qq://qq
                    // Tencent类是SDK的主要实现类，开发者可通过Tencent类访问腾讯开放的OpenAPI。
                    // 其中APP_ID是分配给第三方应用的appid，类型为String。
                    mTencent = Tencent.createInstance(tencentAppID, getApplicationContext());
                    // 1.4版本:此处需新增参数，传入应用程序的全局context，可通过activity的getApplicationContext方法获取
                    mTencent.logout(LoginChooseActivity.this);
                    mTencent.login(LoginChooseActivity.this, "all", loginListener);
                    break;
                case R.id.lin_phone://手机
                    startActivityForResult(new Intent(getBaseContext(), PhoneLoginActivity.class), 2);
                    break;
            }
        }
    };

    public void showProgressDialog() {
        dialog = new ProgressDialog(LoginChooseActivity.this);
        //设置进度条风格，风格为圆形，旋转的
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        //设置ProgressDialog 提示信息
        dialog.setMessage("登录中...");
        //设置ProgressDialog 的进度条是否不明确
        dialog.setIndeterminate(false);
        //设置ProgressDialog 是否可以按退回按键取消
        dialog.setCancelable(true);
        //显示
        dialog.show();

    }


    /**
     * QQ登陆的回调
     */
    IUiListener loginListener = new IUiListener() {
        @Override
        public void onComplete(Object o) {
            showProgressDialog();
//            Toast.makeText(getApplicationContext(), "授权成功", Toast.LENGTH_SHORT).show();
            TencentUserToken tuk = new Gson().fromJson(o.toString(), TencentUserToken.class);
            if (tuk != null) {
                thirdPartyLogin(tuk);
            }
//            Toast.makeText(LoginChooseActivity.this, "tuk.getAccess_token()----" + tuk.getAccess_token(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onError(UiError uiError) {
        }

        @Override
        public void onCancel() {
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_LOGIN) {
            Tencent.onActivityResultData(requestCode, resultCode, data, loginListener);
        }
        if (requestCode == 2) {//从手机号登录回来

        }
    }

    /**
     * 第三方登录
     *
     * @param tuk QQ登录信息
     */
    public void thirdPartyLogin(TencentUserToken tuk) {

        String url = ConstantManger.SERVER_IP + ConstantManger.THIRD_PARTY_LOGIN;//"?Access_token=1& Openid=1& ThirdPartyType=1&appid=1";
        RequestBody requestBody = null;
        if (tuk != null) {
            requestBody = new FormBody.Builder().add("appid", tencentAppID).add("Access_token", tuk.getAccess_token()).add("Openid", tuk.getOpenid()).add("ThirdPartyType", "0").build();
        }
        Request request = new Request.Builder().url(url).post(requestBody).build();
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();
                ReturnJson returnJson = new Gson().fromJson(json, ReturnJson.class);
                if (returnJson.getError() == 200 && returnJson.getUsers() != null) {//获取用户信息成功
                    StaticManger.user = returnJson.getUsers().get(0);
                    loginOk();
                }
            }
        });
    }

    /**
     * 登录成功，关闭此activity
     */
    public void loginOk() {
        setResult(2);
        //销毁弹框
        if (dialog != null) {
            dialog.dismiss();
        }
        finish();
    }


}
