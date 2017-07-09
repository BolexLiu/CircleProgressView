package com.bolex.circleprogressview;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.bolex.circleprogresslibrary.CircleProgressView;

public class MainActivity extends AppCompatActivity {

    private CircleProgressView mCircleProgressView1;
    private CircleProgressView mCircleProgressView2;
    private CircleProgressView mCircleProgressView3;
    private Button add;
    private Button change_color;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mCircleProgressView1 = (CircleProgressView) findViewById(R.id.circleProgressView1);
        mCircleProgressView2 = (CircleProgressView) findViewById(R.id.circleProgressView2);
        mCircleProgressView3 = (CircleProgressView) findViewById(R.id.circleProgressView3);
        add = (Button) findViewById(R.id.add);
        change_color = (Button) findViewById(R.id.change_color);
        change_color.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCircleProgressView2.setmProgressBackgroundColor(Color.parseColor("#FF5722"));
                mCircleProgressView2.setmProgressHeadColor(Color.parseColor("#FF5722"));
                mCircleProgressView3.setmProgressColor(Color.parseColor("#689F38"));
                mCircleProgressView3.setmProgressHeadColor(Color.parseColor("#689F38"));
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float v1 = mCircleProgressView1.getmProgressValue();
                mCircleProgressView1.setProgress(v1+10);
                mCircleProgressView2.setProgress(v1+10);
                mCircleProgressView3.setProgress(v1+10);
            }
        });
    }
}
