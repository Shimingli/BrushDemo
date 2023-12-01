package com.brush.opengldemo;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;

import com.brush.opengldemo.weight.BrushWeight;

public class BrushActivity extends AppCompatActivity {

    private BrushWeight mBrushWeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brush);
        findView();
    }

    private void findView() {
        mBrushWeight = (BrushWeight) findViewById(R.id.brush_weight);
        mBrushWeight.setgoToAcitivity(new BrushWeight.IActionCallback() {
            @Override
            public void gotoWetingActivity(View view) {
                 startActivity(new Intent(BrushActivity.this,Main3Activity.class));
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        mBrushWeight.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mBrushWeight.onResume();
    }
}
