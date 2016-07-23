package com.wxgh.livehappy.adapter;

import android.content.Context;

import com.wxgh.livehappy.R;
import com.wxgh.livehappy.model.Users;

import java.util.List;

/**
 * Created by Administrator on 2016/7/22 0022.
 */
public class MyFansAdapter extends CommonAdapter<Users> {
    public MyFansAdapter(Context mContext, List<Users> mData, int layoutId) {
        super(mContext, mData, layoutId);
    }

    @Override
    public void convert(ViewHolder holder, Users users) {
        holder.setImageView(R.id.img_header,users.getUsersinfoPhoto());
        holder.setTextView(R.id.tv_userName,users.getUsersinfoName());
        holder.setTextView(R.id.tv_signature,users.getUserSignature());
    }
}
