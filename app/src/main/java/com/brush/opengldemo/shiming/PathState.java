package com.brush.opengldemo.shiming;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.MotionEvent;

/**
 * Created by shiming on 2017/8/16.
 */

public class PathState  {
    private static PathState mPathState = null;
    private PathDrawData mPathDrawData;

    private PathState() {
    }

    public static PathState getInstance() {
        if (mPathState == null) {
            mPathState = new PathState();
        }
        return mPathState;
    }


    public void onDraw(PathDrawData baseDrawData, Canvas canvas) {
        if (baseDrawData != null) {
            PathDrawData pathDrawData = baseDrawData;
            canvas.drawPath(pathDrawData.getPath(), pathDrawData.getPaint());
        }
    }


    public PathDrawData downDrawData(MotionEvent event, Paint paint) {
        Command command = new Command();
        mPathDrawData = new PathDrawData();
        Paint pathPaint = new Paint(paint);
        Path path = new Path();
        path.reset();
        path.moveTo(event.getX(), event.getY());
        mPathDrawData.setPaint(pathPaint);
        mPathDrawData.setPath(path);
        mPathDrawData.setPoint(event.getX() + "," + event.getY() + "|");
        command.setCommand(DrawActivity.COMMAND_ADD);
        command.getCommandDrawList().add(mPathDrawData);
        CommandUtils.getInstance().getUndoCommandList().add(command);
        DrawDataUtils.getInstance().getSaveDrawDataList().add(mPathDrawData);
        return mPathDrawData;
    }


    public void moveDrawData(MotionEvent event, Paint paint, Canvas canvas) {
        mPathDrawData.setPoint(event.getX() + "," + event.getY() + "|");
        mPathDrawData.getPath().lineTo(event.getX(), event.getY());
    }


    public void upDrawData(MotionEvent event, Paint paint) {
        mPathDrawData.setPoint(event.getX() + "," + event.getY() + "]");
        mPathDrawData.getPath().lineTo(event.getX(), event.getY());
    }


    public void pointerDownDrawData(MotionEvent event) {

    }


    public void pointerUpDrawData(MotionEvent event) {

    }


    public void destroy() {
        mPathState = null;
    }

    public boolean isShear() {
        return false;
    }
}
