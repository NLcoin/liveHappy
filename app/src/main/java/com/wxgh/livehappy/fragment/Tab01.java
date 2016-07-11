package com.wxgh.livehappy.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wxgh.livehappy.R;
import com.wxgh.livehappy.adapter.MainAdapter;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Administrator on 2016/5/16.
 */
public class Tab01 extends Fragment {
    private View view;

    private ViewPager mViewPager;
    private MainAdapter mAdapter;

    private Tab0101 tab0101;
    private Tab0102 tab0102;
    private Tab0103 tab0103;

    private LinearLayout lin_tab01;
    private LinearLayout lin_tab02;
    private LinearLayout lin_tab03;

    private TextView txt1;
    private TextView txt2;
    private TextView txt3;

    private List<Fragment> mFragments=new ArrayList<Fragment>();

    private Context mContext;

    public void setmContext(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.tab01, container,false);
        init();
        mAdapter=new MainAdapter(getChildFragmentManager());
        mAdapter.setFragmentList(mFragments);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setCurrentItem(1);


        return view;
    }

    /**
     * 加载数据
     */
    public void init(){
        mViewPager=(ViewPager)view.findViewById(R.id.tab01_vp);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                reset();
                switch (position){
                    case 0:
                        lin_tab01.setBackgroundResource(R.drawable.tab01);
                        txt1.setTextColor(Color.parseColor("#801A04"));
                        break;
                    case 1:
                        lin_tab02.setBackgroundResource(R.drawable.tab01);
                        txt2.setTextColor(Color.parseColor("#801A04"));
                        break;
                    case 2:
                        lin_tab03.setBackgroundResource(R.drawable.tab01);
                        txt3.setTextColor(Color.parseColor("#801A04"));
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        lin_tab01=(LinearLayout) view.findViewById(R.id.lin_tab01);
        lin_tab02=(LinearLayout) view.findViewById(R.id.lin_tab02);
        lin_tab03=(LinearLayout) view.findViewById(R.id.lin_tab03);

        txt1=(TextView)view.findViewById(R.id.txt1);
        txt2=(TextView)view.findViewById(R.id.txt2);
        txt3=(TextView)view.findViewById(R.id.txt3);

        onclickLinearLayout();

        initFragment();
    }

    public void onclickLinearLayout(){
        lin_tab01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lin_tab01.setBackgroundResource(R.drawable.tab01);
                txt1.setTextColor(Color.parseColor("#801A04"));
                mViewPager.setCurrentItem(0);
            }
        });
        lin_tab02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lin_tab02.setBackgroundResource(R.drawable.tab01);
                txt2.setTextColor(Color.parseColor("#801A04"));
                mViewPager.setCurrentItem(1);
            }
        });
        lin_tab03.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lin_tab03.setBackgroundResource(R.drawable.tab01);
                txt3.setTextColor(Color.parseColor("#801A04"));
                mViewPager.setCurrentItem(2);
            }
        });
    }

    /**
     * 初始化mFragments 数据
     */
    public void initFragment(){
        tab0101 = new Tab0101();
        tab0102= new Tab0102();
        tab0102.setmContext(mContext);
        tab0103= new Tab0103();
        mFragments.add(tab0101);
        mFragments.add(tab0102);
        mFragments.add(tab0103);
    }

    /**
     * 重置
     */
    public void reset(){
        lin_tab01.setBackgroundResource(R.drawable.test);
        lin_tab02.setBackgroundResource(R.drawable.test);
        lin_tab03.setBackgroundResource(R.drawable.test);

        txt1.setTextColor(Color.parseColor("#6C5B61"));
        txt2.setTextColor(Color.parseColor("#6C5B61"));
        txt3.setTextColor(Color.parseColor("#6C5B61"));
    }

}
