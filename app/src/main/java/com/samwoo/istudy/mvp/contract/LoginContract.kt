package com.samwoo.istudy.mvp.contract

import com.samwoo.istudy.base.IView
import com.samwoo.istudy.bean.LoginData

interface LoginContract {
    interface View : IView {
        fun loginSuccess(data: LoginData)
    }

    interface Presenter {
        fun login(username: String, password: String)
    }
}