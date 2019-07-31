package com.samwoo.istudy.mvp.model

import android.util.Log
import com.samwoo.istudy.BuildConfig
import com.samwoo.istudy.bean.ArticlesListBean
import com.samwoo.istudy.bean.HttpResult
import com.samwoo.istudy.callback.Callback
import com.samwoo.istudy.util.NetUtil
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

class ArticlesModel {
    fun getArticles(id:Int, curPage:Int, callback: Callback<HttpResult<ArticlesListBean>, String>){
        NetUtil.service.getArticles(id, curPage)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(object :Subscriber<HttpResult<ArticlesListBean>>(){
                override fun onNext(result: HttpResult<ArticlesListBean>?) {
                    if (BuildConfig.DEBUG) Log.d("Sam", "wxarticles-------->${result}")
                    if (result == null) {
                        callback.onFail("Error!!")
                    } else if (result.errorCode != 0) {
                        callback.onFail("errorCode=" + result.errorCode)
                    } else {
                        callback.onSuccess(result)
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