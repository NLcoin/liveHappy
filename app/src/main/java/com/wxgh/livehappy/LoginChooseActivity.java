package com.wxgh.livehappy;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

public class LoginChooseActivity extends AppCompatActivity implements View.OnClickListener {
    private LinearLayout lin_phone;
    private LinearLayout lin_qq;
    private LinearLayout lin_xin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_choose);
        lin_phone = (LinearLayout) findViewById(R.id.lin_phone);
        lin_qq = (LinearLayout) findViewById(R.id.lin_qq);
        lin_xin = (LinearLayout) findViewById(R.id.lin_xin);
        lin_phone.setOnClickListener(this);
        lin_qq.setOnClickListener(this);
        lin_xin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lin_phone:
                startActivity(new Intent(LoginChooseActivity.this,PhoneLoginActivity.class));
                finish();
                break;
            case R.id.lin_qq:
                break;
            case R.id.lin_xin:
                break;
        }
    }
}
