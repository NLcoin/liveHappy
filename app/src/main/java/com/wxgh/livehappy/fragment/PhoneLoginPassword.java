package com.wxgh.livehappy.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.wxgh.livehappy.R;
import com.wxgh.livehappy.utiles.Verification;


/**
 * Created by Administrator on 2016/5/16.
 */
public class PhoneLoginPassword extends Fragment implements View.OnClickListener{
    private View view;
    //登录
    private TextView btn_login;
    //手机号输入框
    private EditText et_phone;
    //密码输入框
    private EditText et_password;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.phoneloginpassword, container,false);
        init();
        return view;
    }
    //初始化
    public void init(){
        btn_login= (TextView) view.findViewById(R.id.btn_login);
        btn_login.setOnClickListener(this);
        et_phone= (EditText) view.findViewById(R.id.et_phone);
        et_password= (EditText) view.findViewById(R.id.et_password);
    }
    @Override
    public void onClick(View v) {
        //单击登录按钮时
        switch (v.getId()){
            case R.id.btn_login:
                String phone = et_phone.getText().toString();
                if (!Verification.isMobile(phone)) {
                    Verification.createDialog1(getActivity(),"提示", "请填写正确手机号！");
                    return;
                }
                String pas = et_password.getText().toString();
                if (!Verification.isPassword(pas)) {
                    Verification.createDialog1(getActivity(),"提示", "请填写正确密码！");
                    return;
                }
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }
}
