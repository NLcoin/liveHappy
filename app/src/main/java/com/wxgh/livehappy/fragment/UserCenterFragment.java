package com.wxgh.livehappy.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.wxgh.livehappy.FeedbackActivity;
import com.wxgh.livehappy.LoginChooseActivity;
import com.wxgh.livehappy.R;
import com.wxgh.livehappy.UsersInformationActivity;
import com.wxgh.livehappy.WalletActivity;
import com.wxgh.livehappy.model.Users;
import com.wxgh.livehappy.utils.StaticManger;

/**
 * Created by 98016 on 2016/7/13 0013.
 */
public class UserCenterFragment extends Fragment {
    private View view;
    private SimpleDraweeView draweeView;//头像
    private TextView tv_name, tv_thesignature, tv_zan, tv_focuson, tv_fans, tv_friendsNumber, tv_themessageNumber, tv_login_out;//用户名,签名，赞数量，关注数量，粉丝数,朋友数,消息数量,退出登陆
    private RelativeLayout rl_userinfo, rl_purse, rl_feedback, rl_setting;//用户信息,钱包,反馈,设置
    private LinearLayout ll_zan;//赞布局

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.user_center_layout, container, false);

        initView();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    /**
     * 根据用户状态初始化控件
     */
    public void initView() {
        draweeView = (SimpleDraweeView) view.findViewById(R.id.img_header);//头像
        tv_name = (TextView) view.findViewById(R.id.tv_name);//用户名
        tv_thesignature = (TextView) view.findViewById(R.id.tv_thesignature);//签名
        tv_zan = (TextView) view.findViewById(R.id.tv_zan);//赞数量
        tv_focuson = (TextView) view.findViewById(R.id.tv_focuson);//关注数量
        tv_fans = (TextView) view.findViewById(R.id.tv_fans);//粉丝数
        tv_friendsNumber = (TextView) view.findViewById(R.id.tv_friendsNumber);//朋友数
        tv_themessageNumber = (TextView) view.findViewById(R.id.tv_themessageNumber);//消息数量
        rl_userinfo = (RelativeLayout) view.findViewById(R.id.rl_userinfo);//用户信息
        rl_purse = (RelativeLayout) view.findViewById(R.id.rl_purse);//钱包
        rl_feedback = (RelativeLayout) view.findViewById(R.id.rl_feedback);//反馈
        rl_setting = (RelativeLayout) view.findViewById(R.id.rl_setting);//设置
        ll_zan = (LinearLayout) view.findViewById(R.id.ll_zan);//赞布局
        tv_login_out = (TextView) view.findViewById(R.id.tv_login_out);//退出登录

        tv_name.setOnClickListener(click);
        rl_userinfo.setOnClickListener(click);
        tv_login_out.setOnClickListener(click);
        rl_purse.setOnClickListener(click);
        rl_feedback.setOnClickListener(click);
        Users user = StaticManger.getCurrentUser(getContext());
        if (user != null) {//已登录
            isLogin();
        } else {//未登录
            noLogin();
        }
        user = null;
    }


    /**
     * 用户未登录
     */
    public void isLogin() {
        Users user = StaticManger.getCurrentUser(getContext());
        if (user == null) {
            return;
        }
        Uri uri = Uri.parse(StaticManger.getImgPath(user.getUsersinfoPhoto()));

        draweeView.setImageURI(uri);

        tv_name.setText(user.getUsersinfoName());
        tv_thesignature.setText(user.getUserSignature());
        tv_zan.setText(user.getZan() + "");
        draweeView.setVisibility(View.VISIBLE);
        tv_thesignature.setVisibility(View.VISIBLE);//签名
        ll_zan.setVisibility(View.VISIBLE);//赞布局
        tv_login_out.setVisibility(View.VISIBLE);
        user = null;
    }

    /**
     * 用户已登录
     */
    public void noLogin() {
        tv_name.setText("点击登录");
        tv_focuson.setText("0");//关注数量
        tv_fans.setText("0");//粉丝数
        tv_friendsNumber.setText("0");//朋友数
        tv_themessageNumber.setText("0");//消息数量
        draweeView.setVisibility(View.INVISIBLE);
        tv_thesignature.setVisibility(View.GONE);//签名
        ll_zan.setVisibility(View.GONE);//赞布局
        tv_login_out.setVisibility(View.GONE);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {//跳转登录
            if (resultCode == 2)//登录成功
                isLogin();
            else
                noLogin();
        }
        if (requestCode == 2){
            isLogin();
        }
    }

    View.OnClickListener click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Users user = StaticManger.getCurrentUser(getContext());
            switch (v.getId()) {
                case R.id.tv_name://单击用户名
                    if (user == null) {//当前用户未登录，跳转登录
                        startActivityForResult(new Intent(getActivity(), LoginChooseActivity.class), 1);
                    }
                    break;
                case R.id.rl_userinfo:
                    if (user != null) {//当前用户未登录，跳转登录
                        startActivityForResult(new Intent(getActivity(), UsersInformationActivity.class), 2);
                    } else {
                        startActivityForResult(new Intent(getActivity(), LoginChooseActivity.class), 1);
                    }
                    break;
                case R.id.rl_purse:
                    if (user != null) {//当前用户未登录，跳转登录
                        startActivityForResult(new Intent(getActivity(), WalletActivity.class), 0);
                    } else {
                        startActivityForResult(new Intent(getActivity(), LoginChooseActivity.class), 1);
                    }
                    break;
                case R.id.rl_feedback:
                    if (user != null) {//当前用户未登录，跳转登录
                        startActivityForResult(new Intent(getActivity(), FeedbackActivity.class), 0);
                    } else {
                        startActivityForResult(new Intent(getActivity(), LoginChooseActivity.class), 1);
                    }
                    break;
                case R.id.tv_login_out://退出登陆
                    StaticManger.removeUser(getContext());
                    noLogin();
                    break;

            }
            user = null;
        }
    };
}
