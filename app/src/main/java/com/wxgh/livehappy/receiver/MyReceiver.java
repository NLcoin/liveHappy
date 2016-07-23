package com.wxgh.livehappy.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.wxgh.livehappy.LoginChooseActivity;
import com.wxgh.livehappy.utils.ActivityCollector;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by Administrator on 2016/7/16 0016.
 */
public class MyReceiver  extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        String content = bundle.getString(JPushInterface.EXTRA_ALERT);
        if ("true".equals(content)){
            //销毁所有活动
            ActivityCollector.finishAll();
            //启动登陆活动
            Intent intent1 = new Intent(context,LoginChooseActivity.class);
            //在广播中启动活动，需要添加如下代码
            intent1.addFlags(intent1.FLAG_ACTIVITY_NEW_TASK);
            intent1.putExtra("islogin",2);
            context.startActivity(intent1);
        }
    }
}
