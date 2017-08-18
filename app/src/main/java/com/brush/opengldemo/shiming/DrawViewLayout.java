package com.brush.opengldemo.shiming;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewStub;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.brush.opengldemo.R;

/**
 * Created by shiming on 2017/8/15.
 * 封装了drawViewlayou的一些操作
 */

public class DrawViewLayout extends FrameLayout implements View.OnClickListener {

    private TextView mShowKeyboard;
    private TextView mSettingPen;
    private TextView mGotoPreviousStep;
    private TextView mClearCanvas;
    private DrawPenView mDrawView;
    private TextView mSaveBitmap;
    private TextView mEditTextNeedN;
    private ViewStub mViewStub;

    public DrawViewLayout(@NonNull Context context) {
        this(context, null);
    }


    public DrawViewLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        final View child = inflater.inflate(R.layout.brush_weight_layout_test, this, false);
        addView(child);

        mShowKeyboard = (TextView) findViewById(R.id.text1);
        mSettingPen = (TextView) findViewById(R.id.text2);
        mGotoPreviousStep = (TextView) findViewById(R.id.text3);
        mClearCanvas = (TextView) findViewById(R.id.text4);
        mSaveBitmap = (TextView) findViewById(R.id.text5);
        mEditTextNeedN = (TextView) findViewById(R.id.text6);
        mViewStub = (ViewStub) findViewById(R.id.draw_view);

        mShowKeyboard.setOnClickListener(this);
        mSettingPen.setOnClickListener(this);
        mGotoPreviousStep.setOnClickListener(this);
        mClearCanvas.setOnClickListener(this);
        mSaveBitmap.setOnClickListener(this);
        mEditTextNeedN.setOnClickListener(this);


    }

    private void setDrawViewConfig() {
        mDrawView = (DrawPenView) findViewById(R.id.myglsurface_view);
        mDrawView.setCurrentState(PathState.getInstance());
        mDrawView.setGetTimeListener(new DrawPenView.TimeListener() {
            @Override
            public void getTime(long l) {
                mIActionCallback.getUptime(l);
            }

            @Override
            public void stopTime() {
                mIActionCallback.stopTime();
            }
        });
    }

    public DrawPenView getSurfaceView() {
        return mDrawView;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.text1:
                showOrHideMySurfaceView();
                break;
            case R.id.text2://goto
                mIActionCallback.gotoSettingActivity(v);
                break;
            case R.id.text3:
                mDrawView.setCanvasCode(DrawActivity.CANVAS_UNDO);
                mDrawView.invalidate();
                break;
            case R.id.text4:
                mDrawView.setCanvasCode(DrawActivity.CANVAS_RESET);
                mDrawView.invalidate();
                dataReset();
                break;
            case R.id.text5:
                mIActionCallback.saveBitmap(mDrawView);

                break;
            case R.id.text6:
                mIActionCallback.creatNewLine();
                break;

        }
    }

    private void dataReset() {
        CommandUtils.getInstance().getRedoCommandList().clear();
        CommandUtils.getInstance().getUndoCommandList().clear();
    }

    /**
     *  使用Viewstub的在不需要弹出键盘的时候，渲染不占内存不
     */
    private void showOrHideMySurfaceView() {
        if (mViewStub.getParent() != null) {
            mViewStub.inflate();
        }
        if (mDrawView == null) {
            setDrawViewConfig();
        }
        if (mDrawView.getVisibility() == GONE) {
            mDrawView.setVisibility(VISIBLE);
        } else {
            mDrawView.setVisibility(GONE);
        }


    }

    public IActionCallback mIActionCallback;

    public void setgoToAcitivity(IActionCallback a) {
        mIActionCallback = a;
    }

    public void clearScreen() {
        mDrawView.setCanvasCode(DrawActivity.CANVAS_RESET);
        mDrawView.invalidate();
        dataReset();
    }

    public void onResume() {
        if (mDrawView == null) {
            return;
        }
        mDrawView.onResume();
    }

    public DrawPenView getSaveBitmap() {
        return mDrawView;
    }

    public interface IActionCallback {

        void gotoSettingActivity(View view);

        void saveBitmap(DrawPenView view);

        void creatNewLine();

        void getUptime(long l);

        void stopTime();
    }
}
