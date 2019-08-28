package com.samwoo.istudy.util

import android.preference.PreferenceManager
import com.samwoo.istudy.App

object SettingUtil {
    private val setting=PreferenceManager.getDefaultSharedPreferences(App.context)

    //设置夜间模式
    fun setIsNightMode(flag:Boolean){
        setting.edit().putBoolean("switch_night_mode",flag).apply()
    }

    //获取是否开启夜间模式
    fun getIsNightMode():Boolean{
        return setting.getBoolean("switch_night_mode",false)
    }
}