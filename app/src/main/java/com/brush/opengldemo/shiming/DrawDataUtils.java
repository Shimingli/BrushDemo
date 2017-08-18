package com.brush.opengldemo.shiming;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


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





}
