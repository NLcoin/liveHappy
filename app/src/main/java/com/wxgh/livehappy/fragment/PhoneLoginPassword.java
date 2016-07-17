package com.wxgh.livehappy.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.wxgh.livehappy.R;
import com.wxgh.livehappy.model.ReturnJson;
import com.wxgh.livehappy.model.Users;
import com.wxgh.livehappy.utils.ConstantManger;
import com.wxgh.livehappy.utils.StaticManger;
import com.wxgh.livehappy.utils.Verification;

import java.io.IOException;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * Created by Administrator on 2016/5/16.
 */
public class PhoneLoginPassword extends Fragment {
    private View view;

    private EditText etPhone, etPassword;//手机号、密码
    private TextView tvLogin, tvForget;//登陆,忘记密码

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.phoneloginpassword, container, false);

        initView();
        return view;
    }

    /**
     * 初始化
     */
    private void initView() {
        etPhone = (EditText) view.findViewById(R.id.et_phone);
        etPassword = (EditText) view.findViewById(R.id.et_password);
        tvLogin = (TextView) view.findViewById(R.id.tv_login);
        tvForget = (TextView) view.findViewById(R.id.tv_forget);

        tvLogin.setOnClickListener(click);
        tvForget.setOnClickListener(click);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    /**
     * 单击事件
     */
    View.OnClickListener click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv_login://登陆
                    String phone = etPhone.getText().toString().trim();
                    String password = etPassword.getText().toString().trim();
                    if (Verification.isMobile(phone) && Verification.isPassword(password)) {
                        StaticManger.showProgressDialog(getContext());
                        login(phone, password);
                    } else {
                        Toast.makeText(getContext(), "请输入正确的账号密码", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.tv_forget://忘记密码
                    break;
            }
        }
    };

    /**
     * 登陆
     *
     * @param phone    手机号
     * @param password 密码
     */
    private void login(final String phone, final String password) {
        String alias=Verification.getIP(getActivity()).replace(".","")+phone;
        JPushInterface.setAliasAndTags(getActivity(),alias, null, new TagAliasCallback() {
            @Override
            public void gotResult(int i, String s, Set<String> set) {}});
        String url = ConstantManger.SERVER_IP + ConstantManger.USER_LOGIN;
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder().add("UserPhone", phone).add("PassWord", password).add("IP", Verification.getIP(getActivity())).build();
        Request request = new Request.Builder().url(url).post(requestBody).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();
                ReturnJson returnJson=new Gson().fromJson(json,ReturnJson.class);
                    if (returnJson != null) {
                        if (returnJson.getError() == 200) {//登陆成功
                            Users user = returnJson.getUsers().get(0);
                            if (user != null) {
                                StaticManger.saveUser(getContext(), user);
                                //登陆成功操作
                                loginOk();
                            }
                        } else if (returnJson.getError() == 201) {//用户存在
//                            insertUserByPhoneAndPassword(phone, password);
                        }
                    }
                    StaticManger.destroyDialog();
            }
        });
    }


    /**
     * 登陆成功
     */
    public void loginOk() {
        getActivity().setResult(2);
        StaticManger.destroyDialog();
        getActivity().finish();
    }
}
