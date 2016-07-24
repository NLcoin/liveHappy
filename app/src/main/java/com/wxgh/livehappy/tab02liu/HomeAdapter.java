package com.wxgh.livehappy.tab02liu;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.wxgh.livehappy.R;
import com.wxgh.livehappy.model.MyLive;
import com.wxgh.livehappy.utils.StaticManger;
import com.wxgh.livehappy.utils.Verification;

import java.util.List;

/**
 * Created by 98016 on 2016/7/8 0008.
 */
public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder> {
    public HomeAdapter(Context context, List<MyLive.Model> mDatas){
        this.context = context;
        this.mDatas = mDatas;
    }
    private OnItemClickLitener mOnItemClickLitener;
    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener){
        this.mOnItemClickLitener = mOnItemClickLitener;
    }
    private List<MyLive.Model> mDatas;
    private Context context;
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.tab02_item_layout, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }
    public interface OnItemClickLitener {
        void onItemClick(View view, int position);
    }
    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        MyLive.Model model=mDatas.get(position);
        Bitmap bitmap= Verification.getBitmap(model.BroadcastAddress,50,50);
        if (bitmap!=null)
        holder.img.setImageBitmap(bitmap);
        Uri uriimgheader=Uri.parse(StaticManger.getImgPath(model.PicPath));
        holder.img_header.setImageURI(uriimgheader);
        holder.txt_username.setText(model.UserName);
        holder.txt_likenumber.setText(model.UserCount);
        holder.txt_title.setText(model.liveTitle);
        if (mOnItemClickLitener != null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
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
