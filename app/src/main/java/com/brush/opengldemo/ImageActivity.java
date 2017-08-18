package com.brush.opengldemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.brush.opengldemo.config.AppConfig;
import com.brush.opengldemo.utils.FileUtils;
import com.brush.opengldemo.utils.NetworkUtil;

import java.io.File;

/**
 * 用来预览用
 */

public class ImageActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        ImageView mIv = (ImageView) findViewById(R.id.iv_image);
        mIv.setImageBitmap(FileUtils.readBitmapSd(AppConfig.PATH_SD + AppConfig.NAME_IMG_SCROLLVIEW));
        /**上传至七牛服务器*/
        findViewById(R.id.btn_image_upload).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                File file = new File(AppConfig.PATH_SD + AppConfig.NAME_IMG_SCROLLVIEW);
                NetworkUtil.qnFile(file, AppConfig.NAME_IMG_SCROLLVIEW);
            }
        });

    }
}
