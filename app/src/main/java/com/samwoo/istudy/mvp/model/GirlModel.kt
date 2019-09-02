package com.samwoo.istudy.mvp.model

import com.samwoo.istudy.bean.GankBody
import com.samwoo.istudy.callback.Callback
import com.samwoo.istudy.constant.HostType
import com.samwoo.istudy.util.RetrofitManager
import com.samwoo.istudy.util.SLog
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

class GirlModel {
    fun getGirlPhoto(page: Int, callback: Callback<GankBody, String>) {
        RetrofitManager.getService(HostType.GANK_GIRL_PHOTO).getGirlPhoto(page)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(object : Subscriber<GankBody>() {
                override fun onNext(t: GankBody?) {
                    SLog.d("girl---->$t")
                    when (t?.error) {
                        true -> callback.onFail("数据为空!!")
                        else -> callback.onSuccess(t!!)
                    }
                }

                override fun onCompleted() {
                }

                override fun onError(e: Throwable?) {
                    SLog.d("-------->${e}")
                    callback.onFail("数据为空!!")
                }

            })
    }
}