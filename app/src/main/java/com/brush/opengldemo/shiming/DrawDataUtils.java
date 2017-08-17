package com.brush.opengldemo.shiming;

import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.Log;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by lichaojian on 16-8-28.
 */
public class DrawDataUtils {

    private static final String TAG = "DrawDataUtils";
    private static DrawDataUtils mDrawDataUtils = null;
    private List<PathDrawData> mSaveDrawDataList = null;
    private List<PathDrawData> mShearDrawDataList = null;

    private DrawDataUtils() {
        mSaveDrawDataList = new ArrayList<>();
        mShearDrawDataList = new ArrayList<>();
    }

    public static DrawDataUtils getInstance() {
        if (mDrawDataUtils == null) {
            mDrawDataUtils = new DrawDataUtils();
        }
        return mDrawDataUtils;
    }

    public List<PathDrawData> getShearDrawDataList() {
        return mShearDrawDataList;
    }

    public List<PathDrawData> getSaveDrawDataList() {
        return mSaveDrawDataList;
    }

    /**
     * 将数据从数据区转移到裁剪区
     */
    public void drawDataFromSaveToShear() {
        Iterator<PathDrawData> iterator = DrawDataUtils.getInstance().getSaveDrawDataList().iterator();
        while (iterator.hasNext()) {
            PathDrawData baseDrawData = iterator.next();
            if (baseDrawData.isInShear()) {
                DrawDataUtils.getInstance().getShearDrawDataList().add(baseDrawData);
                iterator.remove();
            }
        }

    }

    /**
     * 将数据从裁剪区转移到数据区
     */
    public void drawDataFromShearToSave() {
        Iterator<PathDrawData> iterator = DrawDataUtils.getInstance().getShearDrawDataList().iterator();
        while (iterator.hasNext()) {
            PathDrawData baseDrawData = iterator.next();
                DrawDataUtils.getInstance().getSaveDrawDataList().add(baseDrawData);
            iterator.remove();
        }
    }

    /**
     * 对圈选的数据进行缩放
     *
     * @param iterator 需要进行缩放的数据集
     * @param scaleX   缩放的X
     * @param scaleY   缩放的Y
     * @param matrix
     */
    public void scaleDrawData(Iterator<PathDrawData> iterator, float scaleX, float scaleY, Matrix matrix) {
        while (iterator.hasNext()) {
            PathDrawData baseDrawData = iterator.next();
            switch (baseDrawData.getMode()) {
                case TestActivity.MODE_PATH:
                    PathDrawData pathDrawData = (PathDrawData) baseDrawData;
                    pathDrawData.getPath().transform(matrix);
                    break;
                default:
                    Log.e(TAG, "offSetDrawData" + baseDrawData.getMode());
                    break;
            }
        }
    }


    /**
     * 平移数据
     *
     * @param iterator 需要平移的数据源
     * @param offSetX  平移的横坐标X
     * @param offSetY  平移的纵坐标Y
     */
    public void offSetDrawData(Iterator<PathDrawData> iterator, float offSetX, float offSetY) {
        while (iterator.hasNext()) {
            PathDrawData baseDrawData = iterator.next();
            baseDrawData.setOffSetX(offSetX);
            baseDrawData.setOffSetY(offSetY);

            PathDrawData pathDrawData = (PathDrawData) baseDrawData;
            pathDrawData.getPath().offset(offSetX, offSetY);
            break;


        }
    }

    /**
     * 从Shear的数据里转移到命令数据集
     */
    public void drawDataFromShearToCommand() {
        Iterator<PathDrawData> iterator = mShearDrawDataList.iterator();
        Command command = new Command();
//        command.setCommand(TestActivity.COMMAND_TRANSLATE);
        while (iterator.hasNext()) {
            PathDrawData baseDrawData = iterator.next();
            command.setOffSetX(baseDrawData.getOffSetX());
            command.setOffSetY(baseDrawData.getOffSetY());
            switch (baseDrawData.getMode()) {
                case TestActivity.MODE_PATH:
                    command.getCommandDrawList().add(baseDrawData);
                    break;
                default:
                    break;
            }
        }
        CommandUtils.getInstance().getUndoCommandList().add(command);

    }

    /**
     * 获取需要生成的XML数据数组
     *
     * @return 点，画笔，模式
     */
    public String[] getXMLData() {
        String[] xmlData = {"", "", ""};
        Iterator<PathDrawData> iterator = mSaveDrawDataList.iterator();
        while (iterator.hasNext()) {
            PathDrawData baseDrawData = iterator.next();
            xmlData[1] += baseDrawData.getPaint().getStrokeWidth() + "," + baseDrawData.getPaint().getColor() + "]";
            xmlData[2] += baseDrawData.getMode() + "]";
            switch (baseDrawData.getMode()) {
                case TestActivity.MODE_PATH:
                    PathDrawData pathDrawData = (PathDrawData) baseDrawData;
                    xmlData[0] += pathDrawData.getPoint();

                    break;

                default:
                    break;
            }
        }
        return xmlData;
    }

    /**
     * 二次编辑XML数据
     *
     * @param xmlPath
     */
    public void structureReReadXMLData(String xmlPath) {
        XMLUtils mXmlUtils = new XMLUtils();
        mXmlUtils.decodeXML(xmlPath);
        String[] paint = mXmlUtils.getPaint().split("]");
        String[] point = mXmlUtils.getPoint().split("]");
        String[] mode = mXmlUtils.getMode().split("]");
        for (int i = 0; i < point.length; ++i) {
            PathDrawData baseDrawData = null;
            String[] pointXY = point[i].split("\\|");
            Path path = new Path();
            for (int j = 0; j < pointXY.length; ++j) {
                String[] dian = pointXY[j].split(",");
                int modeStyle = Integer.parseInt(mode[i]);

                switch (modeStyle) {
                    case TestActivity.MODE_PATH:
                        if (j == 0) {
                            baseDrawData = new PathDrawData();
                            ((PathDrawData) baseDrawData).setPoint(point[i] + "]");
                            path.moveTo(Float.parseFloat(dian[0]), Float.parseFloat(dian[1]));
                        } else {
                            path.lineTo(Float.parseFloat(dian[0]), Float.parseFloat(dian[1]));
                            ((PathDrawData) baseDrawData).setPath(path);
                        }
                        break;

                    default:
                        Log.e("modeStyle", modeStyle + "");
                        break;
                }
            }
            Paint paint1 = new Paint();
            paint1.setColor(Integer.parseInt(paint[i].split(",")[1]));
            paint1.setStrokeWidth(Float.parseFloat(paint[i].split(",")[0]));
            paint1.setStyle(Paint.Style.STROKE);
            baseDrawData.setPaint(paint1);
            mSaveDrawDataList.add(baseDrawData);
        }
    }
}
