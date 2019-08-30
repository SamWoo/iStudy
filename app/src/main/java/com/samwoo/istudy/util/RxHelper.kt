package com.samwoo.istudy.util

import com.samwoo.istudy.bean.HttpResult
import com.samwoo.istudy.callback.Callback
import rx.Observable
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers


fun <T> Observable<HttpResult<T>>.handle(callback: Callback<HttpResult<T>, String>) {
    this.subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(object : Subscriber<HttpResult<T>>() {
            override fun onNext(result: HttpResult<T>?) {
                SLog.d("-------->${result}")
                when {
                    result == null -> callback.onFail("数据为空!!")
                    result.errorCode != 0 -> callback.onFail("errorMsg=${result.errorMsg}")
                    else -> callback.onSuccess(result)
                }
            }

            override fun onCompleted() {
            }

            override fun onError(e: Throwable?) {
                SLog.d("-------->${e}")
                callback.onFail(e.toString())
            }
        })
}
