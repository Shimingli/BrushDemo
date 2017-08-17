package com.brush.opengldemo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.View;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;

import com.brush.opengldemo.config.AppConfig;
import com.brush.opengldemo.utils.FileUtils;
import com.brush.opengldemo.utils.NetworkUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.brush.opengldemo.utils.SystemUtils.sendKeyCode;

public class Main2Activity extends AppCompatActivity {

    private MyGLSurfaceView mMyGLSurfaceView;
    private Button mClearButton;
    private Button mSaveButton;
    private Button mBitmapButton;
    private Button mBtnSave, mBtnRead,mBtnSavePreview,mBtnDel;

    private File mPhoto;
    private Bitmap mBitmap;
    private EditText mEText;
    private ImageView mImageView;
    private File mPathd1;
    private Bitmap mTestBitmap;
    private ScrollView mSv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        ViewStub viewStub = (ViewStub) findViewById(R.id.view_stub);
        viewStub.inflate();
        final Handler handler = new Handler();
        mMyGLSurfaceView = (MyGLSurfaceView) findViewById(R.id.signature_pad);
        mClearButton = (Button) findViewById(R.id.clear);
        mSaveButton = (Button) findViewById(R.id.save);
        mBitmapButton = (Button) findViewById(R.id.bitmap);
        mBtnSave = (Button) findViewById(R.id.btn_edit_save);
        mBtnRead = (Button) findViewById(R.id.btn_edit_read);
        mBtnSavePreview = (Button) findViewById(R.id.btn_edit_save_preview);
        mBtnDel = (Button) findViewById(R.id.btn_edit_del);
        mSv = (ScrollView) findViewById(R.id.sv_edit_content);
        mEText = (EditText) findViewById(R.id.modify_edit_text_view);
        mEText.setInputType(InputType.TYPE_NULL);
        mBitmapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMyGLSurfaceView.saveImageText(new MyGLSurfaceView.saveImageListener() {
                    @Override
                    public void saveSuccess(final Bitmap path, final File pathd) {
                        new Thread(){
                            public void run(){
                                mPathd1 = pathd;
                                mTestBitmap = path;
                                handler.post(runnableUi);
                            }
                        }.start();
                    }

                    @Override
                    public void saveFailurw() {

                    }
                });

            }
        });
        mClearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMyGLSurfaceView.clearScreen();
            }
        });
        mImageView = (ImageView) findViewById(R.id.iamgeview);
        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMyGLSurfaceView.saveImageText(new MyGLSurfaceView.saveImageListener() {
                    @Override
                    public void saveSuccess(final Bitmap path, final File pathd) {
                        new Thread(){
                            public void run(){
                                mPathd1 = pathd;
                                mTestBitmap = path;
                                handler.post(runnableUi);
                            }
                        }.start();
                    }
                    @Override
                    public void saveFailurw() {

                    }
                });

            }
        });
        mBtnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FileUtils.saveTextSd(mEText.getText().toString(), "content");
                mEText.setText("");
            }
        });
        mBtnRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txt = FileUtils.readTextWrapSd();
//                    mEText.setText(txt);

                if(TextUtils.isEmpty(txt))
                    return;
                SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(txt);
                Pattern pattern = Pattern.compile("\\[p](\\S+?)\\[/p]");//匹配[xx]的字符串
                Matcher matcher = pattern.matcher(txt);
                while (matcher.find()) {
                    int start = matcher.start();
                    int end = matcher.end();
                    String group = matcher.group();
                    group = group.substring(3, group.length() - 4);
                    FileInputStream fis = null;
                    Bitmap bitmap = FileUtils.readBitmapSd("/sdcard/niannian/002/" + group + ".png");
                    ImageSpan imageSpan = new ImageSpan(Main2Activity.this, bitmap);
                    spannableStringBuilder.setSpan(imageSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    BitmapUtils.recycled(bitmap);
                }
                mEText.setText(spannableStringBuilder);
                mEText.setSelection(spannableStringBuilder.length());
            }
        });

        mBtnSavePreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = "scrollview.jpg";
                FileUtils.getScrollViewBitmap(mSv, AppConfig.PATH_SD+name);
                File file = new File(AppConfig.PATH_SD+name);
                NetworkUtil.qnFile(file,name);
                startActivity(new Intent(Main2Activity.this,ImageActivity.class));
            }
        });
        mBtnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendKeyCode(67);
            }
        });
    }

    private static String full_name = "";
    private static final String LAST_NAME = "img_";
    private int first_name = 1;
    Runnable   runnableUi=new  Runnable(){
        @Override
        public void run() {
            final Bitmap bitmap = BitmapUtils.resizeImage(mTestBitmap, 100, 100);
            if (bitmap != null) {
                //根据Bitmap对象创建ImageSpan对象
                ImageSpan imageSpan = new ImageSpan(Main2Activity.this, bitmap);
                //创建一个SpannableString对象，以便插入用ImageSpan对象封装的图像
                full_name = LAST_NAME + first_name;
                String s = "[p]" + full_name + "[/p]";
                first_name++;
                SpannableString spannableString = new SpannableString(s);
                //  用ImageSpan对象替换face
                spannableString.setSpan(imageSpan, 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                //将选择的图片追加到EditText中光标所在位置
                int index = mEText.getSelectionStart(); //获取光标所在位置
                Editable edit_text = mEText.getEditableText();
                if (index < 0 || index >= edit_text.length()) {
                    edit_text.append(spannableString);
                } else {
                    edit_text.insert(index, spannableString);
                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        FileUtils.saveBitmapSd(bitmap, full_name);
                    }
                }).start();
            }

            mMyGLSurfaceView.clearScreen();
        }

    };
    public void saveBitmap(File path) {
        mBitmap = null;
        try {
            FileInputStream fis = new FileInputStream(path);
            Bitmap bitmap = BitmapFactory.decodeStream(fis);
            mBitmap = BitmapUtils.resizeImage(bitmap, 50, 50);
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (mBitmap != null) {
            //根据Bitmap对象创建ImageSpan对象
            ImageSpan imageSpan = new ImageSpan(this, mBitmap);
            //创建一个SpannableString对象，以便插入用ImageSpan对象封装的图像
            full_name = LAST_NAME + first_name;
            String s = "[" + full_name + "]";
            first_name++;
            SpannableString spannableString = new SpannableString(s);
            //  用ImageSpan对象替换face
            spannableString.setSpan(imageSpan, 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            //将选择的图片追加到EditText中光标所在位置
            int index = mEText.getSelectionStart(); //获取光标所在位置
            Editable edit_text = mEText.getEditableText();
            if (index < 0 || index >= edit_text.length()) {
                edit_text.append(spannableString);
            } else {
                edit_text.insert(index, spannableString);
            }
        }
    }

    public boolean addJpgSignatureToGallery(Bitmap signature) {
        boolean result = false;
        try {
            mPhoto = new File(getAlbumStorageDir("SignaturePad"), String.format("Signature_%d.jpg", System.currentTimeMillis()));
            saveBitmapToJPG(signature, mPhoto);
            result = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public void saveBitmapToJPG(Bitmap bitmap, File photo) throws IOException {
        Bitmap newBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(newBitmap);
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(bitmap, 0, 0, null);
        OutputStream stream = new FileOutputStream(photo);
        //图片的质量
        newBitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream);
        stream.close();
    }

    public File getAlbumStorageDir(String albumName) {
        // Get the directory for the user's public pictures directory.
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), albumName);
        if (!file.mkdirs()) {
            Log.e("SignaturePad", "Directory not created");
        }
        return file;
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMyGLSurfaceView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMyGLSurfaceView.onResume();
    }
}
