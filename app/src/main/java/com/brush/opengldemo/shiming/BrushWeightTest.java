package com.brush.opengldemo.shiming;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.brush.opengldemo.R;

/**
 * Created by shiming on 2017/8/15.
 */

public class BrushWeightTest extends FrameLayout implements View.OnClickListener {

    private TextView mTextView1;
    private TextView mTextView2;
    private TextView mTextView3;
    private TextView mTextView4;
    private DrawView mMyGLSurfaceView;
    private TextView mTextView5;
    private TextView mTextView6;

    public BrushWeightTest(@NonNull Context context) {
        this(context,null);
    }


    public BrushWeightTest(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        final View child = inflater.inflate(R.layout.brush_weight_layout_test, this, false);
        addView(child);
        mTextView1 = (TextView) findViewById(R.id.text1);
        mTextView2 = (TextView) findViewById(R.id.text2);
        mTextView3 = (TextView) findViewById(R.id.text3);
        mTextView4 = (TextView) findViewById(R.id.text4);
        mTextView5 = (TextView) findViewById(R.id.text5);
        mTextView6 = (TextView) findViewById(R.id.text6);
        mMyGLSurfaceView = (DrawView) findViewById(R.id.myglsurface_view);
        mMyGLSurfaceView.setCurrentState(PathState.getInstance());
        mTextView1.setOnClickListener(this);
        mTextView2.setOnClickListener(this);
        mTextView3.setOnClickListener(this);
        mTextView4.setOnClickListener(this);
        mTextView5.setOnClickListener(this);
        mTextView6.setOnClickListener(this);

    }

    public  DrawView getSurfaceView(){
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
                mMyGLSurfaceView.setCanvasCode(TestActivity.CANVAS_UNDO);
                mMyGLSurfaceView.invalidate();
                break;
            case R.id.text4:
                mMyGLSurfaceView.setCanvasCode(TestActivity.CANVAS_RESET);
                mMyGLSurfaceView.invalidate();
                dataReset();
                break;
            case R.id.text5:
                mIActionCallback.save(mMyGLSurfaceView);

                break;
            case R.id.text6:
                mIActionCallback.creatNewLine();
                break;

        }
    }
    private void dataReset() {
        DrawDataUtils.getInstance().getSaveDrawDataList().clear();
        DrawDataUtils.getInstance().getShearDrawDataList().clear();
        CommandUtils.getInstance().getRedoCommandList().clear();
        CommandUtils.getInstance().getUndoCommandList().clear();
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

    public void clearScreen() {
        mMyGLSurfaceView.setCanvasCode(TestActivity.CANVAS_RESET);
        mMyGLSurfaceView.invalidate();
        dataReset();
    }

    public void onResume() {
        mMyGLSurfaceView.onResume();
    }

    public interface IActionCallback {
        void gotoWetingActivity(View view);
        void save(DrawView view);
        void creatNewLine();
    }
}
