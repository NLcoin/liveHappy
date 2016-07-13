package com.wxgh.livehappy;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wxgh.livehappy.adapter.MainAdapter;
import com.wxgh.livehappy.fragment.PhoneLoginPassword;
import com.wxgh.livehappy.fragment.PhoneLoginVerificationCode;

import java.util.ArrayList;
import java.util.List;

public class PhoneLoginActivity extends AppCompatActivity implements View.OnClickListener {
    //切换
    private ViewPager mViewPager;
    private MainAdapter mAdapter;
    private List<Fragment> mFragments=new ArrayList<Fragment>();
    //切换的页面
    private PhoneLoginPassword phoneLoginPassword;
    private PhoneLoginVerificationCode phoneLoginVerificationCode;
    //手机登录 验证码登录
    private TextView txt_password;
    private TextView txt_Verificationcode;
    private LinearLayout lin_left;
    private LinearLayout lin_reight;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_login);
        init();
        mAdapter=new MainAdapter(getSupportFragmentManager());
        mAdapter.setFragmentList(mFragments);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setCurrentItem(0);
    }
    public void init(){
        mViewPager=(ViewPager)findViewById(R.id.phone_login_vp);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}
            @Override
            public void onPageSelected(int position) {
                reset();
                switch (position){
                    case 0:
                        txt_password.setTextColor(Color.parseColor("#E5AA1E"));
                        break;
                    case 1:
                        txt_Verificationcode.setTextColor(Color.parseColor("#E5AA1E"));
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        txt_password=(TextView)findViewById(R.id.txt_password);
        txt_Verificationcode=(TextView)findViewById(R.id.txt_Verificationcode);
        lin_left=(LinearLayout)findViewById(R.id.lin_left);
        lin_left.setOnClickListener(this);
        lin_reight=(LinearLayout)findViewById(R.id.lin_reight);
        lin_reight.setOnClickListener(this);
        initFragment();
    }
    /**
     * 初始化mFragments 数据 页面
     */
    public void initFragment(){
        phoneLoginPassword=new PhoneLoginPassword();
        phoneLoginVerificationCode=new PhoneLoginVerificationCode();
        mFragments.add(phoneLoginPassword);
        mFragments.add(phoneLoginVerificationCode);
    }
    /**
     * 重置1
     */
    public void reset(){
        txt_password.setTextColor(Color.parseColor("#757575"));
        txt_Verificationcode.setTextColor(Color.parseColor("#757575"));
    }
    /**
     * 单击事件
     * @param v
     */
    @Override
    public void onClick(View v) {
        reset();
        switch (v.getId()){
            case R.id.lin_left:
                txt_password.setTextColor(Color.parseColor("#E5AA1E"));
                mViewPager.setCurrentItem(0);
                break;
            case R.id.lin_reight:
                txt_Verificationcode.setTextColor(Color.parseColor("#E5AA1E"));
                mViewPager.setCurrentItem(1);
                break;
        }
    }
}
