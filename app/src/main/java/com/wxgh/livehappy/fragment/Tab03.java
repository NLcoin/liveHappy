package com.wxgh.livehappy.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.view.SimpleDraweeView;
import com.wxgh.livehappy.R;


/**
 * Created by Administrator on 2016/5/16.
 */
public class Tab03 extends Fragment {
    private View view;
    private String imagepath="http://img5.imgtn.bdimg.com/it/u=3694648255,2464804994&fm=21&gp=0.jpg";
    private SimpleDraweeView draweeView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.tab03, container,false);
        init();
        Uri uri=Uri.parse(imagepath);
        draweeView.setImageURI(uri);
        return view;
    }

    public void init(){
        draweeView=(SimpleDraweeView)view.findViewById(R.id.img_header);
    }




    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

}
