package com.samwoo.istudy.mvp.model

import android.util.Log
import com.samwoo.istudy.BuildConfig
import com.samwoo.istudy.bean.ArticlesListBean
import com.samwoo.istudy.bean.HotKey
import com.samwoo.istudy.bean.HttpResult
import com.samwoo.istudy.callback.Callback
import com.samwoo.istudy.util.RequestUtil
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

class SearchModel {
    // 获取当前搜索热词
    fun getHotKey(callback: Callback<HttpResult<List<HotKey>>, String>) {
        RequestUtil.service.getHotKey()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(object : Subscriber<HttpResult<List<HotKey>>>() {
                override fun onNext(result: HttpResult<List<HotKey>>?) {
                    if (BuildConfig.DEBUG) Log.d("Sam", "-------->${result}")
                    when {
                        result == null -> callback.onFail("Error!!")
                        result.errorCode != 0 -> callback.onFail("errorCode=" + result.errorCode)
                        else -> callback.onSuccess(result)
                    }
                }

                override fun onCompleted() {
                }

                override fun onError(e: Throwable?) {
                    Log.d("Sam", "-------->${e}")
                }
            })
    }

    // 根据关键字搜索
    fun queryByKey(page: Int, key: String, callback: Callback<HttpResult<ArticlesListBean>, String>) {
        RequestUtil.service.queryByKey(page, key)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(object : Subscriber<HttpResult<ArticlesListBean>>() {
                override fun onNext(result: HttpResult<ArticlesListBean>?) {
                    if (BuildConfig.DEBUG) Log.d("Sam", "$result")
                    when {
                        result == null -> callback.onFail("数据为空!")
                        result.errorCode != 0 -> callback.onFail("Bad Request!! errorCode=${result.errorCode}")
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