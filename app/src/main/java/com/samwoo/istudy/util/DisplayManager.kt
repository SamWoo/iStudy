package com.samwoo.istudy.util

import android.content.Context
import android.util.DisplayMetrics
import android.util.TypedValue


object DisplayManager {

    private var displayMetrics: DisplayMetrics? = null

    private var screenWidth: Int? = null

    private var screenHeight: Int? = null

    private var screenDpi: Int? = null

    fun init(context: Context) {
        displayMetrics = context.resources.displayMetrics
        screenWidth = displayMetrics?.widthPixels
        screenHeight = displayMetrics?.heightPixels
        screenDpi = displayMetrics?.densityDpi
    }


    //UI图的大小
    private val STANDARD_WIDTH = 1080
    private val STANDARD_HEIGHT = 1920


    fun getScreenWidth(): Int? {
        return screenWidth
    }

    fun getScreenHeight(): Int? {
        return screenHeight
    }


    /**
     * 传入UI图中问题的高度，单位像素
     * @param size
     * @return
     */
    fun getPaintSize(size: Int): Int? {
        return getRealHeight(size)
    }

    /**
     * 输入UI图的尺寸，输出实际的px
     *
     * @param px ui图中的大小
     * @return
     */
    fun getRealWidth(px: Int): Int? {
        //ui图的宽度
        return getRealWidth(px, STANDARD_WIDTH.toFloat())
    }

    /**
     * 输入UI图的尺寸，输出实际的px,第二个参数是父布局
     *
     * @param px          ui图中的大小
     * @param parentWidth 父view在ui图中的高度
     * @return
     */
    fun getRealWidth(px: Int, parentWidth: Float): Int? {
        return (px / parentWidth * getScreenWidth()!!).toInt()
    }

    /**
     * 输入UI图的尺寸，输出实际的px
     *
     * @param px ui图中的大小
     * @return
     */
    fun getRealHeight(px: Int): Int? {
        //ui图的宽度
        return getRealHeight(px, STANDARD_HEIGHT.toFloat())
    }

    /**
     * 输入UI图的尺寸，输出实际的px,第二个参数是父布局
     *
     * @param px           ui图中的大小
     * @param parentHeight 父view在ui图中的高度
     * @return
     */
    fun getRealHeight(px: Int, parentHeight: Float): Int? {
        return (px / parentHeight * getScreenHeight()!!).toInt()
    }

    /**
     * dp转px
     * @param dipValue
     * @return int
     */
    fun dip2px(dipValue: Float): Int {
        return (dipValue * displayMetrics!!.density + 0.5f).toInt()
    }

    fun px2dip(pxValue: Float): Int {
        return (pxValue / displayMetrics!!.density + 0.5f).toInt()
    }

    fun dp2px(dp: Int): Int {
        return (0.5f+dp.toFloat()*displayMetrics!!.density).toInt()
    }

    fun px2dp(pxValue: Float): Int {
        return (pxValue / displayMetrics!!.density + 0.5f).toInt()
    }

    fun px2sp(pxValue: Float): Int {
        return (pxValue / displayMetrics!!.scaledDensity + 0.5f).toInt()
    }

    fun px2sp(size: Int): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_PX,
            size.toFloat(),
            displayMetrics
        )
    }

    fun sp2px(spValue: Float): Int {
        return (spValue * displayMetrics!!.scaledDensity + 0.5f).toInt()
    }

    fun sp2px(size: Int): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP,
            size.toFloat(),
            displayMetrics
        )
    }
}