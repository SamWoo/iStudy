package com.samwoo.istudy.util

import android.content.Context
import android.content.SharedPreferences
import com.samwoo.istudy.App
import com.samwoo.istudy.constant.Constant
import java.lang.IllegalArgumentException
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class Preference<T>(val name: String, val default: T) : ReadWriteProperty<Any?, T> {

    companion object {
        private val fileName = "istudy_file"
        //通过属性代理初始化共享参数对象
        val prefs: SharedPreferences by lazy {
            App.context.getSharedPreferences(
                fileName,
                Context.MODE_PRIVATE
            )
        }

        //删除全部配置数据
        fun clearPreference() {
            prefs.edit().clear().apply()
        }

        //根据键值删除属性值
        fun clearPreference(key: String) {
            prefs.edit().remove(key).apply()
        }

        //删除cookie
        fun deleteCookie() {
            clearPreference(Constant.LOGIN_URL_KEY)
            clearPreference(Constant.REGISTER_URL_KEY)
            clearPreference(Constant.DOMAIN_KEY)
        }
    }

    //接管属性值得获取行为
    override fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return findPreference(name, default)
    }

    //接管属性值得修改行为
    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        putPreference(name, value)
    }

    //利用with函数定义临时的命名空间
    private fun <T> findPreference(name: String, default: T): T = with(prefs) {
        val res: Any = when (default) {
            is Long -> getLong(name, default)
            is String -> getString(name, default)
            is Int -> getInt(name, default)
            is Boolean -> getBoolean(name, default)
            is Float -> getFloat(name, default)
            else -> throw IllegalArgumentException("This type can be saved into Preferences")
        }
        return res as T
    }


    private fun <T> putPreference(name: String, value: T) = with(prefs.edit()) {
        when (value) {
            is Long -> putLong(name, value)
            is String -> putString(name, value)
            is Int -> putInt(name, value)
            is Boolean -> putBoolean(name, value)
            is Float -> putFloat(name, value)
            else -> throw IllegalArgumentException("This type can be saved into Preferences")
        }.apply() //commit方法和apply方法都表示提交修改
    }

}