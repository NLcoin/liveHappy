package com.wxgh.livehappy.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wxgh.livehappy.R;
import com.wxgh.livehappy.tab02liu.HomeAdapter;
import com.wxgh.livehappy.tab02liu.SpacesItemDecoration;


/**
 * Created by Administrator on 2016/5/16.
 * 附近
 */
public class Tab0103 extends Fragment {
    private View view;
    //流布局
    private RecyclerView mRecyclerView;
    private HomeAdapter mAdapter;

    //获取上下文
    private Context mContext;

    public void setmContext(Context mContext) {
        this.mContext = mContext;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.tab0103, container,false);
        init();
        mRecyclerView = (RecyclerView) view.findViewById(R.id.id_recyclerview);
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));//设置瀑布流重点
        //设置item之间的间隔
        SpacesItemDecoration decoration = new SpacesItemDecoration(15);//设置瀑布流重点
        mRecyclerView.addItemDecoration(decoration);//设置瀑布流重点
        return view;
    }

    public void init(){
        initData();
    }


    private void initData() {
        //数据加载
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

}
