package com.samwoo.istudy.util

import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.text.Spannable
import android.text.SpannableString
import android.text.style.CharacterStyle
import android.view.ViewGroup
import android.webkit.WebView
import com.just.agentweb.AgentWeb
import com.just.agentweb.DefaultWebClient
import com.just.agentweb.WebChromeClient
import com.just.agentweb.WebViewClient
import com.samwoo.istudy.R
import java.text.SimpleDateFormat
import java.util.*


/**
 * 扩展String属性
 */
fun String.getAgentWeb(
    activity: Activity,
    webContent: ViewGroup,
    layoutParams: ViewGroup.LayoutParams,
    webView: WebView,
    webChromeClient: WebChromeClient?,
    webViewClient: WebViewClient
) = AgentWeb.with(activity) //传入Activity or Fragment
    .setAgentWebParent(webContent, layoutParams) //传入AgentWeb 的父控件
    .useDefaultIndicator() // 使用默认进度条
    .setWebView(webView)
    .setWebChromeClient(webChromeClient)
    .setWebViewClient(webViewClient)
    .setMainFrameErrorView(R.layout.agentweb_error_page, -1)
    .setOpenOtherPageWays(DefaultWebClient.OpenOtherPageWays.ASK) //打开其他应用时，弹窗咨询用户是否前往其他应用
    .interceptUnkownUrl() //拦截找不到相关页面的Scheme
    .createAgentWeb()
    .ready()
    .go(this)

/**
 * 高亮特点字符串
 */
fun String.highlight(
    key: String,
    style: CharacterStyle
): SpannableString {
    val spanText = SpannableString(this)
    val beginPos = this.indexOf(key)
    val endPos = beginPos + key.length
    spanText.setSpan(style, beginPos, endPos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    return spanText
}

/**
 * 格式化当前日期时间
 */
fun formatCurrentDate(): String {
    val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    return formatter.format(Date())
}

/**
 * 获取随机rgb颜色值
 */
fun randomColor(): Int {
    val random = Random()
    val COLOR_VALUE: Int = 240
    var red = random.nextInt(COLOR_VALUE)
    var green = random.nextInt(COLOR_VALUE)
    var blue = random.nextInt(COLOR_VALUE / 2)

    return Color.rgb(red, green, blue)
}

/**
 * 随机获取标签背景渐变颜色
 */
fun getBackGround(): Drawable {
    val colors = intArrayOf(randomColor(), randomColor())
//        val colors=listOf(
//            intArrayOf(Color.parseColor("#373B44"),Color.parseColor("#4286f4")),
//            intArrayOf(Color.parseColor("#b92b27"),Color.parseColor("#1565C0")),
//            intArrayOf(Color.parseColor("#bdc3c7"),Color.parseColor("#2c3e50"))
//        )
    var drawable = GradientDrawable()
    drawable.apply {
        orientation = GradientDrawable.Orientation.LEFT_RIGHT
        cornerRadius = 8f
        gradientType = GradientDrawable.LINEAR_GRADIENT
//            setColors(colors[Random().nextInt(colors.size-1)])
        setColors(colors)
    }
    return drawable
}

