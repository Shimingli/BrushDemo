package com.brush.opengldemo.Base;

import android.app.Application;

/**
 * Created by Administrator on 2017/8/18.
 */

public class BaseApplication extends Application {
    private static BaseApplication application;

    @Override
    public void onCreate()
    {
        super.onCreate();
        application = this;
    }

    /**
     * 取得Application单件
     *
     * @return
     */
    public static BaseApplication getInstance()
    {
        return application;
    }
}
