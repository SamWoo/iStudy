package com.samwoo.istudy.mvp.presenter

import com.samwoo.istudy.base.BasePresenter
import com.samwoo.istudy.bean.GankBody
import com.samwoo.istudy.callback.Callback
import com.samwoo.istudy.mvp.contract.GirlContract
import com.samwoo.istudy.mvp.model.GirlModel

class GirlPresenter : BasePresenter<GirlContract.View>(), GirlContract.Presenter {
    private val model: GirlModel by lazy { GirlModel() }

    override fun getGirlPhoto(page: Int) {
        model.getGirlPhoto(page, object : Callback<GankBody, String> {
            override fun onSuccess(data: GankBody) {
                if (isViewAttached()) {
                    mView?.showGirlPhoto(data.results)
                }
            }

            override fun onFail(msg: String) {
                if (isViewAttached()) {
                    mView?.showError(msg)
                }
            }

        })
    }

}