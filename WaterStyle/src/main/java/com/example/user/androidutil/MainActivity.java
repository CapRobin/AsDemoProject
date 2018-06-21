package com.example.user.androidutil;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.anlia.progressbar.WaveProgressView;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {
    private TextView textProgress;
    private WaveProgressView waveProgressView;
    private Button testBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textProgress = (TextView) findViewById(R.id.text_progress);
        testBtn = (Button) findViewById(R.id.testBtn);
        waveProgressView = (WaveProgressView) findViewById(R.id.wave_progress);

        testBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                waveProgressView.setTextView(textProgress);
                waveProgressView.setOnAnimationListener(new WaveProgressView.OnAnimationListener() {
                    @Override
                    public String howToChangeText(float interpolatedTime, float updateNum, float maxNum) {
                        DecimalFormat decimalFormat = new DecimalFormat("0.00");
                        String s = decimalFormat.format(interpolatedTime * updateNum / maxNum * 100)+"%";
                        return s;
                    }

                    @Override
                    public float howToChangeWaveHeight(float percent, float waveHeight) {
                        return (1-percent)*waveHeight;
                    }
                });
                waveProgressView.setProgressNum(80,3000);
                waveProgressView.setDrawSecondWave(true);
            }
        });
//        waveProgressView.setTextView(textProgress);
//        waveProgressView.setOnAnimationListener(new WaveProgressView.OnAnimationListener() {
//            @Override
//            public String howToChangeText(float interpolatedTime, float updateNum, float maxNum) {
//                DecimalFormat decimalFormat = new DecimalFormat("0.00");
//                String s = decimalFormat.format(interpolatedTime * updateNum / maxNum * 100)+"%";
//                return s;
//            }
//
//            @Override
//            public float howToChangeWaveHeight(float percent, float waveHeight) {
//                return 0;
//            }
//        });
//        waveProgressView.setProgressNum(100,3000);

    }
}
