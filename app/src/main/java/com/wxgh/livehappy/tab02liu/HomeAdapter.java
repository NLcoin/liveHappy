package com.wxgh.livehappy.tab02liu;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.wxgh.livehappy.R;

import java.util.List;
import java.util.Map;

/**
 * Created by 98016 on 2016/7/8 0008.
 */
public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder> {
    public HomeAdapter(Context context, List<Map<String,Object>> mDatas) {
        this.context = context;
        this.mDatas = mDatas;
    }
    private OnItemClickLitener mOnItemClickLitener;
    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener)
    {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }


    private List<Map<String,Object>> mDatas;
    private Context context;

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.tab02_item_layout, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    public interface OnItemClickLitener
    {
        void onItemClick(View view, int position);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        Uri uriimg=Uri.parse(mDatas.get(position).get("img").toString());
        holder.img.setImageURI(uriimg);
        Uri uriimgheader=Uri.parse(mDatas.get(position).get("imgheader").toString());
        holder.img_header.setImageURI(uriimgheader);
        holder.txt_username.setText(mDatas.get(position).get("username").toString());
        holder.txt_likenumber.setText(mDatas.get(position).get("likenumber").toString());
        holder.txt_title.setText(mDatas.get(position).get("title").toString());

        if (mOnItemClickLitener != null){
            holder.itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickLitener.onItemClick(holder.itemView, pos);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        SimpleDraweeView img;
        SimpleDraweeView img_header;
        TextView txt_username;
        TextView txt_likenumber;
        TextView txt_title;

        public MyViewHolder(View view) {
            super(view);
            img=(SimpleDraweeView)view.findViewById(R.id.img);
            img_header=(SimpleDraweeView)view.findViewById(R.id.img_header);
            txt_username = (TextView) view.findViewById(R.id.txt_username);
            txt_likenumber = (TextView) view.findViewById(R.id.txt_likenumber);
            txt_title = (TextView) view.findViewById(R.id.txt_title);

        }
    }
}
