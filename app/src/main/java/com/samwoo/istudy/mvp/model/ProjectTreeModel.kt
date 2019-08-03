package com.samwoo.istudy.mvp.model

import android.util.Log
import com.samwoo.istudy.BuildConfig
import com.samwoo.istudy.bean.HttpResult
import com.samwoo.istudy.bean.ProjectTreeBody
import com.samwoo.istudy.callback.Callback
import com.samwoo.istudy.util.RequestUtil
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

class ProjectTreeModel {
    fun getProjectTree(callback: Callback<HttpResult<List<ProjectTreeBody>>, String>) {
        RequestUtil.service.getProjectTree()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(object : Subscriber<HttpResult<List<ProjectTreeBody>>>() {
                override fun onNext(bean: HttpResult<List<ProjectTreeBody>>?) {
                    if (BuildConfig.DEBUG) Log.d("Sam", "-------->${bean}")
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