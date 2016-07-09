package com.wxgh.livehappy.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wxgh.livehappy.R;


/**
 * Created by Administrator on 2016/5/16.
 */
public class Tab03 extends Fragment {
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.tab03, container,false);
        init();

        return view;
    }

    public void init(){

    }




    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

}
