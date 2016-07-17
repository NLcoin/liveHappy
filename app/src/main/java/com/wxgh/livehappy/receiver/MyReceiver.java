package com.wxgh.livehappy.receiver;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.WindowManager;

import com.wxgh.livehappy.LoginChooseActivity;
import com.wxgh.livehappy.utils.ActivityCollector;

/**
 * Created by Administrator on 2016/7/16 0016.
 */
public class MyReceiver  extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, Intent intent) {
        //对话框
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("警告");
        //正文
        builder.setMessage("你已被强制下线，请重新登陆。");
        //不可取消
        builder.setCancelable(false);
        //按钮
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //销毁所有活动
                ActivityCollector.finishAll();
                //启动登陆活动
                Intent intent = new Intent(context,LoginChooseActivity.class);
                //在广播中启动活动，需要添加如下代码
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

        AlertDialog alterDialog = builder.create();
        //添加对话框类型：保证在广播中正常弹出
        alterDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        //对话框展示
        alterDialog.show();
    }
}
