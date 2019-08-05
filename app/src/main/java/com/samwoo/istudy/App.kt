package com.samwoo.istudy

import android.app.Application
import android.content.Context
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
    }
}