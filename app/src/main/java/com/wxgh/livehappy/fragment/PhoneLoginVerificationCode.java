package com.wxgh.livehappy.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.wxgh.livehappy.R;
import com.wxgh.livehappy.model.ReturnJson;
import com.wxgh.livehappy.model.Users;
import com.wxgh.livehappy.utils.ConstantManger;
import com.wxgh.livehappy.utils.StaticManger;
import com.wxgh.livehappy.utils.Verification;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
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
public class PhoneLoginVerificationCode extends Fragment {
    private View view;
    private EditText et_phone, et_verificationcode;//手机号，验证码
    private ImageView iv_get_sms_code;//获取短信验证码按钮
    private TextView tv_login;//登陆按钮
    private String phoneNum, SMSCode, password;//手机号，短信验证码,密码


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.phoneloginverificationcode, container, false);
        initView();
        initSMS();

        return view;
    }

    /**
     * 第一次登录获取密码
     */
    public void getPassword() {
        StaticManger.destroyDialog();
        final EditText editText = new EditText(getActivity());
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("设置密码");
        //    设置我们自己定义的布局文件作为弹出框的Content
        builder.setView(editText);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                password = editText.getText().toString();
                if (Verification.isPassword(password)) {
                    insertUserByPhoneAndPassword(phoneNum);
                    StaticManger.showProgressDialog(getContext());
                    dialog.dismiss();
                } else {
                    Toast.makeText(getContext(), "请输入正确的密码", Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.show();

    }

    /**
     * 短信验证的回掉
     */
    EventHandler eh = new EventHandler() {
        @Override
        public void afterEvent(int event, int result, Object data) {

            if (result == SMSSDK.RESULT_COMPLETE) {
                //回调完成
                if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                    //提交验证码成功
//                       验证失败走异常，成功返回---- {phone=15321763573, country=86}
                    try {
                        JSONObject jsonObject = new JSONObject(data.toString());
                        selectUserByPhone(jsonObject.getString("phone"));
                        StaticManger.showProgressDialog(getContext());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    handler.sendEmptyMessage(1);//将smssdk注册代码注销
                } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                    //获取验证码成功
                    if ((Boolean) data) {
                        //验证成功，此号码在本设备上验证过了，不会再发短信
                        handler.sendEmptyMessage(1);//将smssdk注册代码注销
                        handler1.sendEmptyMessage(2);
                        selectUserByPhone(phoneNum);
                    }
//                    Log.d("sms", "EVENT_GET_VERIFICATION_CODE--------------" + data + "");
                } else if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {
                    //返回支持发送验证码的国家列表
//                        ArrayList<HashMap<String,Object>> phoneMap = (ArrayList<HashMap<String,Object>>) data;
                }
            } else {
                ((Throwable) data).printStackTrace();
            }
        }
    };

    /**
     * 根据手机号查询用户是否已存在，存在登录成功，不存在注册
     *
     * @param phone
     */
    public void selectUserByPhone(final String phone) {
        String url = ConstantManger.SERVER_IP + ConstantManger.SELECT_BY_PHONE;
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder().add("UserPhone", phone).build();
        Request request = new Request.Builder().url(url).post(requestBody).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();
                ReturnJson returnJson = new Gson().fromJson(json, ReturnJson.class);
                if (returnJson != null) {
                    if (returnJson.getError() == 200) {//登录成功
                        Users user = returnJson.getUsers().get(0);
                        if (user != null) {
                            //登陆成功操作
                            StaticManger.saveUser(getContext(), user);
                            loginOk();
                        }
                    } else if (returnJson.getError() == 201) {//用户不存在
                        handler1.sendEmptyMessage(3);//第一次登陆、设置密码
//                            getPassword();
                    } else if (returnJson.getError() == 202) {//用户已在线
                    }
                }

            }
        });
    }

    Handler handler1 = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == 3) {
                getPassword();//用户第一次登陆设置密码
            }
            if (msg.what == 2) {//显示showdialog
                StaticManger.showProgressDialog(getContext());
            }

            return false;
        }
    });

    /**
     * 根据手机号和密码注册
     */
    public void insertUserByPhoneAndPassword(String phone) {
        String url = ConstantManger.SERVER_IP + ConstantManger.INSERT_USERS_INFO;
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder().add("Phone", phone).add("Password", password).build();
        Request request = new Request.Builder().url(url).post(requestBody).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();
                ReturnJson returnJson = new Gson().fromJson(json, ReturnJson.class);
                if (returnJson != null) {
                    if (returnJson.getError() == 200 && returnJson.getUsers() != null) {//获取用户信息成功
                        StaticManger.saveUser(getContext(), returnJson.getUsers().get(0));
                        loginOk();
                    } else if (returnJson.getError() == 201) {//注册失败
                    }
                }
            }
        });
    }

    /**
     * 登陆成功操作
     */
    public void loginOk() {
        getActivity().setResult(2);
        StaticManger.destroyDialog();
        getActivity().finish();
    }


    /**
     * 初始化短信
     */
    private void initSMS() {
        SMSSDK.initSDK(getActivity(), "14d8a28bd8720", "5d108ef6045a50adeffb70afd9ec2273");
        SMSSDK.registerEventHandler(eh); //注册短信回调()
//        SMSSDK.getSupportedCountries();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        et_phone = (EditText) view.findViewById(R.id.et_phone);
        et_verificationcode = (EditText) view.findViewById(R.id.et_verificationcode);
        iv_get_sms_code = (ImageView) view.findViewById(R.id.iv_get_sms_code);
        tv_login = (TextView) view.findViewById(R.id.tv_login);

        iv_get_sms_code.setOnClickListener(click);
        tv_login.setOnClickListener(click);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    View.OnClickListener click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.iv_get_sms_code://获取短信验证码
                    if (getPhoneNum()) {
                        SMSSDK.getVerificationCode("86", phoneNum);
                    }
                    break;
                case R.id.tv_login://登陆
                    if (getPhoneNum() && getSMSCode()) {
                        SMSSDK.submitVerificationCode("86", phoneNum, SMSCode);
                    }
                    break;
            }
        }
    };

    /**
     * 获取手机号，并检查是否正确
     *
     * @return
     */
    public boolean getPhoneNum() {
        phoneNum = et_phone.getText().toString().trim();
        if (!Verification.isMobile(phoneNum)) {
            Toast.makeText(getContext(), "请输入正确的手机号", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    /**
     * 获取验证码，并检查是否正确
     *
     * @return
     */
    public boolean getSMSCode() {
        SMSCode = et_verificationcode.getText().toString().trim();
        if (SMSCode.equals("")) {
            Toast.makeText(getContext(), "请输入短信验证码", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    // 验证通过之后，将smssdk注册代码注销
                    SMSSDK.unregisterEventHandler(eh);
                    break;
            }
            return false;
        }
    });

}
