package com.brush.opengldemo.shiming;

import android.graphics.Color;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.brush.opengldemo.R;
import com.brush.opengldemo.settings.SettingsData;
import com.brush.opengldemo.settings.SettingsManager;
import com.brush.opengldemo.weight.ColorAdapter;
import com.brush.opengldemo.weight.ColorBean;

import java.util.ArrayList;
import java.util.Random;

public class SettingPenConfigActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private ColorAdapter mColorAdapter;
    private ArrayList<ColorBean> mArrayList;
    private SettingsManager settingsManager;
    private SettingsData settingsData;
    private LinearLayout mContainer_1;
    private LinearLayout mContainer_2;
    private LinearLayout mContainer_3;
    private ImageView mPenWidth_1;
    private ImageView mPenWidth_2;
    private ImageView mPenWidth_3;
    private int mArgb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seting_pen_config);
        settingsManager = SettingsManager.getInstance(this);
        settingsData = settingsManager.getSettingsData().clone();
        findViews();
        creatDatas();
        mColorAdapter.setNewData(mArrayList);
        mArgb = Color.argb( 255,settingsData.getColorWrapper().getRed(),settingsData.getColorWrapper().getGreen(), settingsData.getColorWrapper().getBlue());
    }

    @Override
    protected void onResume() {
        super.onResume();

        mPenWidth_1.setColorFilter(mArgb);
        mPenWidth_2.setColorFilter(mArgb);
        mPenWidth_3.setColorFilter(mArgb);
    }

    private void creatDatas() {
        mArrayList = new ArrayList<>();
        for (int i=0;i<8;i++){
            ColorBean colorBean = new ColorBean();
            colorBean.alpha=255;
            Random random = new Random(22);
            int i1 = random.nextInt();
            colorBean.blue=20+i1*i;
            colorBean.green=20+i1*i;
            colorBean.red=20+i1*i;
            mArrayList.add(colorBean);
        }
    }

    private void findViews() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        mContainer_1 = (LinearLayout) findViewById(R.id.px_container1);
        mContainer_2 = (LinearLayout) findViewById(R.id.px_container2);
        mContainer_3 = (LinearLayout) findViewById(R.id.px_container3);

        mPenWidth_1 = (ImageView) findViewById(R.id.textView1);
        mPenWidth_2 = (ImageView) findViewById(R.id.textView2);
        mPenWidth_3 = (ImageView) findViewById(R.id.textView3);



        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 4) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mColorAdapter = new ColorAdapter(null,this);
        mRecyclerView.setAdapter(mColorAdapter);
        mColorAdapter.setOnItemClickListener(new ColorAdapter.OnItemClickListener() {
            @Override
            public void onClick(View v, ColorBean colorBean) {
                int rgb = Color.rgb(colorBean.red, colorBean.green, colorBean.blue);
                settingsData.getColorWrapper().setColorWithoutAlpha(rgb);
                settingsManager.saveSettingsData(settingsData);
                mPenWidth_1.setColorFilter(rgb);
                mPenWidth_2.setColorFilter(rgb);
                mPenWidth_3.setColorFilter(rgb);

            }
        });

        mContainer_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnimationSet animationSet = new AnimationSet(true);
                ScaleAnimation scaleAnimation = new ScaleAnimation(1,1.5f,1,1.5f,
                        Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
                //3秒完成动画
                scaleAnimation.setDuration(1000);
                //将AlphaAnimation这个已经设置好的动画添加到 AnimationSet中
                animationSet.addAnimation(scaleAnimation);
                animationSet.setFillAfter(true);
                animationSet.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        settingsData.setPaintWidht(10);
                        settingsManager.saveSettingsData(settingsData);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                mPenWidth_1.startAnimation(animationSet);
            }
        });
        mContainer_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnimationSet animationSet = new AnimationSet(true);
                ScaleAnimation scaleAnimation = new ScaleAnimation(1,1.5f,1,1.5f,
                        Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
                //3秒完成动画
                scaleAnimation.setDuration(1000);
                //将AlphaAnimation这个已经设置好的动画添加到 AnimationSet中
                animationSet.addAnimation(scaleAnimation);
                animationSet.setFillAfter(true);
                animationSet.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        settingsData.setPaintWidht(15);
                        settingsManager.saveSettingsData(settingsData);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                mPenWidth_2.startAnimation(animationSet);
            }
        });
        mContainer_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnimationSet animationSet = new AnimationSet(true);
                ScaleAnimation scaleAnimation = new ScaleAnimation(1,1.5f,1,1.5f,
                        Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
                //3秒完成动画
                scaleAnimation.setDuration(1000);
                //将AlphaAnimation这个已经设置好的动画添加到 AnimationSet中
                animationSet.addAnimation(scaleAnimation);
                animationSet.setFillAfter(true);
                animationSet.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        settingsData.setPaintWidht(25);
                        settingsManager.saveSettingsData(settingsData);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                mPenWidth_3.startAnimation(animationSet);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
