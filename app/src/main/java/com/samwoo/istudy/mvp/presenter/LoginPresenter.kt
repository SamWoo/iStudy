package com.samwoo.istudy.mvp.presenter

import com.samwoo.istudy.base.BasePresenter
import com.samwoo.istudy.bean.HttpResult
import com.samwoo.istudy.bean.LoginData
import com.samwoo.istudy.bean.UserInfo
import com.samwoo.istudy.callback.Callback
import com.samwoo.istudy.constant.Constant
import com.samwoo.istudy.mvp.contract.LoginContract
import com.samwoo.istudy.mvp.model.LoginModel
import com.samwoo.istudy.util.Preference
import com.samwoo.istudy.util.SLog
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class LoginPresenter : BasePresenter<LoginContract.View>(), LoginContract.Presenter {
    private val model: LoginModel by lazy { LoginModel() }

    override fun login(username: String, password: String) {
        mView?.showLoading()
        model.login(username, password, object : Callback<HttpResult<LoginData>, String> {
            override fun onSuccess(data: HttpResult<LoginData>) {
                if (isViewAttached()) mView?.loginSuccess(data.data)
            }

            override fun onFail(msg: String) {
                if (isViewAttached()) {
                    doAsync { uiThread { mView?.showError(msg) } }
                }
            }
        })
        mView?.hideLoading()
    }

    override fun getUserInfo() {
        model.getUserInfo(object : Callback<HttpResult<UserInfo>, String> {
            override fun onSuccess(data: HttpResult<UserInfo>) {
                SLog.d("UserInfo-----> ${data.data}")
                if (isViewAttached()) mView?.getUserInfoSuccess(data.data)
            }

            override fun onFail(msg: String) {
                if (isViewAttached()) {
                    doAsync { uiThread { mView?.showError(msg) } }
                }
            }
        })
    }

}