package com.samwoo.istudy.callback

interface Callback<K, V> {
    fun onSuccess(data: K)
    fun onFail(msg: V)
}