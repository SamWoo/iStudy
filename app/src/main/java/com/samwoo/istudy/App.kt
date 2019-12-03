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

    companion object {
        lateinit var instance: Application

        var context: Context by Delegates.notNull()
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        context = applicationContext
        // LitePal
        LitePal.initialize(this)
        //
        initTheme()
        DisplayManager.init(this)
        //未捕获异常信息抓取
        CrashHandler.getInstance()!!.init(this)

//        SLog.d("screen.w-->${DisplayManager.getScreenWidth()} / screen.h-->${DisplayManager.getScreenHeight()}")
    }

    private fun initTheme() {
        AppCompatDelegate.setDefaultNightMode(
            if (SettingUtil.getIsNightMode()) AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_NO
        )
    }
}