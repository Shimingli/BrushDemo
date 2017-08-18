package com.brush.opengldemo.shiming;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;

import com.brush.opengldemo.BitmapUtils;
import com.brush.opengldemo.ImageActivity;
import com.brush.opengldemo.R;
import com.brush.opengldemo.config.AppConfig;
import com.brush.opengldemo.utils.FileUtils;
import com.brush.opengldemo.utils.SystemUtils;
import com.brush.opengldemo.utils.ViewUtiles;

import java.io.FileInputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DrawActivity extends AppCompatActivity {
    public static final int MODE_PATH = 0;
    public static final int CANVAS_NORMAL = 0;
    public static final int CANVAS_UNDO = 1;//撤回画笔
    public static final int CANVAS_RESET = 3;//全部清除
    public static final int COMMAND_ADD = 0;
    private DrawViewLayout mDrawViewLayout;
    public static final String ROOT_DIRECTORY = Environment.getExternalStorageDirectory().toString() + "/note";
    public static final String TEMPORARY_PATH = ROOT_DIRECTORY + "/temporary";
    private Bitmap mBitmap;
    private EditText mEText;
    private Button mBtnLook, mBtnSave, mBtnRead, mBtnDel;
    private ScrollView mSv;
    private long mOldTime;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try {
                boolean obj = (boolean) msg.obj;
                if (obj) {
                    DrawPenView view = mDrawViewLayout.getSaveBitmap();
                    if (view != null) {
                        mBitmap = view.clearBlank(100);
                        handler.post(runnableUi);
                    }
                }
            } catch (Exception e) {

            } finally {
                handler.removeCallbacks(runnable);
            }

        }
    };
    /**
     * 图片命名
     */
    private static String full_name = "";
    private static final String LAST_NAME = "img_";
    private int first_name = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        mSv = (ScrollView) findViewById(R.id.sv_edit_txt);
        mEText = (EditText) findViewById(R.id.modify_edit_text_view);
        mBtnLook = (Button) findViewById(R.id.btn_look);
        mBtnSave = (Button) findViewById(R.id.btn_test_save);
        mBtnRead = (Button) findViewById(R.id.btn_test_read);
        mBtnDel = (Button) findViewById(R.id.btn_test_del);
        mDrawViewLayout = (DrawViewLayout) findViewById(R.id.brush_weight);
        /**EditText禁止选择和长按*/
        mEText.setLongClickable(false);
        mEText.setTextIsSelectable(false);
        ViewUtiles.hideSoftInputMethod(mEText);
        mEText.clearFocus();
        mDrawViewLayout.setgoToAcitivity(new DrawViewLayout.IActionCallback() {

            @Override
            public void gotoSettingActivity(View view) {
                startActivity(new Intent(DrawActivity.this, SettingPenConfigActivity.class));
            }

            @Override
            public void saveBitmap(DrawPenView view) {
                mBitmap = view.clearBlank(100);
                handler.post(runnableUi);
                handler.removeCallbacks(runnable);
            }

            @Override
            public void creatNewLine() {
                int index = mEText.getSelectionStart();
                Editable editable = mEText.getText();
                editable.insert(index, "\n");//\r 这是增加空格
            }

            @Override
            public void getUptime(final long l) {
                mOldTime = l;
                handler.postDelayed(runnable, 100);
            }

            @Override
            public void stopTime() {
                handler.removeCallbacks(runnable);
            }
        });

        mBtnLook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            /*三个操作 1.保存长图 2.上传服务器  3.预览*/
                if (!TextUtils.isEmpty(mEText.getText())) {
                    mEText.setCursorVisible(false);
                    FileUtils.getScrollViewBitmap(mSv, AppConfig.PATH_SD + AppConfig.NAME_IMG_SCROLLVIEW);
                    startActivity(new Intent(DrawActivity.this, ImageActivity.class));
                    mEText.setCursorVisible(true);
                }
            }
        });
        mBtnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FileUtils.saveTextSd(mEText.getText().toString(), AppConfig.NAME_IMG_CONTENT);
                mEText.setText("");
            }
        });
        mBtnRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txt = FileUtils.readTextWrapSd();
                //                    mEText.setText(txt);

                if (TextUtils.isEmpty(txt))
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
                    Bitmap bitmap = FileUtils.readBitmapSd(AppConfig.PATH_SD + group + ".png");
                    ImageSpan imageSpan = new ImageSpan(DrawActivity.this, bitmap);
                    spannableStringBuilder.setSpan(imageSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    BitmapUtils.recycled(bitmap);
                }
                mEText.setText(spannableStringBuilder);
                mEText.setSelection(spannableStringBuilder.length());
            }
        });

        mBtnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SystemUtils.sendKeyCode(67);
            }
        });
    }

    final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            long l1 = System.currentTimeMillis();
            if ((l1 - mOldTime) > 1000) {
                handler.removeCallbacks(runnable);
                Message msg = handler.obtainMessage();
                msg.obj = true;
                handler.sendMessage(msg);
            } else {
                handler.postDelayed(this, 200);
            }

        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
        mDrawViewLayout.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    Runnable runnableUi = new Runnable() {
        @Override
        public void run() {
            final Bitmap bitmap = BitmapUtils.resizeImage(mBitmap, 100, 100);
            if (bitmap != null) {
                //根据Bitmap对象创建ImageSpan对象
                ImageSpan imageSpan = new ImageSpan(DrawActivity.this, bitmap);
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

            mDrawViewLayout.clearScreen();
        }

    };


    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }
}
