package com.wxgh.livehappy.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.wxgh.livehappy.app.MyApplication;
import com.wxgh.livehappy.model.Users;
import com.wxgh.livehappy.view.ViewDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

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
     * 隐藏电话号码中间四位
     *
     * @param phoneNum
     * @return
     */
    public static String hidePhoneNum(String phoneNum)
    {
        return phoneNum.substring(0, 3) + "****" + phoneNum.substring(7, phoneNum.length());
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
    /**
     * 文件上传
     */
    public static void uploadImg(String url, File file, final Context context, final Users users) {
        final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");
        OkHttpClient client = new OkHttpClient();
        // mImgUrls为存放图片的url集合
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        builder.addFormDataPart("file", file.getName(), RequestBody.create(MEDIA_TYPE_PNG, file));
        MultipartBody requestBody = builder.build();
        //构建请求
        final Request request = new Request.Builder()
                .url(url)//地址
                .post(requestBody)//添加请求体
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                handler.sendEmptyMessage(0);
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String str=response.body().string();
                try {
                    JSONObject a = new JSONObject(str);
                    String error=a.getString("error");
                    if ("200".equals(error)){
                        handler.sendEmptyMessage(2);
                    }else {
                        handler.sendEmptyMessage(1);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    private static Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    Toast.makeText(MyApplication.getContext(),"网络异常！",Toast.LENGTH_LONG).show();
                    break;
                case 1:
                    Toast.makeText(MyApplication.getContext(),"上传失败！",Toast.LENGTH_LONG).show();
                    break;
                case 2:
                    Toast.makeText(MyApplication.getContext(),"上传成功！",Toast.LENGTH_LONG).show();
                    break;
            }
        }
    };
}
