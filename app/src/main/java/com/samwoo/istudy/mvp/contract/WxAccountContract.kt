package com.samwoo.istudy.mvp.contract

import com.samwoo.istudy.base.IView
import com.samwoo.istudy.bean.WxAccountBody

interface WxAccountContract{
    interface View:IView{
        fun scrollToTop()
        fun setWxAccount(data:List<WxAccountBody>)
    }

    interface Presenter{
        fun getWxAccount()
    }
}