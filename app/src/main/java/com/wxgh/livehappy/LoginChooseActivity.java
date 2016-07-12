package com.wxgh.livehappy;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.wxgh.livehappy.model.TencentUserToken;

public class LoginChooseActivity extends AppCompatActivity {

    private LinearLayout linXin, linQq, linPhone;//新浪、qq、手机号
    private Tencent mTencent;

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
                    String TencentAppID = "1105461627";
                    // Tencent类是SDK的主要实现类，开发者可通过Tencent类访问腾讯开放的OpenAPI。
                    // 其中APP_ID是分配给第三方应用的appid，类型为String。
                    mTencent = Tencent.createInstance(TencentAppID, getApplicationContext());
                    // 1.4版本:此处需新增参数，传入应用程序的全局context，可通过activity的getApplicationContext方法获取

//                    if (!mTencent.isSessionValid()) {
                    mTencent.logout(LoginChooseActivity.this);
                    Log.d("msg", "退出");
                    mTencent.login(LoginChooseActivity.this, "all", loginListener);
                    Log.d("msg", "FirstLaunch_SDK:" + SystemClock.elapsedRealtime());

                    Log.d("msg", "qqToken:" + mTencent.getQQToken());
//                        isServerSideLogin = false;

//                    } else {
//                        if (isServerSideLogin) { // Server-Side 模式的登陆, 先退出，再进行SSO登陆
//                            mTencent.logout(this);
//                            mTencent.login(this, "all", loginListener);
//                            isServerSideLogin = false;
//                            Log.d("SDKQQAgentPref", "FirstLaunch_SDK:" + SystemClock.elapsedRealtime());
//                            return;
//                        }
//                        mTencent.logout(this);
//                        updateUserInfo();
//                        updateLoginButton();
//                    }
//

                    break;
                case R.id.lin_phone://手机
                    startActivity(new Intent(getBaseContext(), PhoneLoginActivity.class));
                    finish();
                    break;
            }
        }
    };


    /**
     * QQ登陆的回调
     */
    IUiListener loginListener = new IUiListener() {
        @Override
        public void onComplete(Object o) {
            Toast.makeText(getApplicationContext(), "登录成功", Toast.LENGTH_SHORT).show();
            Log.d("msg", "------------" + o);

//                JSONObject jsonObject = new JSONObject(o.toString());
            TencentUserToken tuk = new Gson().fromJson(o.toString(), TencentUserToken.class);
            Toast.makeText(LoginChooseActivity.this, "tuk.getAccess_token()----" + tuk.getAccess_token(), Toast.LENGTH_SHORT).show();

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
    }

    /**
     * {"ret":0,"openid":"AE3119CF73451C2EAB4423FCDA014F47","access_token":"99BA2A2B5A67CC12C036BA8048E5DEC3","pay_token":"4CCAD5A1A224EEFB893CE63ACA850274","expires_in":7776000,"pf":"desktop_m_qq-10000144-android-2002-","pfkey":"80207c431a77a801c29a77a0fb69994d","msg":"","login_cost":431,"query_authority_cost":333,"authority_cost":-213690143}
     * 根据oauth_consumer_key----app的appid。
     * access_token
     * openid
     * format-----可选，默认json
     * 获取qq个人信息
     */
    public void getUserInfoByTencent() {
        String url = "oauth_consumer_key=100330589&access_token=41C2D82BEFC0C23F206ADB7793645966&openid=84A65F986326A461041A30BD8258275C&format=json";
        url = "https://graph.qq.com/user/get_user_info";

    }


}
