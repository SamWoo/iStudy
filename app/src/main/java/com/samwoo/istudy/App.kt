package com.samwoo.istudy

import android.app.Application
import android.content.Context
import kotlin.properties.Delegates

class App :Application(){
    companion object{
        lateinit var instance:Application
        var context: Context by Delegates.notNull()
    }

    override fun onCreate() {
        super.onCreate()
        instance=this

    }
}