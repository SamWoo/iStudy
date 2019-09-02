package com.samwoo.istudy.constant

import androidx.annotation.IntDef

object HostType {
    /**
     * 多少种Host类型
     */
    const val TYPE_COUNT = 2

    /**
     * 网易新闻视频的host
     */
    const val WAN_ANDROID = 1

    /**
     * 新浪图片的host
     */
    const val GANK_GIRL_PHOTO = 2


    /**
     * 替代枚举的方案，使用IntDef保证类型安全
     */
    @IntDef(WAN_ANDROID, GANK_GIRL_PHOTO)
    @Retention(AnnotationRetention.SOURCE)
    annotation class HostTypeChecker
}