package com.brush.opengldemo.settings;


import com.brush.opengldemo.utils.ColorWrapper;

/**
 * 笔的数量
 */
public class SettingsData {

    private float size;//笔的宽度   笔的宽度
    private int numBristles;//毛笔的个数  笔尖的个数点多少  越多越比较弄的颜色
    private float pressureFactor; //压力与深度的因素  按压的时间越长的画，会越来越弄的颜色
    private float bristleThickness;//墨水的多少  0.01   1.0   墨水越大   越弄

    private boolean isDry;
    private float opacity;
    private ColorWrapper colorWrapper;//颜色的透明度和rgb的值

    private boolean showBrushView;
    
    /**
     * {
     "bristleThickness": 0.17,
     "colorWrapper": {
     "alpha": 196,
     "blue": 69,
     "green": 39,
     "red": 242,
     "temp": [
     0.9490197,
     0.15294118,
     0.27058825,
     0.7686275
     ]
     },
     "isDry": true,
     "numBristles": 230,
     "opacity": 0.8,
     "pressureFactor": 0.13,
     "showBrushView": false,
     "size": 0.13
     }
     */
    public SettingsData(){
        size = 0.13f; //0.5
        numBristles = 230;
        pressureFactor = 0.13f;
        bristleThickness = 0.17f;

        isDry = true;
        opacity = 0.8f;
        colorWrapper = new ColorWrapper(196, 69, 39, 150);

        showBrushView = true;
    }

    public SettingsData clone() {
        SettingsData sd = new SettingsData();

        sd.setSize(size);
        sd.setNumBristles(numBristles);
        sd.setPressureFactor(pressureFactor);
        sd.setBristleThickness(bristleThickness);

        sd.setIsDry(isDry);
        sd.setOpacity(opacity);
        sd.setColorWrapper(colorWrapper);

        sd.setShowBrushView(showBrushView);

        return sd;
    }

    public float getSize() {
        return size;
    }

    public void setSize(float size) {
        this.size = size;
    }

    public int getNumBristles() {
        return numBristles;
    }

    public void setNumBristles(int numBristles) {
        this.numBristles = numBristles;
    }

    public float getPressureFactor() {
        return pressureFactor;
    }

    public void setPressureFactor(float pressureFactor) {
        this.pressureFactor = pressureFactor;
    }

    public float getBristleThickness() {
        return bristleThickness;
    }

    public void setBristleThickness(float bristleThickness) {
        this.bristleThickness = bristleThickness;
    }

    public boolean isDry() {
        return isDry;
    }

    public void setIsDry(boolean isDry) {
        this.isDry = isDry;
    }

    public float getOpacity() {
        return opacity;
    }

    public void setOpacity(float opacity) {
        this.opacity = opacity;
    }

    public ColorWrapper getColorWrapper() {
        return colorWrapper;
    }

    public void setColorWrapper(ColorWrapper colorWrapper) {
        this.colorWrapper = colorWrapper;
    }

    public boolean isShowBrushView() {
        return showBrushView;
    }

    public void setShowBrushView(boolean showBrushView) {
        this.showBrushView = showBrushView;
    }
}
