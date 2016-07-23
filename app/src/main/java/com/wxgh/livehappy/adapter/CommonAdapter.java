package com.wxgh.livehappy.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by Administrator on 2016/5/24 0024.
 */
public abstract class CommonAdapter<T> extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private List<T> mData;
    private int mLayoutId;

    public CommonAdapter(Context mContext, List<T> mData, int layoutId) {
        this.mContext = mContext;
        this.mData = mData;
        this.mLayoutId = layoutId;
        this.mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        if (mData != null)
            return mData.size();
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = ViewHolder.get(mContext, convertView, position, mLayoutId, parent);
        convert(viewHolder, (T) getItem(position));
        return viewHolder.getConverView();
    }

    //赋值方法
    public abstract void convert(ViewHolder holder, T t);
}
