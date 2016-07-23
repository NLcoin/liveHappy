package com.wxgh.livehappy.adapter;

import android.content.Context;
import android.net.Uri;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wxgh.livehappy.utils.StaticManger;

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

    public ViewHolder setImageView(int viewId, int resId) {
        ImageView view = getView(viewId);
        view.setImageResource(resId);
        return this;
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
