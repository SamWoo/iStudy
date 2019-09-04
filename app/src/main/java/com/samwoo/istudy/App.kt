package com.samwoo.istudy

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import com.samwoo.istudy.util.DisplayManager
import com.samwoo.istudy.util.SettingUtil
import org.litepal.LitePal
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
    }

    private fun initTheme() {
        AppCompatDelegate.setDefaultNightMode(
            if (SettingUtil.getIsNightMode()) AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_NO)
    }
}