package com.example.nerd.charttest

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.DashPathEffect
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PointF
import android.graphics.RectF
import android.graphics.Shader
import android.util.AttributeSet
import android.view.View

import java.util.ArrayList

/**
 * Created by xcj on 2017/4/28.
 */

class CurveChart @JvmOverloads constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {
    //坐标点需要转化为实际的点
    private var txtPaint: Paint? = null
    private var linePaint: Paint? = null
    private var curPaint: Paint? = null

    private var AxisXRect: RectF? = null
    private var AxisYRect: RectF? = null

    enum class TYPE {
        FOLD, CURVER
    }

    private var type: TYPE? = null
    private var points: ArrayList<PointF>? = null
    private var mCanvas: Canvas? = null
    private var mBitmap: Bitmap? = null
    private var holder: Bitmap? = null
    private val sparkPath = Path()
    private val renderPath = Path()

    private val mTxtSize = 40
    private val mTxtColor = Color.GRAY
    private val mSpace = 100
    private var mPaddingTop = 40

    private var viewWidth: Int = 0
    private var viewHeight: Int = 0
    private var Xst: Array<String>? = null
    private var Yst: Array<String>? = null

    private var isDrawed = false
    private var isMeasured = false
    private var isTurned = false

    init {
        init()
    }

    fun init() {
        linePaint = Paint()
        linePaint!!.style = Paint.Style.STROKE
        linePaint!!.strokeWidth = 2f
        linePaint!!.color = Color.parseColor("#e3e3e3")

        txtPaint = Paint()
        txtPaint!!.style = Paint.Style.FILL
        txtPaint!!.textSize = mTxtSize.toFloat()
        txtPaint!!.color = mTxtColor

        curPaint = Paint()
        curPaint!!.style = Paint.Style.STROKE
        curPaint!!.strokeWidth = 5f
        curPaint!!.color = Color.GREEN

        type = TYPE.CURVER

        points = ArrayList<PointF>()

    }

    fun setXst(str: Array<String>) {
        Xst = str
    }

    fun setYst(str: Array<String>) {
        Yst = str
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthMode = View.MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = View.MeasureSpec.getSize(widthMeasureSpec)
        val heightMode = View.MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = View.MeasureSpec.getSize(heightMeasureSpec)
        if (widthMode == View.MeasureSpec.EXACTLY) {
            viewWidth = widthSize
        } else {
            viewWidth = widthSize
        }
        if (heightMode == View.MeasureSpec.EXACTLY) {
            viewHeight = heightSize
        } else {
            mPaddingTop += paddingTop
            viewHeight = mSpace * 4 + 2 * mPaddingTop + mTxtSize + paddingBottom
        }

        setMeasuredDimension(viewWidth, viewHeight)
        AxisXRect = RectF(paddingLeft.toFloat(), paddingTop.toFloat(), paddingLeft.toFloat() + maxTextWidth + 10f, (viewHeight - paddingBottom).toFloat())
        AxisYRect = RectF(AxisXRect!!.right, (viewHeight - paddingBottom - mTxtSize - mPaddingTop).toFloat(), (viewWidth - paddingRight).toFloat(), (viewHeight - paddingBottom).toFloat())
        isMeasured = true
    }

    override fun onDraw(canvas: Canvas) {
        if (isDrawed) {
            canvas.drawBitmap(holder!!, 0f, 0f, null)
            return
        }

        if (isMeasured && !isTurned)
            turnPoint()
        mCanvas!!.drawPath(renderPath, curPaint!!)
        holder = mBitmap
        canvas.drawBitmap(mBitmap!!, 0f, 0f, null)
        isDrawed = true
    }

    fun setType(type: TYPE) {
        this.type = type
    }

    fun setPoints(points: ArrayList<PointF>) {
        if (this.points === points) {
            return
        }
        this.points = points
        isDrawed = false
        isTurned = false
        postInvalidate()
    }

    private fun caculateCurPaint(paint: Paint) {
        paint.setShadowLayer(10f, 0f, 5f, Color.parseColor("#AAffb90f"))
        val mLinearGradient = LinearGradient(
                0f, mPaddingTop.toFloat(), 0f, AxisYRect!!.top,
                intArrayOf(Color.parseColor("#ff7f00"), Color.parseColor("#ffb90f"), Color.parseColor("#ffb90f"), Color.parseColor("#6BE61A"), Color.parseColor("#6BE61A"), Color.parseColor("#1AE6E6")),
                floatArrayOf(0.235f, 0.265f, 0.485f, 0.515f, 0.735f, 0.765f),
                Shader.TileMode.CLAMP)
        paint.shader = mLinearGradient
    }

