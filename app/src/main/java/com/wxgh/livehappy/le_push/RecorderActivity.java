package com.wxgh.livehappy.le_push;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.letv.recorder.controller.Publisher;
import com.letv.recorder.util.LeLog;
import com.wxgh.livehappy.R;
import com.wxgh.livehappy.le_push.ui.RecorderView;
import com.wxgh.livehappy.le_push.ui.mobile.RecorderSkinMobile;
import com.wxgh.livehappy.model.Users;
import com.wxgh.livehappy.utils.ConstantManger;
import com.wxgh.livehappy.utils.StaticManger;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 移动直播推流界面
 * 在移动直播中，推流器只认识推流地址。
 * 这个Activity 需要用户传入三个值 isVertical 是否竖屏录制、streamUrl 推流地址、pushName 推流名称
 */
public class RecorderActivity extends Activity {
    private RecorderView rv;
    private RecorderSkinMobile recorderSkinMobile;
    private ImageView focusView;
    private boolean isVertical = false;
    private Publisher publisher;

    private String pushSteamUrl;
    private String pushName;

    private static String pushTitle, playUrl;//直播标题,播放地址//传过来滴

    public static int liveId;//正在直播的直播间编号

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);////设置窗体始终点亮
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//设置窗体全屏
        setContentView(R.layout.activity_recorder);

        focusView = (ImageView) findViewById(R.id.focusView);
        rv = (RecorderView) findViewById(R.id.rv);//获取rootView

        pushTitle = getIntent().getStringExtra("pushTitle");
        playUrl = getIntent().getStringExtra("playUrl");
        isVertical = getIntent().getBooleanExtra("isVertical", false);
        pushSteamUrl = getIntent().getStringExtra("streamUrl");
        pushName = getIntent().getStringExtra("pushName");
        if (pushSteamUrl == null || "".equals(publisher)) {
            Toast.makeText(this, "不能传入空的推流地址", Toast.LENGTH_SHORT).show();
        }
        LeLog.w("推流地址是:" + pushSteamUrl);

        /**
         *  1、 初始化推流器，在移动直播中使用的是Publisher 对象
         */
        initPublish();
        /**
         *  2、初始化皮肤,在移动直播中使用的是RecorderSkinMobile 对象，并且需要传入不同的参数
         */
        initSkin(isVertical);
        /**
         * 3、绑定推流器
         */
        bindingPublish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        /**
         * 设置为横屏
         */
        if (!isVertical && getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else if (isVertical && getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        /**
         * onResume的时候需要做一些事情
         */
        if (recorderSkinMobile != null) {
            recorderSkinMobile.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        /**
         * onPause的时候要作的一些事情
         */
        if (recorderSkinMobile != null) {
            recorderSkinMobile.onPause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (recorderSkinMobile != null) {
            recorderSkinMobile.onDestroy();
        }
        destoryObject();
    }

    /**
     * 销毁一些对象
     */
    public void destoryObject() {
        destroyLive();
//        liveId = 0;

    }
    ////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * 皮肤于推流器关联
     */
    private void bindingPublish() {
        recorderSkinMobile.BindingPublisher(publisher);
        publisher.setCameraView(recorderSkinMobile.getCameraView());
        publisher.setFocusView(focusView);
    }

    /**
     * 初始化皮肤
     */
    private void initSkin(boolean isScreenOrientation) {
        recorderSkinMobile = new RecorderSkinMobile();
        recorderSkinMobile.setPushSteamUrl(pushSteamUrl);
        recorderSkinMobile.setStreamName(pushName);
        if (isScreenOrientation) {
            recorderSkinMobile.build(this, rv, ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else {
            recorderSkinMobile.build(this, rv, ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
    }

    /**
     * 初始化推流器
     */
    private void initPublish() {
        publisher = Publisher.getInstance();
        if (isVertical) {
            /// 竖屏状态
            publisher.getRecorderContext().setUseLanscape(false);
        } else {
            /// 横屏状态
            publisher.getRecorderContext().setUseLanscape(true);
        }
        publisher.initPublisher(this);
    }


    /**
     * 添加直播
     */
    public static void addLive(Context context) {
        Users users = StaticManger.getCurrentUser(context);
        String url = ConstantManger.SERVER_IP + ConstantManger.INSERT_LIVE + "?BroadcastAddress=" + playUrl + "&UserID=" + users.getUsersinfoid() + "&LiveTitle=" + pushTitle;
        OkHttpClient okHttpClient = new OkHttpClient();
        final Request request = new Request.Builder().url(url).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    if (Integer.parseInt(jsonObject.getString("error"))== 200) {//直播成功
                        liveId = Integer.parseInt(jsonObject.getString("LiveID"));
                    } else if (Integer.parseInt(jsonObject.getString("error")) == 201) {//直播失败

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    /**
     * 202     * 停止直播、暂停直播
     */
    public static void stopLive() {
        updateLiveState(1);
    }

    /**
     * 销毁直播
     */
    public static void destroyLive() {
        updateLiveState(2);
    }

    /**
     * 继续直播
     */
    public static void continueLive() {
        updateLiveState(0);
    }

    /**
     * 更新直播间状态
     *
     * @param state 0是开播   1 是暂停  2 是关闭
     */
    private static void updateLiveState(int state) {
        String url = ConstantManger.SERVER_IP + ConstantManger.UPDATE_LIVE_STATE + "?State=" + state + "&LiveID=" + liveId;
        OkHttpClient okHttpClient = new OkHttpClient();
        final Request request = new Request.Builder().url(url).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    if (jsonObject.getInt("error") == 200) {//状态修改成功

                    } else if (jsonObject.getInt("error") == 201) {//状态修改失败

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
