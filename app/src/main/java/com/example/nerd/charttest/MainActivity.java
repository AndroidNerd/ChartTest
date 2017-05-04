package com.example.nerd.charttest;

import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.PointF;
import android.graphics.Shader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    CurveChart chart;
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        chart = (CurveChart) findViewById(R.id.chart);
        btn = (Button) findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<PointF> points = new ArrayList<>();
                for (int i = 0; i < 7; i++) {
                    PointF point = new PointF(i, new Random().nextFloat() * 4f);
                    points.add(point);
                }
                chart.setPoints(points);
            }
        });
        ArrayList<PointF> points = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            PointF point = new PointF(i, new Random().nextFloat() * 4f);
            points.add(point);
        }
        chart.setPoints(points);
//        chart.setType(CurveChart.TYPE.FOLD);
    }
}
