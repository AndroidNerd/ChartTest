package com.example.nerd.charttest;

import android.graphics.PointF;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    CurveChart chart, chart2;
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        chart = (CurveChart) findViewById(R.id.chart);
        chart2 = (CurveChart) findViewById(R.id.chart2);
        btn = (Button) findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<PointF> points = new ArrayList<>();
                for (int i = 0; i < 8; i++) {
                    PointF point = new PointF(i, new Random().nextFloat() * 4f);
                    points.add(point);
                }
                chart.setPoints(points);
            }
        });
        ArrayList<PointF> points = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            PointF point = new PointF(i, new Random().nextFloat() * 4f);
            points.add(point);
        }
        chart.setPoints(points);
        String str[] = new String[]{"20", "15", "10", "5", "0"};
        chart.setXst(str);

        String str2[] = new String[]{"20", "21", "22", "23", "24", "25", "26", "27"};
        chart.setYst(str2);
//        chart.setType(CurveChart.TYPE.FOLD);
        ArrayList<PointF> points2 = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            PointF point = new PointF(i, new Random().nextFloat() * 4f);
            points2.add(point);
        }
        chart2.setPoints(points2);
        chart2.setXst(str);
        String str3[] = new String[]{"20", "21", "22", "23", "24", "25"};
        chart2.setYst(str3);
    }
}
