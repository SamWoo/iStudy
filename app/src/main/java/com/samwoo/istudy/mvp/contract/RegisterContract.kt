package com.samwoo.istudy.mvp.contract

import com.samwoo.istudy.base.IView
import com.samwoo.istudy.bean.LoginData

interface RegisterContract {
    interface View : IView {
        fun registerSuccess(data: LoginData)
    }

    interface Presenter {
        fun register(username: String, password: String, repassword: String)
    }
}