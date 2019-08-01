package com.samwoo.istudy.mvp.presenter

import com.samwoo.istudy.base.BasePresenter
import com.samwoo.istudy.bean.HttpResult
import com.samwoo.istudy.bean.WxAccountBody
import com.samwoo.istudy.callback.Callback
import com.samwoo.istudy.mvp.contract.WxAccountContract
import com.samwoo.istudy.mvp.model.WxAccountModel

class WxAccountPresenter : BasePresenter<WxAccountContract.View>(), WxAccountContract.Presenter {
    private val wxAccountModel: WxAccountModel by lazy {
        WxAccountModel()
    }

    override fun getWxAccount() {
        mView?.showLoading()
        wxAccountModel.getWxAccount(object : Callback<HttpResult<List<WxAccountBody>>, String> {
            override fun onSuccess(result: HttpResult<List<WxAccountBody>>) {
                if (isViewAttached()) {
                    mView?.run {
                        setWxAccount(result.data)
                        hideLoading()
                    }
                }
            }

            override fun onFail(msg: String) {
                if (isViewAttached()) {
                    mView?.apply {
                        showError(msg)
                        hideLoading()
                    }
                }
            }
        })
    }

}