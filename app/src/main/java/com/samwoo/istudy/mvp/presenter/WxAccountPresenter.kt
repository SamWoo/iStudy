package com.samwoo.istudy.mvp.presenter

import com.samwoo.istudy.base.BasePresenter
import com.samwoo.istudy.bean.HttpResult
import com.samwoo.istudy.bean.WxAccountBody
import com.samwoo.istudy.callback.Callback
import com.samwoo.istudy.mvp.contract.WxAccountContract
import com.samwoo.istudy.mvp.model.WxAccountModel
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class WxAccountPresenter : BasePresenter<WxAccountContract.View>(), WxAccountContract.Presenter {
    private val wxAccountModel: WxAccountModel by lazy {
        WxAccountModel()
    }

    override fun getWxAccount() {
        mView?.showLoading()
        wxAccountModel.getWxAccount(object : Callback<HttpResult<List<WxAccountBody>>, String> {
            override fun onSuccess(result: HttpResult<List<WxAccountBody>>) {
                if (isViewAttached()) {
                    mView?.apply {
                        hideLoading()
                        setWxAccount(result.data)
                    }
                }
            }

            override fun onFail(msg: String) {
                if (isViewAttached()) {
                    doAsync {
                        //休眠2s模拟加载过程
                        for (ratio in 0..10) Thread.sleep(200)
                        ////处理完成，回到主线程在界面上显示
                        uiThread {
                            mView?.apply {
                                hideLoading()
                                showError(msg)
                            }
                        }
                    }
                }
            }
        })
    }
}