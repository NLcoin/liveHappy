package com.wxgh.livehappy.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.wxgh.livehappy.R;
import com.wxgh.livehappy.utils.KeyBoardUtils;


/**
 * Created by Administrator on 2016/5/16.
 */
public class Tab02 extends Fragment implements View.OnClickListener {
    private View view;
    private EditText et_title;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.tab02, container,false);
        init();

        return view;
    }

    public void init(){
        et_title=(EditText)view.findViewById(R.id.et_title);
        et_title.setOnClickListener(this);
    }




    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.et_title:
                Toast.makeText(getContext(),"132456",Toast.LENGTH_LONG).show();
                KeyBoardUtils.openKeybord(et_title,getContext());
                break;
        }
    }
}
