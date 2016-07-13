package com.wxgh.livehappy.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.wxgh.livehappy.R;


/**
 * Created by Administrator on 2016/5/16.
 */
public class PhoneLoginVerificationCode extends Fragment implements View.OnClickListener{
    private View view;
    //获取验证码
    private ImageView verification;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.phoneloginverificationcode, container,false);
        init();
        return view;
    }
    //初始化
    public void init(){
        verification= (ImageView) view.findViewById(R.id.verification);
        verification.setOnClickListener(this);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //单击获取验证码
            case R.id.verification:
                Toast.makeText(getContext(),"111",Toast.LENGTH_LONG).show();
                break;
        }
    }
}
