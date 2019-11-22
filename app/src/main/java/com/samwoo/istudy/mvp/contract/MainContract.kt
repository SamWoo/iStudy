package com.samwoo.istudy.mvp.contract

import com.samwoo.istudy.base.IView

interface MainContract {
    interface View: IView {
        fun getUserInfoSuccess()
    }
    interface Presenter {
        fun getUserInfo()
    }
}