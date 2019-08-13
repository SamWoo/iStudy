package com.samwoo.istudy.mvp.presenter

import com.samwoo.istudy.base.BasePresenter
import com.samwoo.istudy.bean.HttpResult
import com.samwoo.istudy.bean.LoginData
import com.samwoo.istudy.callback.Callback
import com.samwoo.istudy.mvp.contract.LoginContract
import com.samwoo.istudy.mvp.model.LoginModel

class LoginPresenter : BasePresenter<LoginContract.View>(), LoginContract.Presenter {

    private val model: LoginModel by lazy { LoginModel() }

    override fun login(username: String, password: String) {
        mView?.showLoading()
        model.login(username, password, object : Callback<HttpResult<LoginData>, String> {
            override fun onSuccess(data: HttpResult<LoginData>) {
                if (isViewAttached()) {
                    mView?.run {
                        loginSuccess(data.data)
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