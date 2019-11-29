package com.samwoo.istudy

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import com.samwoo.istudy.constant.Constant
import com.samwoo.istudy.util.*
import org.jetbrains.anko.doAsync
import org.litepal.LitePal
import java.io.IOException
import java.net.URL
import kotlin.properties.Delegates

class App : Application() {
    private var picUrl: String by Preference(Constant.PIC_URL, "")

    companion object {
        lateinit var instance: Application

        var context: Context by Delegates.notNull()
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        context = applicationContext
        //获取splash背景图片url
        if (NetworkUtil.isNetworkConnected(context)) {
            doAsync {
                val imgUrl = URL(Constant.SPLASH_PIC_URL).readText()
                if (imgUrl.isNotEmpty() && !imgUrl.equals(picUrl)) picUrl = imgUrl
            }
        }
        // LitePal
        LitePal.initialize(this)
        //
        initTheme()
        DisplayManager.init(this)
//        SLog.d("screen.w-->${DisplayManager.getScreenWidth()} / screen.h-->${DisplayManager.getScreenHeight()}")
    }

    private fun initTheme() {
        AppCompatDelegate.setDefaultNightMode(
            if (SettingUtil.getIsNightMode()) AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_NO
        )
    }
}