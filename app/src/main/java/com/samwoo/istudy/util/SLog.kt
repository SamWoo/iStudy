package com.samwoo.istudy.util

import android.util.Log
import com.samwoo.istudy.BuildConfig

object SLog {
    private val TAG = "Sam"

    //debug
    fun d(msg: String) {
        if (BuildConfig.DEBUG) Log.d(TAG, msg)
    }

    fun d(tag: String, msg: String) {
        if (BuildConfig.DEBUG) Log.d(tag, msg)
    }

    //info
    fun i(msg: String) {
        if (BuildConfig.DEBUG) Log.i(TAG, msg)
    }

    fun i(tag: String, msg: String) {
        if (BuildConfig.DEBUG) Log.i(tag, msg)
    }

    //error
    fun e(msg: String) {
        if (BuildConfig.DEBUG) Log.e(TAG, msg)
    }

    fun e(tag: String, msg: String) {
        if (BuildConfig.DEBUG) Log.e(tag, msg)
    }

    //verbose
    fun v(msg: String) {
        if (BuildConfig.DEBUG) Log.v(TAG, msg)
    }

    fun v(tag: String, msg: String) {
        if (BuildConfig.DEBUG) Log.v(tag, msg)
    }

    //warn
    fun w(msg: String) {
        if (BuildConfig.DEBUG) Log.w(TAG, msg)
    }

    fun w(tag: String, msg: String) {
        if (BuildConfig.DEBUG) Log.w(tag, msg)
    }
}