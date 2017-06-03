package com.example.nerd.charttest

import android.graphics.PointF
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button

import java.util.ArrayList
import java.util.Random

class MainActivity : AppCompatActivity() {
    internal var chart: CurveChart?=null
    internal var chart2: CurveChart?=null
    internal var btn: Button?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        chart = findViewById(R.id.chart) as CurveChart
        chart2 = findViewById(R.id.chart2) as CurveChart
        btn = findViewById(R.id.btn) as Button
        btn!!.setOnClickListener {
            val points = ArrayList<PointF>()
            for (i in 0..7) {
                val point = PointF(i.toFloat(), Random().nextFloat() * 4f)
                points.add(point)
            }
            chart!!.setPoints(points)
        }
        val points = ArrayList<PointF>()
        for (i in 0..7) {
            val point = PointF(i.toFloat(), Random().nextFloat() * 4f)
            points.add(point)
        }
        chart!!.setPoints(points)
        val str = arrayOf("20", "15", "10", "5", "0")
        chart!!.setXst(str)

        val str2 = arrayOf("20", "21", "22", "23", "24", "25", "26", "27")
        chart!!.setYst(str2)
        //        chart.setType(CurveChart.TYPE.FOLD);
        val points2 = ArrayList<PointF>()
        for (i in 0..5) {
            val point = PointF(i.toFloat(), Random().nextFloat() * 4f)
            points2.add(point)
        }
        chart2!!.setPoints(points2)
        chart2!!.setXst(str)
        val str3 = arrayOf("20", "21", "22", "23", "24", "25")
        chart2!!.setYst(str3)
    }
}
