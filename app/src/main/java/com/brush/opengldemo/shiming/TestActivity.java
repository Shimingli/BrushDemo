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
import android.text.style.ImageSpan;
import android.view.View;
import android.widget.EditText;

import com.brush.opengldemo.BitmapUtils;
import com.brush.opengldemo.R;
import com.brush.opengldemo.utils.FileUtils;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        final Handler handler = new Handler();
        mEText = (EditText) findViewById(R.id.modify_edit_text_view);
//        mEText.setInputType(InputType.TYPE_NULL);
        mBrushWeightTest = (BrushWeightTest) findViewById(R.id.brush_weight);
        mBrushWeightTest.setgoToAcitivity(new BrushWeightTest.IActionCallback() {
            @Override
            public void gotoWetingActivity(View view) {
                startActivity(new Intent(TestActivity.this,SetingPenConfigActivity.class));
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


    }

    @Override
    protected void onResume() {
        super.onResume();
        mBrushWeightTest.onResume();
    }

    private static String full_name = "";
    private static final String LAST_NAME = "img_";
    private int first_name = 1;
    Runnable   runnableUi=new  Runnable(){
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