    private fun turnPoint() {
        val zoomY = mSpace.toFloat()
        val left = AxisYRect!!.left
        val width = AxisYRect!!.right - AxisYRect!!.left
        for (i in points!!.indices) {
            val pointF = points!![i]
            points!![i].set((left + (i + 0.5) * width / points!!.size).toFloat(), AxisYRect!!.top - pointF.y * zoomY)
        }
        isTurned = true
        mBitmap = Bitmap.createBitmap(getWidth(), height, Bitmap.Config.ARGB_8888)
        mCanvas = Canvas(mBitmap!!)
        drawXT()
        drawYT()

        caculateCurPaint(curPaint!!)
        if (type == TYPE.CURVER) {
            caculateScrollLine()
        } else {
            caculateFoldLine()
        }
    }

    private fun caculateScrollLine() {
        var startp: PointF
        var endp: PointF
        for (i in 0..points!!.size - 1 - 1) {
            if (i == 0)
                sparkPath.reset()
            startp = points!![i]
            endp = points!![i + 1]
            val wt = (startp.x + endp.x) / 2
            val p3 = PointF()
            val p4 = PointF()
            p3.y = startp.y
            p3.x = wt
            p4.y = endp.y
            p4.x = wt

            val path = Path()
            path.moveTo(startp.x, startp.y)
            path.cubicTo(p3.x, p3.y, p4.x, p4.y, endp.x, endp.y)
            sparkPath.addPath(path)
        }
        renderPath.reset()
        renderPath.addPath(sparkPath)

    }

    private fun caculateFoldLine() {
        val path = Path()
        path.moveTo(points!![0].x, points!![0].y)
        for (i in 1..points!!.size - 1)
            path.lineTo(points!![i].x, points!![i].y)
        renderPath.reset()
        renderPath.addPath(path)
    }


    private fun drawXT() {
        val width = AxisXRect!!.right
        val fontMetrics = txtPaint!!.fontMetrics
        val baseLine = ((paddingTop + 2 * mPaddingTop).toFloat() - fontMetrics.bottom - fontMetrics.top).toInt() shr 1
        for (i in Xst!!.indices) {
            mCanvas!!.drawText(Xst!![i], width - getTextWidth(txtPaint!!, Xst!![i]) - 10f, (baseLine + i * mSpace).toFloat(), txtPaint!!)

            if (i == 1 || i == 3) {
                val paint = Paint()
                paint.style = Paint.Style.STROKE
                paint.strokeWidth = 2f
                paint.pathEffect = DashPathEffect(floatArrayOf(10f, 5f), 0f)
                if (i == 1) {
                    paint.color = Color.parseColor("#ff7f00")
                } else {
                    paint.color = Color.parseColor("#1AE6E6")
                }
                val path = Path()
                path.moveTo(width, (mPaddingTop + i * mSpace).toFloat())
                path.lineTo((viewWidth - paddingRight).toFloat(), (mPaddingTop + i * mSpace).toFloat())
                mCanvas!!.drawPath(path, paint)
                //google drawLine no support dashed line
                //                canvas.drawLine(width, (i + 1) * height / 5 + txtPaint.getTextSize() / 4, getWidth() - 20, (i + 1) * height / 5 + txtPaint.getTextSize() / 4, paint);

            } else {
                mCanvas!!.drawLine(width, (mPaddingTop + i * mSpace).toFloat(), (viewWidth - paddingRight).toFloat(), (mPaddingTop + i * mSpace).toFloat(), linePaint!!)
            }
        }
    }

    private fun drawYT() {
        val left = AxisYRect!!.left

        val fontMetrics = txtPaint!!.fontMetrics
        val baseLine = (AxisYRect!!.bottom + AxisYRect!!.top - fontMetrics.bottom - fontMetrics.top).toInt() shr 1

        for (i in Yst!!.indices) {
            mCanvas!!.drawText(Yst!![i], (left + AxisYRect!!.width() / Yst!!.size * (i + 0.5)).toFloat() - getTextWidth(txtPaint!!, Yst!![i]) / 2, baseLine.toFloat(), txtPaint!!)
        }
    }

    val maxTextWidth: Float
        get() {
            var max = 0f
            for (i in Xst!!.indices) {
                max = Math.max(max, getTextWidth(txtPaint!!, Xst!![i]))
            }
            return max
        }

    fun getTextWidth(textPaint: Paint, text: String): Float {
        return textPaint.measureText(text)
    }
}
