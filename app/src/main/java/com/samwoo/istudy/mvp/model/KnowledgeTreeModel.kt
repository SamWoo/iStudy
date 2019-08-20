package com.samwoo.istudy.mvp.model

import android.util.Log
import com.samwoo.istudy.bean.HttpResult
import com.samwoo.istudy.bean.KnowledgeTreeBody
import com.samwoo.istudy.callback.Callback
import com.samwoo.istudy.util.RequestUtil
import com.samwoo.istudy.util.SLog
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

class KnowledgeTreeModel {
    //获取知识体系
    fun getKnowledgeTree(callback: Callback<HttpResult<List<KnowledgeTreeBody>>, String>) {
        RequestUtil.service.getKnowledgeTree()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(object : Subscriber<HttpResult<List<KnowledgeTreeBody>>>() {
                override fun onNext(result: HttpResult<List<KnowledgeTreeBody>>?) {
//                    SLog.d("-------->${result}")
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