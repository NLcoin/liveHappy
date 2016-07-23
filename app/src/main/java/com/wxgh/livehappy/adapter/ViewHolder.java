package com.wxgh.livehappy.adapter;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wxgh.livehappy.R;
import com.wxgh.livehappy.app.MyApplication;
import com.wxgh.livehappy.model.Users;
import com.wxgh.livehappy.utils.ConstantManger;
import com.wxgh.livehappy.utils.StaticManger;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/5/24 0024.
 */
public class ViewHolder {
    private Context mContext;//上下文对象
    private View converView;//当前行布局
    private SparseArray<View> mView;//当前行布局上的所有控件
    private int mPosition;//当前行的Position
    public ViewHolder(Context mContext, int mPosition, int resId, ViewGroup parent) {
        this.mContext = mContext;
        this.mPosition = mPosition;
        this.converView = LayoutInflater.from(mContext).inflate(resId, parent, false);
        this.mView = new SparseArray<View>();
        converView.setTag(this);
    }

    public static ViewHolder get(Context mContext, View convertView, int mPosition, int resId, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder(mContext, mPosition, resId, parent);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            viewHolder.mPosition = mPosition;
        }
        return viewHolder;
    }

    public View getConverView() {
        return converView;
    }

    public int getPosition() {
        return mPosition;
    }

    /**
     * 根据控件Id获取行布局上控件的方法
     *
     * @param viewId
     * @param <T>
     * @return
     */
    public <T extends View> T getView(int viewId) {
        View view = mView.get(viewId);
        if (view == null) {
            view = converView.findViewById(viewId);
            mView.put(viewId, view);
        }
        return (T) view;
    }

    /**
     * 给行布局上的控件赋值
     */
    public ViewHolder setTextView(int viewId, String str) {
        TextView view = getView(viewId);
        view.setText(str);
        return this;
    }

    public ViewHolder setImageView(int viewId, final int resId, final Users users, final Activity activity) {
        ImageView view = getView(viewId);
        final Users user = StaticManger.getCurrentUser(mContext);
        if (resId==1){
            view.setImageResource(R.drawable.cancelfollow);
        }else if (resId==2){
            view.setImageResource(R.drawable.plusfollow);
        } else {
            view.setImageResource(R.drawable.cancelfollow);
        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url="";
                if (resId==1){
                     url= ConstantManger.SERVER_IP + ConstantManger.DELETERELATIONSHIP;
                }else if (resId==2){
                     url= ConstantManger.SERVER_IP + ConstantManger.INSERTRELATIONSHIP;
                }else {
                     url= ConstantManger.SERVER_IP + ConstantManger.DELETERELATIONSHIP;
                }
                chose(url,users,user,resId,activity);
            }
        });

        return this;
    }
    private void chose(String url, Users friend, final Users user, final int resid, final Activity activity){
        RequestBody requestBody = new FormBody.Builder().add("Uid", user.getUsersinfoid()).add("Frid", friend.getUsersinfoid()).build();
        Request request = new Request.Builder().url(url).post(requestBody).build();
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();
                try {
                    JSONObject a = new JSONObject(json);
                    String error = a.getString("error");
                    if ("200".equals(error)) {//成功
                        referce(resid,user,activity);
                    } else {}
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void referce(final int resid, Users user, final Activity activity) {
        MyApplication mAPP = (MyApplication)activity.getApplication();
        // 获得该共享变量实例
        final Handler mHandler = mAPP.getmofHandler();
        String url="";
        if (resid==1){
            url = ConstantManger.SERVER_IP + ConstantManger.SELECTRELATIONSHIPBYID;
        }else if (resid==2){
            url = ConstantManger.SERVER_IP + ConstantManger.SELECTRELATIONSHIPBYFRID;
        }else{
            url = ConstantManger.SERVER_IP + ConstantManger.SELECTMYFRIEND;
        }
        RequestBody requestBody = new FormBody.Builder().add("Uid", user.getUsersinfoid()).build();
        Request request = new Request.Builder().url(url).post(requestBody).build();
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mHandler.sendEmptyMessage(0);
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (resid==2){
                    return;
                }
                String json = response.body().string();
                try {
                    JSONObject a = new JSONObject(json);
                    String error = a.getString("error");
                    if ("200".equals(error)) {//成功
                        Message message=new Message();
                        message.what=2;
                        message.obj=json;
                        mHandler.sendMessage(message);
                    } else {
                        mHandler.sendEmptyMessage(2);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public ViewHolder setImageView(int viewId) {
        ImageView view = getView(viewId);
        view.setVisibility(View.GONE);
        return this;
    }

    public ViewHolder setImageView(int viewId,String url) {
        ImageView view = getView(viewId);
        Uri uri = Uri.parse(StaticManger.getImgPath(url));

        view.setImageURI(uri);
        return this;
    }
}
