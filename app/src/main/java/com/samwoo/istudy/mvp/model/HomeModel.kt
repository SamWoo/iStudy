package com.samwoo.istudy.mvp.model

import android.util.Log
import com.samwoo.istudy.bean.ArticlesListBean
import com.samwoo.istudy.bean.Banner
import com.samwoo.istudy.bean.BannerList
import com.samwoo.istudy.bean.HttpResult
import com.samwoo.istudy.callback.Callback
import com.samwoo.istudy.util.NetUtil
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

class HomeModel {

    //获取Banner
    fun getBanners(callback: Callback<BannerList, String>) {
        NetUtil.service.getBanners()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Subscriber<BannerList>() {
                override fun onNext(bean: BannerList?) {
                    Log.d("Sam","-------->${bean}")
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
                    Log.d("Sam","-------->${e}")
                }

            })
    }

    //获取文章列表
    fun getArticles(curPage: Int, callback: Callback<HttpResult<ArticlesListBean>, String>) {
        NetUtil.service.getArticles(curPage)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Subscriber<HttpResult<ArticlesListBean>>() {
                override fun onNext(bean: HttpResult<ArticlesListBean>?) {
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
                }

            })
    }
}
