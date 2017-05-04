package com.example.nerd.charttest;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by xcj on 2017/4/28.
 */

public class CurveChart extends View {
    //坐标点需要转化为实际的点
    Paint txtPaint;
    Paint linePaint;
    Paint curPaint;

    RectF AxisXRect;
    RectF AxisYRect;

    ArrayList<PointF> points;

    public CurveChart(Context context) {
        this(context, null);
    }

    public CurveChart(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CurveChart(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init() {
        linePaint = new Paint();
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeWidth(2f);
        linePaint.setColor(Color.GRAY);

        txtPaint = new Paint();
        txtPaint.setStyle(Paint.Style.FILL);
        txtPaint.setTextSize(25f);
        txtPaint.setColor(Color.GRAY);

        curPaint = new Paint();
        curPaint.setStyle(Paint.Style.STROKE);
        curPaint.setStrokeWidth(5f);
        curPaint.setColor(Color.GREEN);

        points = new ArrayList<PointF>();

//        for (int i = 0; i < 7; i++) {
//            PointF point = new PointF(i, new Random().nextFloat()*4f);
//            points.add(point);
//        }
        points.add(new PointF(0, 0.5f));
        points.add(new PointF(1, 3.5f));
        points.add(new PointF(2, 1.5f));
        points.add(new PointF(3, 0.5f));
        points.add(new PointF(4, 2.5f));
        points.add(new PointF(5, 3.5f));
        points.add(new PointF(6, 0.5f));


    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        AxisXRect = new RectF(0, 0, getWidth() / 10, getHeight());
        AxisYRect = new RectF(AxisXRect.width(), getHeight() / 5 * 4, getWidth(), getHeight());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Log.e("X", AxisXRect.left + "  " + AxisXRect.top + "   " + AxisXRect.right + "   " + AxisXRect.bottom);
        Log.e("X", AxisYRect.left + "  " + AxisYRect.top + "   " + AxisYRect.right + "   " + AxisYRect.bottom);
//        canvas.drawRect(AxisXRect, linePaint);
//        canvas.drawRect(AxisYRect, linePaint);
        drawXT(canvas);
        drawYT(canvas);
        turnPoint();
//        drawCurve(canvas);
        drawFoldLine(canvas);

    }

    private void turnPoint() {
        float zoomY = AxisYRect.top / 5 + txtPaint.getTextSize() / 4;
        float left = AxisYRect.left;
        float width = AxisYRect.right - AxisYRect.left;
        for (int i = 0; i < points.size(); i++) {
            PointF pointF = points.get(i);
            points.get(i).set(left + i * width / points.size(), AxisYRect.top - pointF.y * zoomY);
        }
    }

    private void drawCurve(Canvas canvas) {
        Path path = new Path();
        path.moveTo(points.get(0).x, points.get(0).y);
        for (int i = 1; i < points.size() - 1; i = i + 3)
            path.cubicTo(points.get(i - 1).x, points.get(i - 1).y, points.get(i).x, points.get(i).y, points.get(i + 1).x, points.get(i + 1).y);

        LinearGradient mLinearGradient = new LinearGradient(
                0, 0, 0, AxisYRect.top,
                new int[]{
                        Color.parseColor("#ff7f00"),
                        Color.parseColor("#ff7f00"),
                        Color.parseColor("#ffb90f"),
                        Color.parseColor("#ffb90f"),
                        Color.parseColor("#6BE61A"),
                        Color.parseColor("#6BE61A"),
                        Color.parseColor("#1AE6E6"),
                        Color.parseColor("#1AE6E6")},
                new float[]{0.175f, 0.375f, 0.425f, 0.575f, 0.625f, 0.775f, 0.825f, 1f},
                Shader.TileMode.CLAMP);
        curPaint.setShader(mLinearGradient);
        canvas.drawPath(path, curPaint);

    }

    private void drawFoldLine(Canvas canvas) {
        Path path = new Path();
        path.moveTo(points.get(0).x, points.get(0).y);
        for (int i = 1; i < points.size(); i++)
            path.lineTo(points.get(i).x, points.get(i).y);
        LinearGradient mLinearGradient = new LinearGradient(
                0, 0, 0, AxisYRect.top,
                new int[]{
                        Color.parseColor("#ff7f00"),
                        Color.parseColor("#ff7f00"),
                        Color.parseColor("#ffb90f"),
                        Color.parseColor("#ffb90f"),
                        Color.parseColor("#6BE61A"),
                        Color.parseColor("#6BE61A"),
                        Color.parseColor("#1AE6E6"),
                        Color.parseColor("#1AE6E6")},
                new float[]{0.175f, 0.375f, 0.425f, 0.575f, 0.625f, 0.775f, 0.825f, 1f},
                Shader.TileMode.CLAMP);
        curPaint.setShader(mLinearGradient);
        canvas.drawPath(path, curPaint);

    }

    private void drawXT(Canvas canvas) {
        String str[] = new String[]{"20", "15", "10", "5", "0"};
        float height = AxisYRect.top;
        float width = AxisXRect.right;
        for (int i = 0; i < str.length; i++) {
            canvas.drawText(str[i], width - getTextWidth(txtPaint, str[i]) - 10, (i + 1) * height / 5 + txtPaint.getTextSize() / 2, txtPaint);

            if (i == 1) {
                Paint paint = new Paint();
                paint.setStyle(Paint.Style.STROKE);
                paint.setStrokeWidth(2f);
                paint.setColor(Color.parseColor("#ff7f00"));
                paint.setPathEffect(new DashPathEffect(new float[]{10, 5}, 0));
                Path path = new Path();
                path.moveTo(width, (i + 1) * height / 5 + txtPaint.getTextSize() / 4);
                path.lineTo(getWidth() - 20, (i + 1) * height / 5 + txtPaint.getTextSize() / 4);
                canvas.drawPath(path, paint);
                //google drawLine no support dashed line
//                canvas.drawLine(width, (i + 1) * height / 5 + txtPaint.getTextSize() / 4, getWidth() - 20, (i + 1) * height / 5 + txtPaint.getTextSize() / 4, paint);

            } else if (i == 3) {
                Paint paint = new Paint();
                paint.setStyle(Paint.Style.STROKE);
                paint.setStrokeWidth(2f);
                paint.setColor(Color.parseColor("#1AE6E6"));
                paint.setPathEffect(new DashPathEffect(new float[]{10, 5}, 0));
                Path path = new Path();
                path.moveTo(width, (i + 1) * height / 5 + txtPaint.getTextSize() / 4);
                path.lineTo(getWidth() - 20, (i + 1) * height / 5 + txtPaint.getTextSize() / 4);
                canvas.drawPath(path, paint);

            } else {
                canvas.drawLine(width, (i + 1) * height / 5 + txtPaint.getTextSize() / 4, getWidth() - 20, (i + 1) * height / 5 + txtPaint.getTextSize() / 4, linePaint);
            }

        }
    }

    private void drawYT(Canvas canvas) {
        linePaint.setTextSize(50f);
        String str[] = new String[]{"20", "21", "22", "23", "24", "25", "26"};
        float width = AxisYRect.right - AxisYRect.left;
        float height = AxisYRect.top + 20;
        float left = AxisYRect.left;
        for (int i = 0; i < str.length; i++) {
            canvas.drawText(str[i], left + i * width / str.length, height + txtPaint.getTextSize(), txtPaint);
        }
    }

    public float getTextWidth(Paint textPaint, String text) {
        return textPaint.measureText(text);
    }
}
