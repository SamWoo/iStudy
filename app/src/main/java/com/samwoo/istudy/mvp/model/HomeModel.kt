package com.samwoo.istudy.mvp.model

import android.util.Log
import com.samwoo.istudy.BuildConfig
import com.samwoo.istudy.bean.ArticlesListBean
import com.samwoo.istudy.bean.BannerList
import com.samwoo.istudy.bean.HttpResult
import com.samwoo.istudy.callback.Callback
import com.samwoo.istudy.util.RequestUtil
import com.samwoo.istudy.util.SLog
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

class HomeModel {

    //获取Banner
    fun getBanners(callback: Callback<BannerList, String>) {
        RequestUtil.service.getBanners()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Subscriber<BannerList>() {
                override fun onNext(result: BannerList?) {
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

    //获取文章列表
    fun getArticles(curPage: Int, callback: Callback<HttpResult<ArticlesListBean>, String>) {
        RequestUtil.service.getArticles(curPage)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Subscriber<HttpResult<ArticlesListBean>>() {
                override fun onNext(result: HttpResult<ArticlesListBean>?) {
                    when {
                        result == null -> callback.onFail("Error!!")
                        result.errorCode != 0 -> callback.onFail("errorCode=" + result.errorCode)
                        else -> callback.onSuccess(result)
                    }
                }

                override fun onCompleted() {
                    SLog.d("request comleted!!!")
                }

                override fun onError(e: Throwable?) {
                    callback.onFail(e.toString())
                }
            })
    }
}
