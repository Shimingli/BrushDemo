package com.brush.opengldemo.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.Log;
import android.widget.ScrollView;

import com.brush.opengldemo.BitmapUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

/**
 * 文件操作类
 * Created by Administrator on 2017/8/10.
 */

public class FileUtils {

    /**
     * 保存bitmap到SD卡
     */
    public static void saveBitmapSd(Bitmap bitmap, String picName){
        try {
            File fil = new File("/sdcard/niannian/002");
            if (!fil.exists()) {
                fil.mkdirs();
            }
            File file = new File("/sdcard/niannian/002",picName + ".png");
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
            BitmapUtils.recycled(bitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /***
     * 读取SD卡bitmap
     */
    public static Bitmap readBitmapSd(String pathname){
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(pathname);
            return BitmapFactory.decodeStream(fis);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
       return null;
    }

    /**
     * 截取scrollview的屏幕
     * **/
    public static Bitmap getScrollViewBitmap(ScrollView scrollView, String picpath) {
        int h = 0;
        Bitmap bitmap;
        // 获取recyclerview实际高度
        for (int i = 0; i < scrollView.getChildCount(); i++) {
            h += scrollView.getChildAt(i).getHeight();
        }
        // 创建对应大小的bitmap
        bitmap = Bitmap.createBitmap(scrollView.getWidth(), h,
                Bitmap.Config.ARGB_4444);
        final Canvas canvas = new Canvas(bitmap);
        scrollView.draw(canvas);
        // 测试输出
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(picpath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            if (null != out) {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                out.flush();
                out.close();
            }
        } catch (IOException e) {
        }
        return bitmap;
    }


    /***
     * 保存文件到SD
     * @param txt
     * @param name
     */
    public static void saveTextSd(String txt, String name){
        try
        {
            String fullNmae = "/sdcard/niannian/002/"+name+".txt";
            File file = new File(fullNmae);
            if(file.exists()){
                file.delete();
            }
            FileOutputStream outStream = new FileOutputStream(fullNmae,true);
            OutputStreamWriter writer = new OutputStreamWriter(outStream,"utf-8");
            writer.write(txt);
            writer.flush();
            writer.close();//记得关闭

            outStream.close();
        }
        catch (Exception e)
        {
            Log.e("m", "file write error");
        }
    }

    /**
     * 二进制读取文本办法
     * @return
     */
    public static String readTextSd() {
        String str = "";
        try {
            File urlFile = new File("/sdcard/niannian/002/content.txt");
            InputStreamReader isr = new InputStreamReader(new FileInputStream(urlFile), "UTF-8");
            BufferedReader br = new BufferedReader(isr);
            String mimeTypeLine = null;
            while ((mimeTypeLine = br.readLine()) != null) {
                str = str + mimeTypeLine;
            }
            Log.e("ss",str);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return str;
    }

    /**
     * 读取文本内容，能读取空格，换行内容
     * @return
     */
    public static String readTextWrapSd() {
        String result = null;
        try {
            File file = new File("/sdcard/niannian/002/content.txt");
            if (!file.exists()) {
                return null;
            }
            FileInputStream inputStream = new FileInputStream(file);
            byte[] b = new byte[inputStream.available()];
            inputStream.read(b);
            result = new String(b);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
