package com.samwoo.istudy.mvp.model

import android.util.Log
import com.samwoo.istudy.BuildConfig
import com.samwoo.istudy.bean.ArticlesListBean
import com.samwoo.istudy.bean.HttpResult
import com.samwoo.istudy.callback.Callback
import com.samwoo.istudy.util.RequestUtil
import com.samwoo.istudy.util.SLog
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

class ArticlesModel {
    fun getArticleList(curPage: Int, id: Int, callback: Callback<HttpResult<ArticlesListBean>, String>) {
        RequestUtil.service.getArticleList(curPage, id)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(object : Subscriber<HttpResult<ArticlesListBean>>() {
                override fun onNext(result: HttpResult<ArticlesListBean>?) {
//                    SLog.d("wxarticles-------->${result}")
                    when {
                        result == null -> callback.onFail("Error!!")
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
}