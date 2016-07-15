package com.wxgh.livehappy;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.wxgh.livehappy.model.Users;
import com.wxgh.livehappy.utils.KeyBoardUtils;
import com.wxgh.livehappy.utils.StaticManger;
import com.wxgh.livehappy.utils.Verification;

public class UsersInformationActivity extends Activity implements View.OnClickListener {
    private Users user = null;
    //用户手机号
    private TextView txt_user_phone;
    //用户名
    private EditText edit_username;
    //性别
    private TextView txt_userage;
    //年龄
    private EditText txt_usersex;
    //签名
    private EditText edit_userSignature;
    private SimpleDraweeView draweeView;//头像
    private LinearLayout touxiangupdate;
    private TextView create; //完成

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(getApplicationContext());
        setContentView(R.layout.activity_users_information);
        user = StaticManger.getCurrentUser(this);
        initView();
    }

    /**
     * 初始化控件 和数据
     */
    private void initView() {
        touxiangupdate = (LinearLayout) findViewById(R.id.touxiangupdate);
        draweeView = (SimpleDraweeView) findViewById(R.id.img_userhead);//头像
        txt_user_phone = (TextView) findViewById(R.id.txt_user_phone);
        edit_username = (EditText) findViewById(R.id.edit_username);
        edit_userSignature = (EditText) findViewById(R.id.edit_userSignature);
        txt_userage = (TextView) findViewById(R.id.txt_usergender);
        txt_usersex = (EditText) findViewById(R.id.txt_usersex);
        create= (TextView) findViewById(R.id.create);
        txt_user_phone.setText(Verification.hidePhoneNum(user.getUsersinfoPhone()));
        edit_username.setText(user.getUsersinfoName());
        edit_userSignature.setText(user.getUserSignature());
        txt_userage.setText(user.getUsersinfo_Age());
        txt_usersex.setText(user.getUsersinfoSex());
        Uri uri = Uri.parse(StaticManger.getImgPath(user.getUsersinfoPhoto()));
        draweeView.setImageURI(uri);
        touxiangupdate.setOnClickListener(this);
        edit_username.setOnClickListener(this);
        edit_userSignature.setOnClickListener(this);
        txt_userage.setOnClickListener(this);
        txt_usersex.setOnClickListener(this);
        create.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.touxiangupdate:
                break;
            case R.id.edit_username:
                KeyBoardUtils.openKeybord(edit_username,this);
                break;
            case R.id.edit_userSignature:
                KeyBoardUtils.openKeybord(edit_username,this);
                break;
            case R.id.txt_usergender:

                break;
            case R.id.txt_usersex:
                KeyBoardUtils.openKeybord(edit_username,this);
                break;
            case R.id.create:

                break;
        }
    }
}