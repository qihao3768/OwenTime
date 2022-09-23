package com.example.time_project.widget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet

import com.youth.banner.indicator.BaseIndicator
import com.youth.banner.util.BannerUtils


/**
 * 自定义数字指示器demo，比较简单，具体的自己发挥
 *
 * 这里没有用的自定义属性的参数，可以考虑加上
 */
class NumIndicator(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) :
    BaseIndicator(context, attrs, defStyleAttr) {
    private var mWidth: Int = 0
    private var mHeight: Int = 0
    private val radius: Float

    constructor(context: Context?) : this(context, null) {}
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0) {}

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val count = config.indicatorSize
        if (count <= 1) {
            return
        }
        setMeasuredDimension(mWidth, mHeight)
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val count = config.indicatorSize
        if (count <= 1) {
            return
        }
        val rectF = RectF(0F, 0F, mWidth.toFloat(), mHeight.toFloat())
        mPaint.color = Color.parseColor("#70000000")
        canvas.drawRoundRect(rectF, radius, radius, mPaint)
        val text: String = (config.currentPosition + 1).toString().plus("/") + count
        mPaint.color = Color.WHITE
        canvas.drawText(text, (mWidth / 2).toFloat(), (mHeight * 0.7).toFloat(), mPaint)
    }

    init {
        mPaint.textSize = BannerUtils.dp2px(10F).toFloat()
        mPaint.textAlign = Paint.Align.CENTER
        mWidth = BannerUtils.dp2px(30F)
        mHeight = BannerUtils.dp2px(15F)
        radius = BannerUtils.dp2px(20F).toFloat()
    }
}
