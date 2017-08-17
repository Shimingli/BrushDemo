package com.brush.opengldemo.shiming;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.support.annotation.ColorInt;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.brush.opengldemo.settings.SettingsData;
import com.brush.opengldemo.settings.SettingsManager;
import com.brush.opengldemo.utils.FileUtils;

import java.util.Iterator;
import java.util.List;

/**
 * Created by shiming on 2017/8/16.
 */

public class DrawView extends View implements DrawViewInterface {
    private static final String TAG = "DrawView";
    private Paint mPaint;//画笔
    private Canvas mCanvas;//画布
    private Bitmap mBitmap;
    private int mCanvasCode = 0;
    private PathDrawData mBaseDrawData = null;
    private PathState mCurrentState = PathState.getInstance();//当前绘制的状态
    private SettingsManager settingsManager;
    private SettingsData settingsData;
    private int mArgb;

    public DrawView(Context context) {
        super(context);
        initParameter(context);
    }

    public DrawView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initParameter(context);
    }

    public DrawView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initParameter(context);
    }

    public void setCanvasCode(int mCanvasCode) {
        this.mCanvasCode = mCanvasCode;
    }

    private void initParameter(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
        mBitmap = Bitmap.createBitmap(dm.widthPixels, dm.heightPixels, Bitmap.Config.ARGB_8888);
        initPaint();
        initCanvas();

        settingsManager = SettingsManager.getInstance(context);
        settingsData = settingsManager.getSettingsData().clone();
        int size = settingsData.getPaintWidht();
        String s = convertRGBToHex(settingsData.getColorWrapper().getRed(), settingsData.getColorWrapper().getGreen(), settingsData.getColorWrapper().getBlue());
        changePaintColor(Color.parseColor(s));
        changePaintSize(size);
    }
    //**将rgb色彩值转成16进制代码**
    public  String convertRGBToHex(int r, int g, int b) {
        String rFString, rSString, gFString, gSString,
                bFString, bSString, result;
        int red, green, blue;
        int rred, rgreen, rblue;
        red = r / 16;
        rred = r % 16;
        if (red == 10) rFString = "A";
        else if (red == 11) rFString = "B";
        else if (red == 12) rFString = "C";
        else if (red == 13) rFString = "D";
        else if (red == 14) rFString = "E";
        else if (red == 15) rFString = "F";
        else rFString = String.valueOf(red);

        if (rred == 10) rSString = "A";
        else if (rred == 11) rSString = "B";
        else if (rred == 12) rSString = "C";
        else if (rred == 13) rSString = "D";
        else if (rred == 14) rSString = "E";
        else if (rred == 15) rSString = "F";
        else rSString = String.valueOf(rred);

        rFString = rFString + rSString;

        green = g / 16;
        rgreen = g % 16;

        if (green == 10) gFString = "A";
        else if (green == 11) gFString = "B";
        else if (green == 12) gFString = "C";
        else if (green == 13) gFString = "D";
        else if (green == 14) gFString = "E";
        else if (green == 15) gFString = "F";
        else gFString = String.valueOf(green);

        if (rgreen == 10) gSString = "A";
        else if (rgreen == 11) gSString = "B";
        else if (rgreen == 12) gSString = "C";
        else if (rgreen == 13) gSString = "D";
        else if (rgreen == 14) gSString = "E";
        else if (rgreen == 15) gSString = "F";
        else gSString = String.valueOf(rgreen);

        gFString = gFString + gSString;

        blue = b / 16;
        rblue = b % 16;

        if (blue == 10) bFString = "A";
        else if (blue == 11) bFString = "B";
        else if (blue == 12) bFString = "C";
        else if (blue == 13) bFString = "D";
        else if (blue == 14) bFString = "E";
        else if (blue == 15) bFString = "F";
        else bFString = String.valueOf(blue);

        if (rblue == 10) bSString = "A";
        else if (rblue == 11) bSString = "B";
        else if (rblue == 12) bSString = "C";
        else if (rblue == 13) bSString = "D";
        else if (rblue == 14) bSString = "E";
        else if (rblue == 15) bSString = "F";
        else bSString = String.valueOf(rblue);
        bFString = bFString + bSString;
        result = "#" + rFString + gFString + bFString;
        return result;

    }
    private void initPaint() {
        mPaint = new Paint();
        mPaint.setColor(Color.parseColor("#FF4081"));
        mPaint.setStrokeWidth(10);
        mPaint.setStyle(Paint.Style.STROKE);
    }

    private void initCanvas() {
        mCanvas = new Canvas(mBitmap);
        //设置画布的颜色的问题
        mCanvas.drawColor(Color.TRANSPARENT);
    }

    public void changePaintColor(int color) {
        mPaint.setColor(color);
    }

    public void changePaintSize(float width) {
        mPaint.setStrokeWidth(width);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(mBitmap, 0, 0, mPaint);
        switch (mCanvasCode) {
            case TestActivity.CANVAS_NORMAL:
                mCurrentState.onDraw(mBaseDrawData, canvas);
                break;
            case TestActivity.CANVAS_UNDO:
                undo();
                break;
            case TestActivity.CANVAS_REDO:
                redo();
                break;
            case TestActivity.CANVAS_RESET:
                reset();
                break;
            default:
                Log.e(TAG, "onDraw" + Integer.toString(mCanvasCode));
                break;
        }
        super.onDraw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mCanvasCode = TestActivity.CANVAS_NORMAL;

        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                actionDown(event);
                if (mBaseDrawData == null && !mCurrentState.isShear()) {
                    reset();
                    drawFromData();
                    return super.onTouchEvent(event);
                }
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                mCurrentState.pointerDownDrawData(event);
                break;
            case MotionEvent.ACTION_MOVE:
                actionMove(event);
                break;
            case MotionEvent.ACTION_UP:
                actionUp(event);
                break;
            case MotionEvent.ACTION_POINTER_UP:
                mCurrentState.pointerUpDrawData(event);
                break;
            default:
                break;
        }
        invalidate();
        return true;
    }


    private void actionDown(MotionEvent event) {
        mBaseDrawData = mCurrentState.downDrawData(event, mPaint);
    }

    private void actionMove(MotionEvent event) {
        mCurrentState.moveDrawData(event, mPaint, mCanvas);
    }

    private void actionUp(MotionEvent event) {
        mCurrentState.upDrawData(event, mPaint);
        mCurrentState.onDraw(mBaseDrawData, mCanvas);
    }

    @Override
    public void redo() {
        redoFromCommand(CommandUtils.getInstance().redo());
    }

    @Override
    public void undo() {
        reset();
        undoFromCommand(CommandUtils.getInstance().undo());
    }

    @Override
    public void reset() {
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        mCanvas.drawPaint(mPaint);
        mPaint.setXfermode(null);
    }

    @Override
    public String save(String path, String rootPath) {
        // TODO: 2016/8/8 从数据源里面获取相应的参数，保存为XML,PNG,SVG
        String pngFileName = null;
        pngFileName = FileUtils.saveAsPng(mBitmap, path, rootPath);
        //        XMLUtils xmlUtils = new XMLUtils();
        //        String[] xmlData = DrawDataUtils.getInstance().getXMLData();
        //        xmlUtils.encodeXML(rootPath + "/" + pngFileName + ".xml", xmlData[0], xmlData[1], xmlData[2]);
        //        SVGUtils svgUtils = new SVGUtils();
        //        svgUtils.encodeSVG(DrawDataUtils.getInstance().getSaveDrawDataList(), rootPath + "/" + pngFileName + ".svg");
        return pngFileName;
    }

    public Bitmap saveBitmap(String path, String rootPath) {
        // TODO: 2016/8/8 从数据源里面获取相应的参数，保存为XML,PNG,SVG
        String pngFileName = null;
        //        pngFileName = FileUtils.saveAsPng(mBitmap, path, rootPath);
        //        XMLUtils xmlUtils = new XMLUtils();
        //        String[] xmlData = DrawDataUtils.getInstance().getXMLData();
        //        xmlUtils.encodeXML(rootPath + "/" + pngFileName + ".xml", xmlData[0], xmlData[1], xmlData[2]);
        //        SVGUtils svgUtils = new SVGUtils();
        //        svgUtils.encodeSVG(DrawDataUtils.getInstance().getSaveDrawDataList(), rootPath + "/" + pngFileName + ".svg");
        return mBitmap;
    }

    public void setCurrentState(PathState currentState) {
        mCurrentState = currentState;
    }





    public void setBackColor(@ColorInt int backColor) {
        mBackColor = backColor;
    }

    private int mBackColor = Color.TRANSPARENT;

    /**
     * 逐行扫描 清楚边界空白。
     *
     *
     * @param blank 边距留多少个像素
     * @return
     */


    public Bitmap clearBlank(int blank) {
        if (mBitmap != null) {
            int HEIGHT = mBitmap.getHeight();
            int WIDTH = mBitmap.getWidth();
            int top = 0, left = 0, right = 0, bottom = 0;
            int[] pixs = new int[WIDTH];
            boolean isStop;
            for (int y = 0; y < HEIGHT; y++) {
                mBitmap.getPixels(pixs, 0, WIDTH, 0, y, WIDTH, 1);
                isStop = false;
                for (int pix : pixs) {
                    if (pix != mBackColor) {

                        top = y;
                        isStop = true;
                        break;
                    }
                }
                if (isStop) {
                    break;
                }
            }
            for (int y = HEIGHT - 1; y >= 0; y--) {
                mBitmap.getPixels(pixs, 0, WIDTH, 0, y, WIDTH, 1);
                isStop = false;
                for (int pix : pixs) {
                    if (pix != mBackColor) {
                        bottom = y;
                        isStop = true;
                        break;
                    }
                }
                if (isStop) {
                    break;
                }
            }
            pixs = new int[HEIGHT];
            for (int x = 0; x < WIDTH; x++) {
                mBitmap.getPixels(pixs, 0, 1, x, 0, 1, HEIGHT);
                isStop = false;
                for (int pix : pixs) {
                    if (pix != mBackColor) {
                        left = x;
                        isStop = true;
                        break;
                    }
                }
                if (isStop) {
                    break;
                }
            }
            for (int x = WIDTH - 1; x > 0; x--) {
                mBitmap.getPixels(pixs, 0, 1, x, 0, 1, HEIGHT);
                isStop = false;
                for (int pix : pixs) {
                    if (pix != mBackColor) {
                        right = x;
                        isStop = true;
                        break;
                    }
                }
                if (isStop) {
                    break;
                }
            }
            if (blank < 0) {
                blank = 0;
            }
            left = left - blank > 0 ? left - blank : 0;
            top = top - blank > 0 ? top - blank : 0;
            right = right + blank > WIDTH - 1 ? WIDTH - 1 : right + blank;
            bottom = bottom + blank > HEIGHT - 1 ? HEIGHT - 1 : bottom + blank;
            return Bitmap.createBitmap(mBitmap, left, top, right - left, bottom - top);
        } else {
            return null;
        }
    }

    private void undoFromCommand(Command command) {

        if (command != null) {
            switch (command.getCommand()) {
                case TestActivity.COMMAND_ADD:
                    Iterator<PathDrawData> iterator = DrawDataUtils.getInstance().getSaveDrawDataList().iterator();
                    while (iterator.hasNext()) {
                        PathDrawData baseDrawData = iterator.next();
                        if (baseDrawData.equals(command.getCommandDrawList().get(0))) {
                            iterator.remove();
                            break;
                        }
                    }
                    drawFromData();
                    break;

                default:
                    Log.e(TAG, "undoFromCommand" + Integer.toString(command.getCommand()));
                    break;
            }
        }
    }


    private void redoFromCommand(Command command) {
        if (command != null) {
            switch (command.getCommand()) {
                case TestActivity.COMMAND_ADD:
                    drawFromRedoData(command.getCommandDrawList());
                    break;
                default:
                    Log.e(TAG, "redoFromCommand" + Integer.toString(command.getCommand()));
                    break;
            }
        }
    }

    /**
     * 从redo里面恢复数据
     */
    private void drawFromRedoData(List<PathDrawData> redoList) {
        int size = redoList.size();
        for (int i = 0; i < size; ++i) {
            PathDrawData baseDrawData = redoList.get(i);
            baseDrawData.onDraw(mCanvas);
            //            DrawDataUtils.getInstance().getSaveDrawDataList().add(baseDrawData);
        }
    }

    public void drawFromData() {
        int size = DrawDataUtils.getInstance().getSaveDrawDataList().size();
        for (int i = 0; i < size; ++i) {
            PathDrawData baseDrawData = DrawDataUtils.getInstance().getSaveDrawDataList().get(i);
            baseDrawData.onDraw(mCanvas);
        }
    }

    public void addCommand() {
        Iterator<PathDrawData> iterator = DrawDataUtils.getInstance().getSaveDrawDataList().iterator();
        Command command;
        while (iterator.hasNext()) {
            PathDrawData baseDrawData = iterator.next();
            command = new Command();
            command.setCommand(TestActivity.COMMAND_ADD);
            command.getCommandDrawList().add(baseDrawData);
            CommandUtils.getInstance().getUndoCommandList().add(command);
        }
    }

    public void onResume() {
        boolean b = settingsManager.hasChanges();
        if (b){
            System.out.println("shiming");
            String s = convertRGBToHex(settingsData.getColorWrapper().getRed(), settingsData.getColorWrapper().getGreen(), settingsData.getColorWrapper().getBlue());
            changePaintColor(Color.parseColor(s));
            settingsManager.setChangesRead();
        }
    }
}
