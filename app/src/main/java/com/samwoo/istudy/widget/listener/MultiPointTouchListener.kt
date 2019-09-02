package com.samwoo.istudy.widget.listener

import android.graphics.Matrix
import android.graphics.PointF
import android.util.FloatMath
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView

/**
 * 自定义ImageView缩放监听器
 */
class MultiPointTouchListener : View.OnTouchListener {
    var matrix: Matrix = Matrix()
    var sMatrix: Matrix = Matrix()

    companion object {
        const val NONE: Int = 0
        const val DRAG: Int = 1
        const val ZOOM: Int = 2
    }

    private var mode: Int = NONE

    private var start: PointF = PointF()
    private var mid: PointF = PointF()
    private var oldDist: Float = 1f

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        val imageView: ImageView = v as ImageView
        when (event?.action?.and(MotionEvent.ACTION_MASK)) {
            MotionEvent.ACTION_DOWN -> {
                matrix.set(imageView.imageMatrix)
                sMatrix.set(matrix)
                start.set(event.x, event.y)
                mode = DRAG
            }
            MotionEvent.ACTION_POINTER_DOWN -> {
                oldDist = spacing(event)
                if (oldDist > 10f) {
                    sMatrix.set(matrix)
                    midpoint(mid, event)
                    mode = ZOOM
                }
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_POINTER_UP -> {
                mode = NONE
            }
            MotionEvent.ACTION_MOVE -> {
                when (mode) {
                    DRAG -> {
                        matrix.set(sMatrix)
                        matrix.postTranslate(event.x - start.x, event.y - start.y)
                    }
                    ZOOM -> {
                        val newDist = spacing(event)
                        if (newDist > 10f) {
                            matrix.set(sMatrix)
                            val scale: Float = newDist / oldDist
                            matrix.postScale(scale, scale, mid.x, mid.y)
                        }
                    }
                }
            }
        }
        imageView.imageMatrix = matrix
        return true
    }

    private fun spacing(event: MotionEvent): Float {
        var x = event.getX(0) - event.getX(1)
        var y = event.getY(0) - event.getY(1)

        return Math.sqrt((x * x + y * y).toDouble()).toFloat()
    }


    private fun midpoint(point: PointF, event: MotionEvent) {
        val x = event.getX(0) + event.getX(1)
        val y = event.getY(0) + event.getY(1)
        point.set(x / 2, y / 2)
    }

}