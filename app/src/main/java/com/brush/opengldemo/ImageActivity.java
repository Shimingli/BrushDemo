package com.brush.opengldemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.brush.opengldemo.config.AppConfig;
import com.brush.opengldemo.utils.FileUtils;

/**
 * 用来预览用
 */

public class ImageActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        ImageView mIv = (ImageView) findViewById(R.id.iv_image);
        mIv.setImageBitmap(FileUtils.readBitmapSd(AppConfig.PATH_SD+"scrollview.jpg"));
    }
}
