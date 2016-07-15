package com.wxgh.livehappy;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.wxgh.livehappy.app.MyApplication;
import com.wxgh.livehappy.model.Users;
import com.wxgh.livehappy.utils.ConstantManger;
import com.wxgh.livehappy.utils.FileUtil;
import com.wxgh.livehappy.utils.KeyBoardUtils;
import com.wxgh.livehappy.utils.StaticManger;
import com.wxgh.livehappy.utils.Verification;
import com.wxgh.livehappy.view.SelectPicPopupWindow;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UsersInformationActivity extends Activity implements View.OnClickListener {
    private Users user = null;
    //用户手机号
    private TextView txt_user_phone;
    //用户名
    private EditText edit_username;
    //性别
    private TextView txt_userage;
    //年龄
    private EditText txt_usersex;
    //签名
    private EditText edit_userSignature;
    private SimpleDraweeView draweeView;//头像
    private RelativeLayout touxiangupdate;
    private TextView create; //完成
    //自定义的弹出框类
    private SelectPicPopupWindow menuWindow;
    private static final String IMAGE_FILE_NAME = "avatarImage.jpg";// 头像文件名称
    private static final int REQUESTCODE_PICK = 0; // 相册选图标记
    private static final int REQUESTCODE_TAKE = 1; // 相机拍照标记
    private static final int REQUESTCODE_CUTTING = 2; // 图片裁切标记
    private String urlpath; // 图片本地路径
    private ImageView imageButton2;//返回按钮
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(getApplicationContext());
        setContentView(R.layout.activity_users_information);
        user = StaticManger.getCurrentUser(this);
        initView();
    }

    /**
     * 初始化控件 和数据
     */
    private void initView() {
        imageButton2 = (ImageView) findViewById(R.id.imageButton2);
        touxiangupdate = (RelativeLayout) findViewById(R.id.touxiangupdate);
        draweeView = (SimpleDraweeView) findViewById(R.id.img_header);//头像
        txt_user_phone = (TextView) findViewById(R.id.txt_user_phone);
        edit_username = (EditText) findViewById(R.id.edit_username);
        edit_userSignature = (EditText) findViewById(R.id.edit_userSignature);
        txt_userage = (TextView) findViewById(R.id.txt_usergender);
        txt_usersex = (EditText) findViewById(R.id.txt_usersex);
        create = (TextView) findViewById(R.id.create);
        if (!"".equals(user.getUsersinfoPhone())) {
            txt_user_phone.setText(Verification.hidePhoneNum(user.getUsersinfoPhone()));
        }
        edit_username.setText(user.getUsersinfoName());
        edit_userSignature.setText(user.getUserSignature());
        txt_userage.setText(user.getUsersinfo_Age());
        txt_usersex.setText(user.getUsersinfoSex());
        Uri uri = Uri.parse(StaticManger.getImgPath(user.getUsersinfoPhoto()));
        draweeView.setImageURI(uri);
        touxiangupdate.setOnClickListener(this);
        edit_username.setOnClickListener(this);
        edit_userSignature.setOnClickListener(this);
        txt_userage.setOnClickListener(this);
        txt_usersex.setOnClickListener(this);
        create.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.touxiangupdate:
                //实例化SelectPicPopupWindow
                menuWindow = new SelectPicPopupWindow(UsersInformationActivity.this, itemsOnClick, "拍照", "相册");
                //显示窗口
                menuWindow.showAtLocation(UsersInformationActivity.this.findViewById(R.id.main), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); //设置layout在PopupWindow中显示的位置
                break;
            case R.id.edit_username:
                KeyBoardUtils.openKeybord(edit_username, this);
                break;
            case R.id.edit_userSignature:
                KeyBoardUtils.openKeybord(edit_username, this);
                break;
            case R.id.txt_usergender:
                //实例化SelectPicPopupWindow
                menuWindow = new SelectPicPopupWindow(UsersInformationActivity.this, itemsOnClickGender, "男", "女");
                //显示窗口
                menuWindow.showAtLocation(UsersInformationActivity.this.findViewById(R.id.main), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); //设置layout在PopupWindow中显示的位置
                break;
            case R.id.txt_usersex:
                KeyBoardUtils.openKeybord(edit_username, this);
                break;
            case R.id.create:
                String UsersinfoName = edit_username.getText().toString();
                String UserSignature = edit_userSignature.getText().toString();
                String usergender = txt_userage.getText().toString();
                String usersex = txt_usersex.getText().toString();
                if ("".equals(UsersinfoName) || UsersinfoName.length() > 6) {
                    Toast.makeText(UsersInformationActivity.this, "用户名不符合规范!要求长度大于0小于6!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (UserSignature.length() > 20) {
                    Toast.makeText(UsersInformationActivity.this, "签名不符合规范!要求长度小于20!", Toast.LENGTH_SHORT).show();
                    return;
                }
                int age = Integer.parseInt(usersex);
                if (age <= 0 || age > 100) {
                    Toast.makeText(UsersInformationActivity.this, "年龄要大于0小于100!", Toast.LENGTH_SHORT).show();
                    return;
                }
                Users users = new Users();
                users.setUsersinfoName(UsersinfoName);
                users.setUserSignature(UserSignature);
                users.setUsersinfoSex(usersex);
                users.setUsersinfo_Age(usergender);
                users.setUsersinfoid(user.getUsersinfoid());
                users.setUsersinfoPhoto(user.getUsersinfoPhoto());
                users.setBalance(user.getBalance());
                users.setToken(user.getToken());
                users.setZan(user.getZan());
                users.setUsersinfoPhone(user.getUsersinfoPhone());
                StaticManger.saveUser(UsersInformationActivity.this, users);
                String url = ConstantManger.SERVER_IP + ConstantManger.UPDATEUSERSINFOPICPATH;
                RequestBody requestBody = new FormBody.Builder().add("UserName", UsersinfoName).add("UserGender", usergender).add("UserSignature", UserSignature).add("UserSex", usersex)
                        .add("PicPath", user.getUsersinfoPhoto()).add("UserID", user.getUsersinfoid())
                        .build();
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
                                handler.sendEmptyMessage(2);
                            } else {
                                handler.sendEmptyMessage(1);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
                break;
            case R.id.imageButton2:
                finish();
                break;
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    Toast.makeText(MyApplication.getContext(), "网络异常！", Toast.LENGTH_LONG).show();
                    break;
                case 1:
                    Toast.makeText(MyApplication.getContext(), "修改失败！", Toast.LENGTH_LONG).show();
                    break;
                case 2:
                    Toast.makeText(MyApplication.getContext(), "修改成功！", Toast.LENGTH_LONG).show();
                    UsersInformationActivity.this.finish();
                    break;
            }
        }
    };


    //为修改头像弹出窗口实现监听类
    private View.OnClickListener itemsOnClick = new View.OnClickListener() {
        public void onClick(View v) {
            menuWindow.dismiss();
            switch (v.getId()) {
                case R.id.btn_take_photo:
                    Intent takeIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    // 下面这句指定调用相机拍照后的照片存储的路径
                    takeIntent
                            .putExtra(MediaStore.EXTRA_OUTPUT, Uri
                                    .fromFile(new File(Environment
                                            .getExternalStorageDirectory(), IMAGE_FILE_NAME)));
                    startActivityForResult(takeIntent, REQUESTCODE_TAKE);
                    break;
                case R.id.btn_pick_photo:
                    Intent pickIntent = new Intent(Intent.ACTION_PICK, null);
                    pickIntent
                            .setDataAndType(
                                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                    "image/*");
                    startActivityForResult(pickIntent, REQUESTCODE_PICK);
                    break;
                default:
                    break;
            }
        }
    };
    //为改变性别弹出窗口实现监听类
    private View.OnClickListener itemsOnClickGender = new View.OnClickListener() {
        public void onClick(View v) {
            menuWindow.dismiss();
            switch (v.getId()) {
                case R.id.btn_take_photo:
                    txt_userage.setText("男");
                    break;
                case R.id.btn_pick_photo:
                    txt_userage.setText("女");
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUESTCODE_TAKE:// 调用相机拍照
                File temp = new File(Environment.getExternalStorageDirectory()
                        + "/" + IMAGE_FILE_NAME);
                startPhotoZoom(Uri.fromFile(temp));
                break;
            case REQUESTCODE_PICK:// 直接从相册获取
                try {
                    startPhotoZoom(data.getData());
                } catch (NullPointerException e) {
                    e.printStackTrace();// 用户点击取消操作
                }
                break;
            case REQUESTCODE_CUTTING:// 取得裁剪后的图片
                if (data != null) {
                    setPicToView(data);//保存图片到本地，并且上传到服务器
                }
                break;
        }
    }


    /**
     * 裁剪图片方法实现
     *
     * @param uri
     */
    public void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, REQUESTCODE_CUTTING);
    }

    /**
     * 保存裁剪之后的图片数据
     *
     * @param picdata
     */
    private void setPicToView(Intent picdata) {
        Bundle extras = picdata.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            Drawable drawable = new BitmapDrawable(getResources(), photo);
            urlpath = FileUtil.saveFile(UsersInformationActivity.this, user.getUsersinfoid() + "20160715045155.jpg", photo);
            File file = new File(urlpath);
            Uri uri = Uri.fromFile(file);
            draweeView.setImageURI(uri);
            user.setUsersinfoPhoto(user.getUsersinfoid() + "20160715045155.jpg");
            Verification.uploadImg(ConstantManger.SERVER_IP + ConstantManger.UPDATE_IMG, file, UsersInformationActivity.this, user);
        }
    }

}