package com.wxgh.livehappy.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.wxgh.livehappy.FeedbackActivity;
import com.wxgh.livehappy.LoginChooseActivity;
import com.wxgh.livehappy.R;
import com.wxgh.livehappy.UserCurrencyActivity;
import com.wxgh.livehappy.UsersInformationActivity;
import com.wxgh.livehappy.WalletActivity;
import com.wxgh.livehappy.app.MyApplication;
import com.wxgh.livehappy.model.Users;
import com.wxgh.livehappy.utils.ConstantManger;
import com.wxgh.livehappy.utils.StaticManger;
import com.wxgh.livehappy.utils.Verification;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by 98016 on 2016/7/13 0013.
 */
public class UserCenterFragment extends Fragment {
    private View view;
    private SimpleDraweeView draweeView;//头像
    private TextView tv_name, tv_thesignature, tv_zan, tv_focuson, tv_fans, tv_friendsNumber, tv_themessageNumber, tv_login_out;//用户名,签名，赞数量，关注数量，粉丝数,朋友数,消息数量,退出登陆
    private RelativeLayout rl_userinfo, rl_purse, rl_feedback, rl_setting;//用户信息,钱包,反馈,设置
    private LinearLayout ll_zan;//赞布局

    private LinearLayout guanzhu,fensi,haoy;

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
     *根据用户状态初始化控件
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
        guanzhu= (LinearLayout) view.findViewById(R.id.guanzhu);
        fensi= (LinearLayout) view.findViewById(R.id.fensi);
        haoy= (LinearLayout) view.findViewById(R.id.haoy);
        tv_name.setOnClickListener(click);
        rl_userinfo.setOnClickListener(click);
        tv_login_out.setOnClickListener(click);
        rl_purse.setOnClickListener(click);
        rl_feedback.setOnClickListener(click);

        guanzhu.setOnClickListener(click);
        fensi.setOnClickListener(click);
        haoy.setOnClickListener(click);
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


        String url = ConstantManger.SERVER_IP + ConstantManger.SELECTOTHERMY;
        RequestBody requestBody = new FormBody.Builder().add("UserID", user.getUsersinfoid()).build();
        Request request = new Request.Builder().url(url).post(requestBody).build();
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                handler.sendEmptyMessage(0);
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();
                try {
                    JSONObject a = new JSONObject(json);
                    String error = a.getString("error");
                    if ("200".equals(error)) {//成功
                        Message message=new Message();
                        message.what=2;
                        message.obj=json;
                        handler.sendMessage(message);
                    } else {
                        handler.sendEmptyMessage(1);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
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
                    Verification.updateUserOnline(user);
                    break;
                case R.id.guanzhu:
                    Intent intent1=new Intent(getActivity(), UserCurrencyActivity.class);
                    intent1.putExtra("typyFriend",1);
                    startActivityForResult(intent1, 4);
                    break;
                case R.id.fensi:
                    Intent intent=new Intent(getActivity(), UserCurrencyActivity.class);
                    intent.putExtra("typyFriend",2);
                    startActivityForResult(intent, 4);
                    break;
                case R.id.haoy:
                    Intent intent2=new Intent(getActivity(), UserCurrencyActivity.class);
                    intent2.putExtra("typyFriend",3);
                    startActivityForResult(intent2, 4);
                    break;
            }
            user = null;
        }
    };


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    Toast.makeText(MyApplication.getContext(), "网络异常！", Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    break;
                case 2:
                    String json= (String) msg.obj;
                    try {
                        JSONObject a = new JSONObject(json);
                        String guanzhu = a.getString("guanzhu");
                        String fensi = a.getString("fensi");
                        String haoy = a.getString("haoy");
                        tv_focuson.setText(guanzhu);
                        tv_fans.setText(fensi);
                        tv_friendsNumber.setText(haoy);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };
}
