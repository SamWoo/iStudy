package com.samwoo.istudy.mvp.presenter

import com.samwoo.istudy.base.BasePresenter
import com.samwoo.istudy.bean.HttpResult
import com.samwoo.istudy.bean.LoginData
import com.samwoo.istudy.callback.Callback
import com.samwoo.istudy.mvp.contract.RegisterContract
import com.samwoo.istudy.mvp.model.RegisterModel

class RegisterPresenter : BasePresenter<RegisterContract.View>(), RegisterContract.Presenter {

    private val model: RegisterModel by lazy {
        RegisterModel()
    }

    override fun register(username: String, password: String, repassword: String) {
        mView?.showLoading()
        model.register(username, password, repassword, object : Callback<HttpResult<LoginData>, String> {
            override fun onSuccess(data: HttpResult<LoginData>) {
                if (isViewAttached()) {
                    mView?.run {
                        registerSuccess(data.data)
                        hideLoading()
                    }
                }
            }

            override fun onFail(msg: String) {
                if (isViewAttached()) {
                    mView?.run {
                        showError(msg)
                        hideLoading()
                    }
                }
            }

        })
    }

}