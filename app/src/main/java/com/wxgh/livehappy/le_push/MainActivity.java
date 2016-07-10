package com.wxgh.livehappy.le_push;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.letv.recorder.util.MD5Utls;
import com.wxgh.livehappy.R;
import com.wxgh.livehappy.le_push.data.CreateStreamData;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    // 移动直播推流域名
    private static final String DEFAULT_DOMAINNAME = "6172.mpush.live.lecloud.com";
    // 移动直播推流签名密钥
    private static final String DEFAULT_APPKEY = "MS2EZZJG648V1LWEN558";
    // 移动直播推流名称
    private static final String STREAM_ID = "testlive";

    private CreateStreamData createStreamData;//直播实体类
    private Button btn_play;//开始直播按钮
    private Button btn_create_path;//生成直播地址按钮
    private Button btnIsScreen;//是否横屏按钮

    private SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.le_push_activity_main);
        //移动直播生成推流地址时用到的Model
        createStreamData = new CreateStreamData(this);
        initView();
    }

    private void initView() {
        btn_create_path = (Button) findViewById(R.id.btn_create_path);
        btn_create_path.setOnClickListener(onClick);

        btn_play = (Button) findViewById(R.id.btn_play);
//        开始直播按钮被按下
        btn_play.setOnClickListener(onClick);

        btnIsScreen = (Button) findViewById(R.id.btn_is_screen);
        btnIsScreen.setOnClickListener(onClick);


    }

    /**
     * 点击开始推流按钮后调用该方法
     */
    private void pushStream() {
        Intent intent = null;
        // 无推流地址
        // 无推流地址界面中，获取 推流域名 签名密钥 流名称 三个参数
        intent = new Intent(this, RecorderActivity.class);

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

    /**
     * 生成播放地址，点击生成播放地址按钮后调用这个方法
     */
    private void createPlayerUrl() {
        // 按照拼接规则，拼接出一个播放地址
        String pullStream = createStreamUrl(false);
        // 保存成功的播放地址
        if (pullStream != null && !pullStream.equals("")) {
            createStreamData.setPlayUrl(pullStream);
        } else {
            createStreamData.setPlayUrl("无法生成正确的播放地址");
        }
        EditText editText = (EditText) findViewById(R.id.editText);
        editText.setText(createStreamData.getPlayUrl());
//        Toast.makeText(this, createStreamData.getPlayUrl(), Toast.LENGTH_SHORT).show();
    }

    //单击事件
    private View.OnClickListener onClick = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_play://开始播放
                    pushStream();
                    break;
                case R.id.btn_create_path://获取直播地址
                    createPlayerUrl();
                    break;
                case R.id.btn_is_screen://是否横屏
                    if (createStreamData.isVertical()) {
                        createStreamData.setVertical(false);
                    } else {
                        createStreamData.setVertical(true);
                    }
                    break;
            }
        }
    };
}
