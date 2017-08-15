package com.brush.opengldemo.utils;

import android.content.res.Resources;
import android.util.DisplayMetrics;

/** Utility functions */
public class Utils {

    public static final float EPSILON = 0.00001f;

    public static final float BLACK_COLOR[] = {0f, 0f, 0f, 0.6f};
    public static final float BROWN_COLOR[] = {0.26f, 0.18f, 0.14f, 0.85f};

    public static float[] getColorWithAlpha(float[] color, float alpha) {
        float[] copy = color.clone();
        copy[3] = alpha;
        return copy;
    }

    /** Returns the value of val, clamped between min and max */
    public static float clamp (float val, float min, float max) {
        return Math.max(min, Math.min(val, max));
    }

    /** Normalizes a value from [min, max] to [0, 1] */
    public static float normalize(float val, float min, float max) {

        if(Math.abs(max - min) < 0.01f) {
            return 0.5f;
        }

        float clampedVal = clamp(val, min, max);

        float scale = 1f / (max - min);

        return (clampedVal - min) * scale;
    }

    /** Returns whether two floats are equivalent to each other within a treshold */
    public static boolean floatsAreEquivalent(float val1, float val2) {
        float epsilon = 0.001f;

        return Math.abs(val1 - val2) < epsilon;
    }

    /**
     * Wrapper for replacing data in a float array, taken from another array from index zero.
     */
    public static void replaceInFloatArray(float[] floatArray, int floatArrayIndex, float [] contentArray) {
        replaceInFloatArray(floatArray, floatArrayIndex, contentArray, 0);
    }

    /**
     * Takes a float array, and a content array. Replaces the data in the float array from index
     * floatArrayIndex with the data from content array from contentArrayIndex.
     *
     * @param floatArray the array which data is replaced in
     * @param floatArrayIndex the index which data should start to be replaced from
     * @param contentArray the array which contains the content place in float array
     * @param contentArrayIndex the index which content begins from
     */
    public static void replaceInFloatArray(float[] floatArray, int floatArrayIndex, float [] contentArray,
                                           int contentArrayIndex) {

        if(floatArrayIndex < 0 || contentArrayIndex < 0 || floatArrayIndex >= floatArray.length ||
                contentArrayIndex >= contentArray.length) {
            System.err.println("Invalid input indexes");
            throw new ArrayIndexOutOfBoundsException();
        }

        int contentArrayReplaceLength = contentArray.length - contentArrayIndex;

        if(floatArrayIndex + contentArrayReplaceLength > floatArray.length) {
            System.err.println("Float array cannot fit content");
            throw new ArrayIndexOutOfBoundsException();
        }

        for(int i = 0; floatArrayIndex + i < floatArray.length; i++) {
            floatArray[floatArrayIndex + i] = contentArray[contentArrayIndex + i];
        }
    }

    /** Returns a throttled value that is a given percent from previousvalue towards targetValue */
    public static float getThrottledValue(float previousValue, float targetValue) {
        return getThrottledValue(previousValue, targetValue, 0.02f);
    }

    public static float getThrottledValue(float previousValue, float targetValue, float scale) {

        float difference = targetValue - previousValue;

        return previousValue + difference * scale;
    }

    /**
     * Returns whether the device is a tablet.
     *
     * We consider any device equal to or over 7 inches to be a tablet.
     */
    public static boolean isTablet() {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();

        int widthPixels = metrics.widthPixels;
        int heightPixels = metrics.heightPixels;

        float widthDpi = metrics.xdpi;
        float heightDpi = metrics.ydpi;

        float widthInches = widthPixels / widthDpi;
        float heightInches = heightPixels / heightDpi;

        double diagonalInches = Math.sqrt((widthInches * widthInches)
                        + (heightInches * heightInches));

        return diagonalInches >= (6.8f);
    }

    /**
     * This method converts device specific pixels to density independent pixels.
     *
     * @param px A value in px (pixels) unit. Which we need to convert into db
     * @return A float value to represent dp equivalent to px value
     */
    public static float convertPixelsToDp(float px){
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        float dp = px / (metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return dp;
    }

    /**
     * This method converts dp unit to equivalent pixels, depending on device density.
     *
     * @param dp A value in dp (density independent pixels) unit. Which we need to convert into pixels
     * @return A float value to represent px equivalent to dp depending on device density
     */
    public static float convertDpToPixel(float dp){
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        float px = dp * ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return px;
    }

    public static void printMatrix(float[] m) {

        for(int i = 0; i < m.length; i += 4) {
            System.out.println("{" + m[i + 0] + ", " + m[i + 1] + ", " + m[i + 2] + ", " + m[i + 3] + "}");
        }
    }

    /**
     * Converts a pixelbuffer of ARGB format to ABGR format.
     *
     * This must be done because OpenGL stores colors in RGB format while BMP is in BGR format.
     * If this conversion is not done, red and blue colors will be switched.
     */
    /**
     * 将pixelbuffer植入骨的ARGB格式格式。
      这是必须要做的因为OpenGL店颜色RGB格式BMP格式是BGR。
      如果没有转换，红色和蓝色的颜色将被切换。
     RGB即是代表红、绿、蓝三个通道的颜色

     R代表红，red; G代表绿，green; B代表蓝，blue。
     RGB模式就是，色彩数据模式，R在高位，G在中间，B在低位。BGR正好相反。
     * @param pixelBuffer
     */
    public static void convertRGBtoBGR(int[] pixelBuffer) {

        int bits;
        int tempRed;
        int tempBlue;

        int firstByteMask = 0x00ff0000;
        int secondByteMask = 0x000000ff;
        int bitDistance = 16;

        for (int i = 0; i < pixelBuffer.length; i++) {
            bits = pixelBuffer[i];

            // Clear first and third byte
            pixelBuffer[i] &= ~firstByteMask;
            pixelBuffer[i] &= ~secondByteMask;

            // Isolate the color bytes, and switch position
            tempRed = bits & firstByteMask;
            tempRed >>= bitDistance;

            tempBlue = bits & secondByteMask;
            tempBlue <<= bitDistance;

            // Place color into pixel
            pixelBuffer[i] |= tempRed;
            pixelBuffer[i] |= tempBlue;
        }
    }
}
