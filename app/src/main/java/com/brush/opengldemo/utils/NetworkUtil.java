package com.brush.opengldemo.utils;

import android.util.Log;

import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UpProgressHandler;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.storage.UploadOptions;
import com.qiniu.api.auth.digest.Mac;
import com.qiniu.api.rs.PutPolicy;

import org.json.JSONObject;

import java.io.File;

/**
 *  网络通讯类工具
 */

public class NetworkUtil {

    /**
     * 上传文件到七牛
     * @param file   上传的文件对象
     * @param name    上传文件保存的文件名
     */
    public static void qnFile(File file, String name){
        String token = getToken();
        UploadManager uploadManager = new UploadManager();
        uploadManager.put(file, name, token, new UpCompletionHandler() {
            public void complete(String key, ResponseInfo info, JSONObject response) {
                if (info != null && info.isOK()) {
                    Log.e("TAG","上传成功" + info.statusCode);
                }else{
                    Log.e("TAG","上传失败 = " + info.statusCode + "___" + info);
                }
            }
        }, new UploadOptions(null, null, false, new UpProgressHandler() {
            public void progress(String key, double percent) {
            }
        }, null));
    }

    /**
     * 获取七牛token，本地生成
     *
     * @return
     */
    public static String getToken() {
        //默认测试网配置
        String QINIU_AK = "ISyTOW_3TBFRknK8CGUq3rUsqGOB_DL511OAIN4T";
        String QINIU_SK = "lINMIcEBPi_f49BdmwL2L24CovUB2_M28ag2LeCO";
        String QINIU_BUCKNAME = "niannian";
        Mac mac = new Mac(QINIU_AK, QINIU_SK);
        PutPolicy putPolicy = new PutPolicy(QINIU_BUCKNAME);
        putPolicy.returnBody = "{\"name\": $(fname),\"size\": \"$(fsize)\",\"w\": \"$(imageInfo.width)\",\"h\": \"$(imageInfo.height)\",\"key\":$(etag)}";
        try {
            String uptoken = putPolicy.token(mac);
            return uptoken;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
