package com.samwoo.istudy.mvp.model

import com.samwoo.istudy.bean.ArticlesListBean
import com.samwoo.istudy.bean.HttpResult
import com.samwoo.istudy.callback.Callback
import com.samwoo.istudy.util.RequestUtil
import com.samwoo.istudy.util.SLog
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

class CollectModel {
    fun getCollectList(page: Int, callback: Callback<HttpResult<ArticlesListBean>, String>) {
        RequestUtil.service.getCollectList(page)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(object : Subscriber<HttpResult<ArticlesListBean>>() {
                override fun onNext(t: HttpResult<ArticlesListBean>?) {
                    SLog.d("result--->$t")
                    when {
                        t == null -> callback.onFail("数据为空!")
                        t.errorCode != 0 -> callback.onFail("errorMsg-->${t.errorMsg}")
                        else -> callback.onSuccess(t)
                    }
                }

                override fun onCompleted() {
                }

                override fun onError(e: Throwable?) {
                    callback.onFail("$e")
                }
            })
    }

    fun addCollectArticle(id: Int, callback: Callback<HttpResult<Any>, String>) {
        RequestUtil.service.addCollectArticle(id)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(object : Subscriber<HttpResult<Any>>() {
                override fun onNext(t: HttpResult<Any>?) {
                    SLog.d("result--->$t")
                    when {
                        t == null -> callback.onFail("数据为空!")
                        t.errorCode != 0 -> callback.onFail("errorMsg-->${t.errorMsg}")
                        else -> callback.onSuccess(t)
                    }
                }

                override fun onCompleted() {
                }

                override fun onError(e: Throwable?) {
                    callback.onFail("$e")
                }
            })
    }

    fun addExtraCollectArticle(
        title: String,
        author: String,
        link: String,
        callback: Callback<HttpResult<Any>, String>
    ) {
        RequestUtil.service.addExtraCollectArticle(title, author, link)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(object : Subscriber<HttpResult<Any>>() {
                override fun onNext(t: HttpResult<Any>?) {
                    SLog.d("result--->$t")
                    when {
                        t == null -> callback.onFail("数据为空!")
                        t.errorCode != 0 -> callback.onFail("errorMsg-->${t.errorMsg}")
                        else -> callback.onSuccess(t)
                    }
                }

                override fun onCompleted() {
                }

                override fun onError(e: Throwable?) {
                    callback.onFail("$e")
                }
            })
    }

    fun cancleCollectArticle(id: Int, callback: Callback<HttpResult<Any>, String>) {
        RequestUtil.service.cancelCollectArticle(id)
            .subscribeOn(Schedulers.io())
            .subscribe(object : Subscriber<HttpResult<Any>>() {
                override fun onNext(t: HttpResult<Any>?) {
                    SLog.d("result--->$t")
                    when {
                        t == null -> callback.onFail("数据为空!")
                        t.errorCode != 0 -> callback.onFail("errorMsg-->${t.errorMsg}")
                        else -> callback.onSuccess(t)
                    }
                }

                override fun onCompleted() {
                }

                override fun onError(e: Throwable?) {
                    callback.onFail("$e")
                }
            })
    }

    fun removeCollectArticle(id: Int, originId: Int, callback: Callback<HttpResult<Any>, String>) {
        RequestUtil.service.removeCollectArticle(id, originId)
            .subscribeOn(AndroidSchedulers.mainThread())
            .observeOn(Schedulers.io())
            .subscribe(object : Subscriber<HttpResult<Any>>() {
                override fun onNext(t: HttpResult<Any>?) {
                    SLog.d("result--->$t")
                    when {
                        t == null -> callback.onFail("数据为空!")
                        t.errorCode != 0 -> callback.onFail("errorMsg-->${t.errorMsg}")
                        else -> callback.onSuccess(t)
                    }
                }

                override fun onCompleted() {
                }

                override fun onError(e: Throwable?) {
                    callback.onFail("$e")
                }
            })
    }
}