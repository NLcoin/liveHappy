package com.wxgh.livehappy.utils;

import android.app.ProgressDialog;
import android.content.Context;

import com.wxgh.livehappy.model.Users;

/**
 * Created by 98016 on 2016/7/13 0013.
 */
public class StaticManger {
    /**
     * 判断当前是服务器图片还是互联网图片
     *
     * @param imgPath
     * @return
     */
    public static String getImgPath(String imgPath) {
        if (imgPath != null && !imgPath.equals("")) {
            String head = "";
            if (imgPath.length() >= 3) {
                head = imgPath.trim().toLowerCase().substring(0, 4);
            }
            if (!head.equals("http")) {
                return ConstantManger.SERVER_IP + ConstantManger.IMG_FILE + "/" + imgPath;
            } else {
                return imgPath;
            }
        }
        return imgPath;

    }

    /**
     * 获取当前用户
     *
     * @param context
     * @return
     */
    public static Users getCurrentUser(Context context) {
        Users user = new Users();
        user = (Users) SPUtils.get(context, "user", user);
        return user;
    }

    /**
     * 删除本地保存的用户信息
     *
     * @param context
     */
    public static void removeUser(Context context) {
        SPUtils.remove(context, "Usersinfoid");
        SPUtils.remove(context, "Balance");
        SPUtils.remove(context, "Token");
        SPUtils.remove(context, "UserSignature");
        SPUtils.remove(context, "Usersinfo_Age");
        SPUtils.remove(context, "UsersinfoName");
        SPUtils.remove(context, "UsersinfoPhone");
        SPUtils.remove(context, "UsersinfoPhoto");
        SPUtils.remove(context, "UsersinfoSex");
        SPUtils.remove(context, "Zan");

    }

    /**
     * 将用户信息保存到本地
     *
     * @param context
     * @param user
     */
    public static void saveUser(Context context, Users user) {
        removeUser(context);
        SPUtils.put(context, "user", user);
    }


    private static ProgressDialog dialog;

    /**
     * 显示等待进度条
     */
    public static void showProgressDialog(Context context) {
        dialog = new ProgressDialog(context);
        //设置进度条风格，风格为圆形，旋转的
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        //设置ProgressDialog 提示信息
        dialog.setMessage("登录中...");
        //设置ProgressDialog 的进度条是否不明确
        dialog.setIndeterminate(false);
        //设置ProgressDialog 是否可以按退回按键取消
        dialog.setCancelable(true);
        //显示
        dialog.show();
    }

    /**
     * 销毁进度条
     */
    public static void destroyDialog() {
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
    }

}
