package com.brush.opengldemo.shiming;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.view.View;
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

public class TestActivity extends AppCompatActivity {
    public static final int MODE_PATH = 0;
    public static final int CANVAS_NORMAL = 0;
    public static final int CANVAS_UNDO = 1;//撤回画笔
    public static final int CANVAS_REDO = 2;
    public static final int CANVAS_RESET = 3;//全部清除
    private DrawView mDrawView;
    public static final int COMMAND_ADD = 0;
    private BrushWeightTest mBrushWeightTest;
    public static final String ROOT_DIRECTORY = Environment.getExternalStorageDirectory().toString() + "/note";
    public static final String TEMPORARY_PATH = ROOT_DIRECTORY + "/temporary";
    private Bitmap mBitmap;
    private EditText mEText;
    private Button mBtnLook,mBtnSave,mBtnRead,mBtnDel;
    private ScrollView mSv;
    /**图片命名*/
    private static String full_name = "";
    private static final String LAST_NAME = "img_";
    private int first_name = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        final Handler handler = new Handler();
        mSv = (ScrollView) findViewById(R.id.sv_edit_txt);
        mEText = (EditText) findViewById(R.id.modify_edit_text_view);
        mBtnLook = (Button) findViewById(R.id.btn_look);
        mBtnSave = (Button) findViewById(R.id.btn_test_save);
        mBtnRead = (Button) findViewById(R.id.btn_test_read);
        mBtnDel = (Button) findViewById(R.id.btn_test_del);
        mBrushWeightTest = (BrushWeightTest) findViewById(R.id.brush_weight);
        /**EditText禁止选择和长按*/
        mEText.setLongClickable(false);
        mEText.setTextIsSelectable(false);
        ViewUtiles.hideSoftInputMethod(mEText);
        mBrushWeightTest.setgoToAcitivity(new BrushWeightTest.IActionCallback() {
            @Override
            public void gotoWetingActivity(View view) {
                startActivity(new Intent(TestActivity.this, SetingPenConfigActivity.class));
            }

            @Override
            public void save(DrawView view) {
                mBitmap = view.clearBlank(100);
                handler.post(runnableUi);

            }

            @Override
            public void creatNewLine() {
//                mEText.setText("\n\r");
                int index = mEText.getSelectionStart();
                Editable editable = mEText.getText();
                editable.insert(index, "\n");//\r 这是增加空格
            }
        });

        mBtnLook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            /*三个操作 1.保存长图 2.上传服务器  3.预览*/
//                if (!TextUtils.isEmpty(mEText.getText())) {
                    FileUtils.getScrollViewBitmap(mSv, AppConfig.PATH_SD + AppConfig.NAME_IMG_SCROLLVIEW);
//                    File file = new File(AppConfig.PATH_SD + AppConfig.NAME_IMG_SCROLLVIEW);
//                    NetworkUtil.qnFile(file, AppConfig.NAME_IMG_SCROLLVIEW);
                    startActivity(new Intent(TestActivity.this, ImageActivity.class));
//                }
            }
        });
        mBtnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FileUtils.saveTextSd(mEText.getText().toString(),AppConfig.NAME_IMG_CONTENT);
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
                    Bitmap bitmap = FileUtils.readBitmapSd(AppConfig.PATH_SD + group + ".png");
                    ImageSpan imageSpan = new ImageSpan(TestActivity.this, bitmap);
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

    @Override
    protected void onResume() {
        super.onResume();
        mBrushWeightTest.onResume();
    }


    Runnable runnableUi = new Runnable() {
        @Override
        public void run() {
            final Bitmap bitmap = BitmapUtils.resizeImage(mBitmap, 100, 100);
            if (bitmap != null) {
                //根据Bitmap对象创建ImageSpan对象
                ImageSpan imageSpan = new ImageSpan(TestActivity.this, bitmap);
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

            mBrushWeightTest.clearScreen();
        }

    };


}
