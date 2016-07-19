package com.wxgh.livehappy.app;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.Application;
import android.content.Context;

import com.lecloud.config.LeCloudPlayerConfig;
import com.letv.proxy.LeCloudProxy;
import com.wxgh.livehappy.le_play.handler.CrashHandler;

import java.util.List;

public class MyApplication extends Application {

    /**
     * 全局的上下文
     */
    private static Context mContext;
    public static String getProcessName(Context cxt, int pid) {
        ActivityManager am = (ActivityManager) cxt.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningAppProcessInfo> runningApps = am.getRunningAppProcesses();
        if (runningApps != null) {
            for (RunningAppProcessInfo procInfo : runningApps) {
                if (procInfo.pid == pid) {
                    return procInfo.processName;
                }
            }
        }
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        String processName = getProcessName(this, android.os.Process.myPid());
        if (getApplicationInfo().packageName.equals(processName)) {
            //TODO CrashHandler是一个抓取崩溃log的工具类（可选）
            CrashHandler.getInstance(this);
            LeCloudPlayerConfig.getInstance().setPrintSdcardLog(true).setIsApp().setUseLiveToVod(true);//setUseLiveToVod 使用直播转点播功能 (直播结束后按照点播方式播放)
            LeCloudProxy.init(getApplicationContext());
        }
    }
    /**
     * 获取context
     * @return
     */
    public static Context getContext(){
        return mContext;
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

}
