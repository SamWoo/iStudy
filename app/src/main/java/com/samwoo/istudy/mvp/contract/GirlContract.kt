package com.samwoo.istudy.mvp.contract

import com.samwoo.istudy.base.IView
import com.samwoo.istudy.bean.Girl

interface GirlContract{
    interface View:IView{
        fun showGirlPhoto(data:List<Girl>)
    }
    interface Presenter{
        fun getGirlPhoto(page:Int)
    }
}