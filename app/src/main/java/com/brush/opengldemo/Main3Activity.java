package com.brush.opengldemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.brush.opengldemo.weight.ColorAdapter;
import com.brush.opengldemo.weight.ColorBean;

import java.util.ArrayList;
import java.util.Random;

public class Main3Activity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private ColorAdapter mColorAdapter;
    private ArrayList<ColorBean> mArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        findViews();
        creatDatas();
        mColorAdapter.setNewData(mArrayList);
    }

    private void creatDatas() {
        mArrayList = new ArrayList<>();
        for (int i=0;i<8;i++){
            ColorBean colorBean = new ColorBean();
            colorBean.alpha=255;
            Random random = new Random(10);
            int i1 = random.nextInt();
            colorBean.blue=20+i1*i;
            colorBean.green=20+i1*i;
            colorBean.red=20+i1*i;
            mArrayList.add(colorBean);
        }
    }

    private void findViews() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 4) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mColorAdapter = new ColorAdapter(null,this);

        mRecyclerView.setAdapter(mColorAdapter);
    }
}
