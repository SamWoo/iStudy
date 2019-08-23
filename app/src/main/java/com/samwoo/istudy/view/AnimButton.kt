package com.samwoo.istudy.view

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.samwoo.istudy.R
import com.samwoo.istudy.util.SLog

class AnimButton : Button {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private var backDrawable: GradientDrawable = GradientDrawable()
    private var colorDrawable: Int = ContextCompat.getColor(context, R.color.colorAccent)
    private var paint: Paint = Paint()
    private var startAngle: Int = 0
    private lateinit var arcValueAnimator: ValueAnimator
    private var isRunning: Boolean = false

    init {
        backDrawable.apply {
            setColor(colorDrawable)
            cornerRadius = 120f
        }
        background = backDrawable

        setTextColor(ContextCompat.getColor(context, R.color.white))
        paint.apply {
            color = ContextCompat.getColor(context, R.color.while_most_color)
            strokeWidth = 4f
            style = Paint.Style.STROKE
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)

        if (widthMode == MeasureSpec.EXACTLY) width = widthSize
        if (heightMode == MeasureSpec.EXACTLY) height = heightSize
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (isRunning) {
            val rectF =
                RectF((width - height) / 2f, 2f, (width + height) / 2f, (height - 2).toFloat())
            canvas?.drawArc(rectF, startAngle.toFloat(), 270f, false, paint)
        }
    }

    fun startAnim() {
        isRunning = true
        this.text = ""

        var valueAnimator: ValueAnimator = ValueAnimator.ofInt(width, height)
        valueAnimator.addUpdateListener { animation ->
            var value = animation.animatedValue as Int
            var leftOffset = (width - value) / 2
            var rightOffset = width - leftOffset

            backDrawable.setBounds(leftOffset, 0, rightOffset, height)
        }

        var objectAnimator: ObjectAnimator =
            ObjectAnimator.ofFloat(backDrawable, "cornerRadius", 120f, height / 2f)

        var animatorSet = AnimatorSet()
        animatorSet.run {
            duration = 500
            playTogether(valueAnimator, objectAnimator)
            start()
        }

        //画中间的白色圆圈
        showArc()
    }

    private fun showArc() {
        arcValueAnimator = ValueAnimator.ofInt(0, 1080)
        arcValueAnimator.addUpdateListener { animation ->
            startAngle = animation.animatedValue as Int
            invalidate()
        }
        arcValueAnimator.run {
            interpolator = LinearInterpolator()
            repeatCount = ValueAnimator.INFINITE
            duration = 3 * 1000
            start()
        }
    }

    fun stopAnim() {
        isRunning = false
        arcValueAnimator.cancel()
        this.visibility = View.GONE
    }

    fun reset(msg: String?) {
//        stopAnim()
        isRunning = false
        arcValueAnimator.cancel()
        this.visibility = View.VISIBLE
        backDrawable.apply {
            setBounds(0, 0, width, height)
            cornerRadius = 120f
        }
        background = backDrawable
        text = msg
        setTextColor(ContextCompat.getColor(context, R.color.white))
    }

    //设置背景颜色
    fun setBackgroudColor(resId: Int) {
        colorDrawable = ContextCompat.getColor(context, resId)
    }
}