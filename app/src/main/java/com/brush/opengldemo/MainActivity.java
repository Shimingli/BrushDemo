package com.brush.opengldemo;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;

import com.brush.opengldemo.shiming.TestActivity;

import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity {


    private MyGLSurfaceView glSurfaceView;
    private RelativeLayout mainView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        mainView = (RelativeLayout) findViewById(R.id.frame);


        ButterKnife.bind(this);

        glSurfaceView = new MyGLSurfaceView(this);

        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);

        glSurfaceView.setLayoutParams(lp);

        mainView.addView(glSurfaceView, 0);

        hideSystemUi();

        findViewById(R.id.but_go).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,Main2Activity.class));
            }
        });
        findViewById(R.id.goto_demo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,BrushActivity.class));
            }
        });
        if (false){
            glSurfaceView.saveImage();
            glSurfaceView.clearScreen();
            //返回上一步
            glSurfaceView.undo();
        }

        findViewById(R.id.noopnegl).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, TestActivity.class));
            }
        });
    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        // Hides system UI when focus is regained
        if(hasFocus) {
            hideSystemUi();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        glSurfaceView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        glSurfaceView.onResume();
    }

    private void hideSystemUi() {

        int uiVisibilityFlags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;

        // Set immersive mode if >= Android 4.4
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            uiVisibilityFlags = uiVisibilityFlags | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        }

        getWindow().getDecorView().setSystemUiVisibility(uiVisibilityFlags);
    }
}
