package com.example.nerd.charttest;

import android.content.Context;
import android.graphics.Bitmap;
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
import android.view.View;

import java.util.ArrayList;

/**
 * Created by xcj on 2017/4/28.
 */

public class CurveChart extends View {
    //坐标点需要转化为实际的点
    private Paint txtPaint;
    private Paint linePaint;
    private Paint curPaint;

    private RectF AxisXRect;
    private RectF AxisYRect;

    public enum TYPE {
        FOLD, CURVER
    }

    private TYPE type;
    private ArrayList<PointF> points;
    private Canvas mCanvas;
    private Bitmap mBitmap, holder;
    private Path sparkPath = new Path();
    private Path renderPath = new Path();

    private int mTxtSize = 40;
    private int mTxtColor = Color.GRAY;
    private int mSpace = 100;
    private int mPaddingTop = 20;

    private int viewWidth, viewHeight;
    private String[] Xst, Yst;

    private boolean isDrawed = false;
    private boolean isMeasured = false;
    private boolean isTurned = false;

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
        linePaint.setColor(Color.parseColor("#e3e3e3"));

        txtPaint = new Paint();
        txtPaint.setStyle(Paint.Style.FILL);
        txtPaint.setTextSize(mTxtSize);
        txtPaint.setColor(mTxtColor);

        curPaint = new Paint();
        curPaint.setStyle(Paint.Style.STROKE);
        curPaint.setStrokeWidth(5f);
        curPaint.setColor(Color.GREEN);

        type = TYPE.CURVER;

