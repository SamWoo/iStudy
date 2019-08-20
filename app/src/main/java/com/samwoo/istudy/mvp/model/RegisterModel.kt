package com.samwoo.istudy.mvp.model

import android.util.Log
import com.samwoo.istudy.BuildConfig
import com.samwoo.istudy.bean.HttpResult
import com.samwoo.istudy.bean.LoginData
import com.samwoo.istudy.callback.Callback
import com.samwoo.istudy.util.RequestUtil
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

class RegisterModel {
    fun register(
        username: String,
        password: String,
        repassword: String,
        callback: Callback<HttpResult<LoginData>, String>
    ) {
        RequestUtil.service.register(username, password, repassword)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(object : Subscriber<HttpResult<LoginData>>() {
                override fun onNext(t: HttpResult<LoginData>?) {
                    if (BuildConfig.DEBUG) Log.d("Sam", "$t")
                    when {
                        t == null -> callback.onFail("空数据!!")
                        t.errorCode != 0 -> callback.onFail("errorMsg=${t.errorMsg}")
                        else -> callback.onSuccess(t)
                    }

                }

                override fun onCompleted() {
                }

                override fun onError(e: Throwable?) {
                    if (BuildConfig.DEBUG) Log.d("Sam", "$e")
                    callback.onFail(e.toString())
                }
            })
    }
}