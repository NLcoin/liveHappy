package com.wxgh.livehappy.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.widget.Toast;

import com.wxgh.livehappy.view.ViewDialog;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2016/5/24 0024.
 * 工具类
 * 徐博
 */
public class Verification {
    /**
     * 手机号验证
     *
     * @param str
     * @return 验证通过返回true
     */
    public static boolean isMobile(String str) {
        Pattern p = null;
        Matcher m = null;
        boolean b = false;
        p = Pattern.compile("^[1][3,4,5,8][0-9]{9}$"); // 验证手机号
        m = p.matcher(str);
        b = m.matches();
        return b;
    }

    /**
     * 验证密码
     *
     * @param pwd
     * @return
     */
    public static boolean isPassword(String pwd) {
        String r = "^[0-9a-zA-Z]{6,16}$";
        return pwd.matches(r);
    }

    /**
     * 获取当前时间（年月日）
     *
     * @return
     */
    public static String getTime() {
        int y, m, d;
        Calendar cal = Calendar.getInstance();
        y = cal.get(Calendar.YEAR);
        m = cal.get(Calendar.MONTH);
        d = cal.get(Calendar.DATE);
        return y + "年" + m + "月" + d + "日";
    }


    /**
     * 创建对话框
     */
    public static void createDialog1(final Context context, String title, String message) {
        // 创建Builder
        ViewDialog.Builder builder = new ViewDialog.Builder(context);
        // 设置标题
        builder.setTitle(title);
        // 设置信息
        builder.setMessage(message);
        // 设置Positive按钮点击监听
        builder.setPositiveButton("取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builder.setNegativeButton("修改密码", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(context, "修改密码！", Toast.LENGTH_LONG).show();
            }
        });
        // 设置对话框关闭监听
        builder.setCancelable(true);
        // 创建对话框并显示
        builder.create().show();
    }


}
