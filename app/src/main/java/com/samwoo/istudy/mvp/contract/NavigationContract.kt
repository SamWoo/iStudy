package com.samwoo.istudy.mvp.contract

import com.samwoo.istudy.base.IView
import com.samwoo.istudy.bean.NavigationBean

interface NavigationContract {
    interface View : IView {
        fun setNavList(list: List<NavigationBean>)
        fun scrollToTop()
    }

    interface Presenter {
        fun getNavList()
    }
}