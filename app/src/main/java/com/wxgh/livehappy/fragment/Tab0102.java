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
import android.widget.Toast;

import com.wxgh.livehappy.MainActivity;
import com.wxgh.livehappy.R;
import com.wxgh.livehappy.tab02liu.HomeAdapter;
import com.wxgh.livehappy.tab02liu.SpacesItemDecoration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by Administrator on 2016/5/16.
 * 热门
 */
public class Tab0102 extends Fragment {
    private View view;
    //流布局
    private RecyclerView mRecyclerView;
    private List<Map<String,Object>> mDatas;
    private HomeAdapter mAdapter;

    //获取上下文
    private Context mContext;
    public void setmContext(Context mContext) {
        this.mContext = mContext;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.tab0102, container,false);
        init();
        mRecyclerView = (RecyclerView)view.findViewById(R.id.id_recyclerview);
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));//设置瀑布流重点
        mRecyclerView.setAdapter(mAdapter);
        //设置item之间的间隔
        SpacesItemDecoration decoration = new SpacesItemDecoration(15);//设置瀑布流重点
        mRecyclerView.addItemDecoration(decoration);//设置瀑布流重点

        //单击事件
        mAdapter.setOnItemClickLitener(new HomeAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(mContext, position + " click",
                        Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }

    public void init(){

        mDatas = new ArrayList<Map<String,Object>>();
        Map<String,Object> map=null;
        for (int i = 0; i < 35; i++) {
            map=new HashMap<String,Object>();
            map.put("img","http://img4.imgtn.bdimg.com/it/u=3656820678,353780200&fm=11&gp=0.jpg");
            map.put("imgheader","http://img4.imgtn.bdimg.com/it/u=697890588,3646918970&fm=21&gp=0.jpg");
            map.put("username","徐博"+i);
            map.put("likenumber","1000"+i);
            map.put("title","徐博首秀"+i);
            mDatas.add(map);
        }
        mAdapter = new HomeAdapter(mContext, mDatas);
    }




    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

}
