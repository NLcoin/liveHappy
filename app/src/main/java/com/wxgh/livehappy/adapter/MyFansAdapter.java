package com.wxgh.livehappy.adapter;

import android.app.Activity;
import android.content.Context;

import com.wxgh.livehappy.R;
import com.wxgh.livehappy.model.Users;

import java.util.List;

/**
 * Created by Administrator on 2016/7/22 0022.
 */
public class MyFansAdapter extends CommonAdapter<Users> {
    private int chose=1;
    private Activity activity;
    public MyFansAdapter(Context mContext, List<Users> mData, int layoutId,int chose,Activity activity) {
        super(mContext, mData, layoutId);
        this.chose=chose;
        this.activity=activity;
    }

    @Override
    public void convert(ViewHolder holder, Users users) {
        holder.setImageView(R.id.img_header,users.getUsersinfoPhoto());
        holder.setTextView(R.id.tv_userName,users.getUsersinfoName());
        holder.setTextView(R.id.tv_signature,users.getUserSignature());
        holder.setImageView(R.id.chose,chose,users,activity);
    }
}
