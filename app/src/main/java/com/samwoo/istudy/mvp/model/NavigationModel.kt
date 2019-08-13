package com.samwoo.istudy.mvp.model

import android.util.Log
import com.samwoo.istudy.BuildConfig
import com.samwoo.istudy.bean.HttpResult
import com.samwoo.istudy.bean.NavigationBean
import com.samwoo.istudy.callback.Callback
import com.samwoo.istudy.util.RequestUtil
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

class NavigationModel {
    fun getNavList(callback: Callback<HttpResult<List<NavigationBean>>, String>) {
        RequestUtil.service.getNavList()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(object : Subscriber<HttpResult<List<NavigationBean>>>() {
                override fun onNext(t: HttpResult<List<NavigationBean>>?) {
                    if (BuildConfig.DEBUG) Log.d("Sam", "NavList--->${t}")
                    when {
                        t == null -> callback.onFail("数据为空")
                        t.errorCode != 0 -> callback.onFail("errorMsg=${t.errorMsg}")
                        else -> callback.onSuccess(t)
                    }
                }

                override fun onCompleted() {
                }

                override fun onError(e: Throwable?) {
                    if (BuildConfig.DEBUG) Log.d("Sam", "error----->${e}")
                }

            })
    }
}