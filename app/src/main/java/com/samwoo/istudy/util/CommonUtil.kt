package com.samwoo.istudy.util

import android.app.Activity
import android.graphics.Color
import android.view.ViewGroup
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
    webChromeClient: WebChromeClient?,
    webViewClient: WebViewClient
) = AgentWeb.with(activity) //传入Activity or Fragment
    .setAgentWebParent(webContent, layoutParams) //传入AgentWeb 的父控件
    .useDefaultIndicator() // 使用默认进度条
    .setWebChromeClient(webChromeClient)
    .setWebViewClient(webViewClient)
    .setMainFrameErrorView(R.layout.agentweb_error_page, -1)
    .setOpenOtherPageWays(DefaultWebClient.OpenOtherPageWays.ASK) //打开其他应用时，弹窗咨询用户是否前往其他应用
    .interceptUnkownUrl() //拦截找不到相关页面的Scheme
    .createAgentWeb()
    .ready()
    .go(this)

/**
 * 格式化当前日期
 */
fun formatCurrentDate(): String {
    val formatter = SimpleDateFormat("yyyy-MM-dd")
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
    var blue = random.nextInt(COLOR_VALUE)

    return Color.rgb(red, green, blue)
}