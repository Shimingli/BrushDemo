package com.brush.opengldemo.utils;

import android.widget.Toast;

import com.brush.opengldemo.Base.BaseApplication;

/**
 * Toast工具类
 * Created by Administrator on 2017/8/18.
 */

public class ToastUtil {
    private static Toast toast;

    /**
     * 显示字符串
     * @param content
     */
    public static void  showToast(String content){
        if(toast==null){
            toast=Toast.makeText(BaseApplication.getInstance(),content,Toast.LENGTH_SHORT);
        }else {
            toast.setText(content);
        }
        toast.show();
    }
}
