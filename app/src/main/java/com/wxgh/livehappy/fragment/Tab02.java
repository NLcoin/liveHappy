package com.wxgh.livehappy.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.letv.recorder.util.MD5Utls;
import com.wxgh.livehappy.R;
import com.wxgh.livehappy.le_push.RecorderActivity;
import com.wxgh.livehappy.le_push.data.CreateStreamData;
import com.wxgh.livehappy.utils.KeyBoardUtils;

import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Created by Administrator on 2016/5/16.
 */
public class Tab02 extends Fragment {
    private View view;
    private TextView tv_play;//开始直播按钮
    private EditText et_title;//直播标题
    private String playUrl = "";//播放地址

    // 移动直播推流域名
    private static final String DEFAULT_DOMAINNAME = "6172.mpush.live.lecloud.com";
    // 移动直播推流签名密钥
    private static final String DEFAULT_APPKEY = "MS2EZZJG648V1LWEN558";
    // 移动直播推流名称
    private static final String STREAM_ID = "testlive";
    private CreateStreamData createStreamData;//直播实体类
    private SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.tab02, container, false);
        initView();
        setOnlistener();
        //移动直播生成推流地址时用到的Model
        createStreamData = new CreateStreamData(getContext());
        return view;
    }

    private void setOnlistener() {
        tv_play.setOnClickListener(click);
    }

    public void initView() {
        tv_play = (Button) view.findViewById(R.id.tv_play);
        et_title = (EditText) view.findViewById(R.id.et_title);
        et_title.setOnClickListener(click);
    }


    View.OnClickListener click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_play:
                    addPlay();
                    pushStream();
                    break;
                case R.id.et_title:
                    Toast.makeText(getContext(), "132456", Toast.LENGTH_LONG).show();
                    KeyBoardUtils.openKeybord(et_title, getContext());
                    break;
            }
        }
    };

    /**
     * 获取直播标题
     *
     * @return
     */
    public String getTitle() {
        String title = et_title.getText().toString().trim();
        if (!title.equals(""))
            return title;
        return null;
    }


    public void addPlay() {
        String title = getTitle();
        playUrl = createStreamUrl(false);//获取播放地址
    }


    /**
     * 点击开始推流按钮后调用该方法
     */
    private void pushStream() {
        Intent intent = null;
        // 无推流地址
        // 无推流地址界面中，获取 推流域名 签名密钥 流名称 三个参数
        intent = new Intent(getActivity(), RecorderActivity.class);

        createStreamData.setDomainName(DEFAULT_DOMAINNAME);

        createStreamData.setAppKey(DEFAULT_APPKEY);
        createStreamData.setStreamId(STREAM_ID);
        intent.putExtra("pushName", createStreamData.getStreamId());

        // 生成一个推流地址，并且把推流地址 传递到 RecorderActivity 中去
        intent.putExtra("streamUrl", createStreamUrl(true));
        // 获取当前 横屏还是竖屏推流。并且把参数传递给推流界面
        intent.putExtra("isVertical", createStreamData.isVertical());
        // 启动推流界面
        startActivity(intent);
    }

    /**
     * **移动直播 **    生成一个 推流地址/播放地址 。这两个地址生成规则特别像
     *
     * @param isPush 当前需要生成的是推流地址还是播放地址，true 推流地址 ，false 播放地址
     * @return 返回生成的地址
     */
    private String createStreamUrl(boolean isPush) {
        // 格式化，获取时间参数 。注意，当你在创建移动直播应用时，如果开启推流防盗链或播放防盗链 。那么你必须保证这个时间和中国网络时间一致
        String tm = format.format(new Date());
        // 获取无推流地址时 流名称，推流域名，签名密钥 三个参数
        String domainName = DEFAULT_DOMAINNAME;
        // 生成 sign值,在播放和推流时生成的sign值不一样
        String sign;
        if (isPush) {
            // 生成推流的 sign 值 。把流名称 ，时间，签名密钥 通过MD5 算法加密
            sign = MD5Utls.stringToMD5(STREAM_ID + tm + DEFAULT_APPKEY);
        } else {
            // 生成播放 的sign 值，把流名称，时间，签名密钥，和"lecloud" 通过MD5 算法加密
            sign = MD5Utls.stringToMD5(STREAM_ID + tm + DEFAULT_APPKEY + "lecloud");
            // 获取到播放域名。现在播放域名的获取规则是 把推流域名中的 push 替换为pull
            domainName = domainName.replaceAll("push", "pull");
        }
        // 拼接出一个rtmp 的地址
        return "rtmp://" + domainName + "/live/" + STREAM_ID + "?tm=" + tm + "&sign=" + sign;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

}
