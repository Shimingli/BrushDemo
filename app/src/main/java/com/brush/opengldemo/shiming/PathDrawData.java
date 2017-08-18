package com.brush.opengldemo.shiming;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;

/**
 * Created by shiming on 2017/8/16.
 */

public class PathDrawData {
    private Path mPath;
    private String mPoint = "";
    protected Paint mPaint;
    private boolean isInShear = false;
    private float mOffSetX = 0;
    private float mOffSetY = 0;
    public PathDrawData() {
    }

    public Path getPath() {
        return mPath;
    }

    public void setPath(Path path) {
        mPath = path;
    }

    public String getPoint() {
        return mPoint;
    }

    public void setPoint(String point) {
        mPoint += point;
    }

    public Paint getPaint() {
        return mPaint;
    }

    public void setPaint(Paint paint) {
        mPaint = paint;
    }

    public float getOffSetX() {
        return mOffSetX;
    }

    public void setOffSetX(float offSetX) {
        mOffSetX += offSetX;
    }

    public float getOffSetY() {
        return mOffSetY;
    }

    public void setOffSetY(float offSetY) {
        mOffSetY += offSetY;
    }

    public boolean isInShear() {
        return isInShear;
    }

    public void setInShear(boolean inShear) {
        isInShear = inShear;
    }




    public void onDraw(Canvas canvas) {
        canvas.drawPath(mPath, mPaint);
    }

    public int getMode() {
        return 0;
    }
}
