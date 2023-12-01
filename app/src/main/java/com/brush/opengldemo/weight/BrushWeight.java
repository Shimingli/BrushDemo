package com.brush.opengldemo.weight;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.brush.opengldemo.MyGLSurfaceView;
import com.brush.opengldemo.R;
import com.brush.opengldemo.settings.SettingsData;
import com.brush.opengldemo.settings.SettingsManager;

/**
 * Created by shiming on 2017/8/15.
 */

public class BrushWeight extends FrameLayout implements View.OnClickListener {

    private TextView mTextView1;
    private TextView mTextView2;
    private TextView mTextView3;
    private TextView mTextView4;
    private MyGLSurfaceView mMyGLSurfaceView;
    private SettingsManager settingsManager;
    private SettingsData settingsData;

    public BrushWeight(@NonNull Context context) {
        this(context,null);
    }


    public BrushWeight(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        settingsManager = SettingsManager.getInstance(context);
        settingsData = settingsManager.getSettingsData().clone();
        initView();
    }

    private void initView() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        final View child = inflater.inflate(R.layout.brush_weight_layout, this, false);
        addView(child);
        mTextView1 = (TextView) findViewById(R.id.text1);
        mTextView2 = (TextView) findViewById(R.id.text2);
        mTextView3 = (TextView) findViewById(R.id.text3);
        mTextView4 = (TextView) findViewById(R.id.text4);
        mMyGLSurfaceView = (MyGLSurfaceView) findViewById(R.id.myglsurface_view);

        mTextView1.setOnClickListener(this);
        mTextView2.setOnClickListener(this);
        mTextView3.setOnClickListener(this);
        mTextView4.setOnClickListener(this);

        settingsData.setNumBristles(10);
        settingsManager.saveSettingsData(settingsData);

    }
    public void onPause(){
        mMyGLSurfaceView.onPause();
    }
    public void onResume(){
        mMyGLSurfaceView.onResume();
    }
    public  MyGLSurfaceView getSurfaceView(){
        return mMyGLSurfaceView;
    }
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.text1:
                showOrHideMySurfaceView();
                break;
            case R.id.text2://goto
                mIActionCallback.gotoWetingActivity(v);
                break;
            case R.id.text3:
                mMyGLSurfaceView.undo();
                break;
            case R.id.text4:
             mMyGLSurfaceView.clearScreen();
                break;

        }
    }

    private void showOrHideMySurfaceView() {
        if (mMyGLSurfaceView.getVisibility()==VISIBLE){
            mMyGLSurfaceView.setVisibility(GONE);
        }else {
            mMyGLSurfaceView.setVisibility(VISIBLE);
        }
    }
    public IActionCallback mIActionCallback;

    public void setgoToAcitivity(IActionCallback a) {
        mIActionCallback=a;
    }

    public interface IActionCallback {
        void gotoWetingActivity(View view);
    }
}
