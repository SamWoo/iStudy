package com.samwoo.istudy.mvp.model

import android.util.Log
import com.samwoo.istudy.bean.HttpResult
import com.samwoo.istudy.bean.KnowledgeTreeBody
import com.samwoo.istudy.callback.Callback
import com.samwoo.istudy.util.NetUtil
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

class KnowledgeTreeModel {
    //获取知识体系
    fun getKnowledgeTree(callback: Callback<HttpResult<List<KnowledgeTreeBody>>, String>) {
        NetUtil.service.getKnowledgeTree()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(object : Subscriber<HttpResult<List<KnowledgeTreeBody>>>() {
                override fun onNext(bean: HttpResult<List<KnowledgeTreeBody>>?) {
                    Log.d("Sam", "-------->${bean}")
                    if (bean == null) {
                        callback.onFail("Error!!")
                    } else if (bean.errorCode != 0) {
                        callback.onFail("errorCode=" + bean.errorCode)
                    } else {
                        callback.onSuccess(bean)
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