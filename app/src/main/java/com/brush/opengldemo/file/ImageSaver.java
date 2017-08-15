package com.brush.opengldemo.file;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import com.brush.opengldemo.MyGLSurfaceView;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;



/**
 * Class which saves images to internal storage.
 */
public class ImageSaver {

    private static final String TAG = "FastBrush";

    /** Saves bitmap to external storage */
    public static void saveImageToStorage(Bitmap bitmapImage, Context context){

        System.err.println("Saving");

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyddMM-hh:mm:ss");
        String imageName = dateFormat.format(new Date());
//        String imageName = "shiming";

        MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmapImage, imageName, "FastBrush");

        //Create Path to save Image
        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + "/FastBrush"); //Creates app specific folder
        path.mkdirs();
        File imageFile = new File(path, imageName + ".png"); // Imagename.png

        try{
            FileOutputStream out = new FileOutputStream(imageFile);
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, out); // Compress Image
            out.flush();
            out.close();

            // Tell the media scanner about the new file so that it is
            // immediately available to the user.
            MediaScannerConnection.scanFile(context, new String[]{imageFile.getAbsolutePath()}, null, new MediaScannerConnection.OnScanCompletedListener() {
                public void onScanCompleted(String path, Uri uri) {
                    Log.i("ExternalStorage", "Scanned " + path + ":");
                    Log.i("ExternalStorage", "-> uri=" + uri);
                }
            });
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void saveImageToStorage(Bitmap bitmapImage, Context context, MyGLSurfaceView.saveImageListener listener) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyddMM-hh:mm:ss");
                String imageName = dateFormat.format(new Date());
//        String imageName = "shiming";
        // 解决Android拍照保存在系统相册不显示的问题,强制告诉手机，有新的图片进入静来了
        MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmapImage, imageName, "FastBrush");

        //Create Path to save Image
        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + "/FastBrush"); //Creates app specific folder
        path.mkdirs();
        File imageFile = new File(path, imageName + ".webp"); // I

        try{
            FileOutputStream out = new FileOutputStream(imageFile);
            bitmapImage.compress(Bitmap.CompressFormat.WEBP, 80, out); // Compress Image
            out.flush();
            out.close();

            // Tell the media scanner about the new file so that it is
            // immediately available to the user.
            MediaScannerConnection.scanFile(context, new String[]{imageFile.getAbsolutePath()}, null, new MediaScannerConnection.OnScanCompletedListener() {
                public void onScanCompleted(String path, Uri uri) {
                    Log.i("ExternalStorage", "Scanned " + path + ":");
                    Log.i("ExternalStorage", "-> uri=" + uri);
                }
            });
            listener.saveSuccess(bitmapImage,imageFile);
        } catch(Exception e) {
            e.printStackTrace();
            listener.saveFailurw();
        }
    }
}
