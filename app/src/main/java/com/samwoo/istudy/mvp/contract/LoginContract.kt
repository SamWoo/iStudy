package com.samwoo.istudy.mvp.contract

import com.samwoo.istudy.base.IView
import com.samwoo.istudy.bean.LoginData
import com.samwoo.istudy.bean.UserInfo

interface LoginContract {
    interface View : IView {
        fun loginSuccess(data: LoginData)
        fun getUserInfoSuccess(data: UserInfo)
    }

    interface Presenter {
        fun login(username: String, password: String)
        fun getUserInfo()
    }
}