        points = new ArrayList<PointF>();

    }

    public void setXst(String[] str) {
        Xst = str;
    }

    public void setYst(String[] str) {
        Yst = str;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        if (widthMode == MeasureSpec.EXACTLY) {
            viewWidth = widthSize;
        } else {
            viewWidth = widthSize;
        }
        if (heightMode == MeasureSpec.EXACTLY) {
            viewHeight = heightSize;
        } else {
            mPaddingTop += getPaddingTop();
            viewHeight = mSpace * 4 + mPaddingTop + mTxtSize + getPaddingBottom();
        }

        setMeasuredDimension(viewWidth, viewHeight);
        AxisXRect = new RectF(getPaddingLeft(), mSpace - mTxtSize / 2, getPaddingLeft() + 2 * mTxtSize, viewHeight - getPaddingBottom() - mTxtSize);
        AxisYRect = new RectF(AxisXRect.right, viewHeight - getPaddingBottom() - mTxtSize, viewWidth - getPaddingRight(), viewHeight);
        isMeasured = true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (isDrawed) {
            canvas.drawBitmap(holder, 0, 0, null);
            return;
        }

        if (isMeasured && !isTurned)
            turnPoint();
        mCanvas.drawPath(renderPath, curPaint);
        holder = mBitmap;
        canvas.drawBitmap(mBitmap, 0, 0, null);
        isDrawed = true;
    }

    public void setType(TYPE type) {
        this.type = type;
    }

    public void setPoints(ArrayList<PointF> points) {
        if (this.points == points) {
            return;
        }
        this.points = points;
        isDrawed = false;
        isTurned = false;
        postInvalidate();
    }

    private void caculateCurPaint(Paint paint) {
        paint.setShadowLayer(10F, 0F, 5F, Color.parseColor("#AAffb90f"));
        LinearGradient mLinearGradient = new LinearGradient(
                0, mPaddingTop, 0, AxisYRect.top,
                new int[]{
                        Color.parseColor("#ff7f00"),
                        Color.parseColor("#ffb90f"),
                        Color.parseColor("#ffb90f"),
                        Color.parseColor("#6BE61A"),
                        Color.parseColor("#6BE61A"),
                        Color.parseColor("#1AE6E6")},
                new float[]{0.235f, 0.265f, 0.485f, 0.515f, 0.735f, 0.765f},
                Shader.TileMode.CLAMP);
        paint.setShader(mLinearGradient);
    }

    private void turnPoint() {
        float zoomY = mSpace;
        float left = AxisYRect.left;
        float width = AxisYRect.right - AxisYRect.left;
        for (int i = 0; i < points.size(); i++) {
            PointF pointF = points.get(i);
            points.get(i).set((float) (left + (i + 0.5) * width / points.size()), AxisYRect.top - pointF.y * zoomY);
        }
        isTurned = true;
        mBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
        drawXT();
        drawYT();

        caculateCurPaint(curPaint);
        if (type == TYPE.CURVER) {
            caculateScrollLine();
        } else {
            caculateFoldLine();
        }
    }

    private void caculateScrollLine() {
        PointF startp;
        PointF endp;
        for (int i = 0; i < points.size() - 1; i++) {
            if (i == 0)
                sparkPath.reset();
            startp = points.get(i);
            endp = points.get(i + 1);
            float wt = (startp.x + endp.x) / 2;
            PointF p3 = new PointF();
            PointF p4 = new PointF();
            p3.y = startp.y;
            p3.x = wt;
            p4.y = endp.y;
            p4.x = wt;

            Path path = new Path();
            path.moveTo(startp.x, startp.y);
            path.cubicTo(p3.x, p3.y, p4.x, p4.y, endp.x, endp.y);
            sparkPath.addPath(path);
        }
        renderPath.reset();
        renderPath.addPath(sparkPath);

    }

    private void caculateFoldLine() {
        Path path = new Path();
        path.moveTo(points.get(0).x, points.get(0).y);
        for (int i = 1; i < points.size(); i++)
            path.lineTo(points.get(i).x, points.get(i).y);
        renderPath.reset();
        renderPath.addPath(path);
    }


    private void drawXT() {
        float width = AxisXRect.right;
        for (int i = 0; i < Xst.length; i++) {
            mCanvas.drawText(Xst[i], width - getTextWidth(txtPaint, Xst[i]) - 10, mPaddingTop + i * mSpace + txtPaint.getTextSize() / 2, txtPaint);

            if (i == 1 || i == 3) {
                Paint paint = new Paint();
                paint.setStyle(Paint.Style.STROKE);
                paint.setStrokeWidth(2f);
                paint.setPathEffect(new DashPathEffect(new float[]{10, 5}, 0));
                if (i == 1) {
                    paint.setColor(Color.parseColor("#ff7f00"));
                } else {
                    paint.setColor(Color.parseColor("#1AE6E6"));
                }
                Path path = new Path();
                path.moveTo(width, mPaddingTop + i * mSpace);
                path.lineTo(viewWidth - getPaddingRight(), mPaddingTop + i * mSpace);
                mCanvas.drawPath(path, paint);
                //google drawLine no support dashed line
//                canvas.drawLine(width, (i + 1) * height / 5 + txtPaint.getTextSize() / 4, getWidth() - 20, (i + 1) * height / 5 + txtPaint.getTextSize() / 4, paint);

            } else {
                mCanvas.drawLine(width, mPaddingTop + i * mSpace, viewWidth - getPaddingRight(), mPaddingTop + i * mSpace, linePaint);
            }
        }
    }

    private void drawYT() {
        float left = AxisYRect.left;

        Paint.FontMetrics fontMetrics = txtPaint.getFontMetrics();
        int baseLine = (int) (viewHeight - getPaddingBottom() + viewHeight - getPaddingBottom() - mTxtSize - fontMetrics.bottom - fontMetrics.top) >> 1;

        for (int i = 0; i < Yst.length; i++) {
            mCanvas.drawText(Yst[i], (float) (left + AxisYRect.width() / Yst.length * (i + 0.5)) - getTextWidth(txtPaint, Yst[i]) / 2, baseLine + 20, txtPaint);
        }
    }

    public float getTextWidth(Paint textPaint, String text) {
        return textPaint.measureText(text);
    }
}
