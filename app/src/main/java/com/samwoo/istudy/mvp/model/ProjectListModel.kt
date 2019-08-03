package com.samwoo.istudy.mvp.model

import android.util.Log
import com.samwoo.istudy.BuildConfig
import com.samwoo.istudy.bean.ArticlesListBean
import com.samwoo.istudy.bean.HttpResult
import com.samwoo.istudy.callback.Callback
import com.samwoo.istudy.util.RequestUtil
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

class ProjectListModel {
    fun getProjectList(curPage: Int, cid: Int, callback: Callback<HttpResult<ArticlesListBean>, String>) {
        RequestUtil.service.getProjectList(curPage, cid)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(object : Subscriber<HttpResult<ArticlesListBean>>() {
                override fun onNext(bean: HttpResult<ArticlesListBean>?) {
                    if (BuildConfig.DEBUG) Log.d("Sam", "project-------->${bean}")
                    when {
                        bean == null -> callback.onFail("Error!!")
                        bean.errorCode != 0 -> callback.onFail("errorCode=" + bean.errorCode)
                        else -> callback.onSuccess(bean)
                    }
                }

                override fun onCompleted() {
                }

                override fun onError(e: Throwable?) {
                    Log.d("Sam", "-------->${e}")
                }

            })
    }
}