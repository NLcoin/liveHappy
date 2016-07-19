package com.wxgh.livehappy.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.letv.controller.PlayProxy;
import com.letv.universal.iplay.EventPlayProxy;
import com.wxgh.livehappy.R;
import com.wxgh.livehappy.app.MyApplication;
import com.wxgh.livehappy.le_play.PlayNoSkinActivity;
import com.wxgh.livehappy.model.MyLive;
import com.wxgh.livehappy.tab02liu.HomeAdapter;
import com.wxgh.livehappy.tab02liu.SpacesItemDecoration;
import com.wxgh.livehappy.utils.ConstantManger;

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
 * Created by Administrator on 2016/5/16.
 * 关注
 */
public class Tab0101 extends Fragment {
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
        view=inflater.inflate(R.layout.tab0101, container,false);
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
        String url = ConstantManger.SERVER_IP + ConstantManger.SELECTALLLIVE;
        RequestBody requestBody = new FormBody.Builder().build();
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
                        Message message = new Message();
                        message.what = 2;
                        message.obj = json;
                        handler.sendMessage(message);
                    } else {
                        handler.sendEmptyMessage(1);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    Toast.makeText(MyApplication.getContext(), "网络异常！", Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    break;
                case 2:
                    String json = (String) msg.obj;
                    Gson gson = new Gson();
                    final MyLive myLive = gson.fromJson(json, MyLive.class);
                    mAdapter = new HomeAdapter(mContext, myLive.list);
                    mRecyclerView.setAdapter(mAdapter);
                    //单击事件
                    mAdapter.setOnItemClickLitener(new HomeAdapter.OnItemClickLitener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            MyLive.Model model=myLive.list.get(position);
                            Intent intent = new Intent(getActivity(), PlayNoSkinActivity.class);
                            Bundle mBundle  = new Bundle();
                            mBundle.putInt(PlayProxy.PLAY_MODE, EventPlayProxy.PLAYER_LIVE);
                            mBundle.putString("path",model.BroadcastAddress.trim());
                            intent.putExtra(PlayNoSkinActivity.DATA, mBundle);
                            startActivity(intent);
                        }
                    });
                    break;
            }
        }
    };




    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

}
