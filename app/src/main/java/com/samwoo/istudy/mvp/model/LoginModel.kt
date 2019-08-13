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

class LoginModel {
    fun login(username: String, password: String, callback: Callback<HttpResult<LoginData>, String>) {
        RequestUtil.service.login(username, password)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(object : Subscriber<HttpResult<LoginData>>() {
                override fun onNext(result: HttpResult<LoginData>?) {
                    if (BuildConfig.DEBUG) Log.d("Sam", "$result")
                    when {
                        result == null -> callback.onFail("空数据!!")
                        result.errorCode != 0 -> callback.onFail("errorMsg=${result.errorMsg}")
                        else -> callback.onSuccess(result)
                    }
                }

                override fun onCompleted() {
                }

                override fun onError(e: Throwable?) {
                    if (BuildConfig.DEBUG) Log.d("Sam", "$e")
                }

            })
    }
}