package com.samwoo.istudy.mvp.contract

import com.samwoo.istudy.base.IView
import com.samwoo.istudy.bean.ArticlesListBean

interface WxArticlesContract {
    interface View : IView {
        fun scrollTop()
        fun setWxArticles(list: ArticlesListBean)
    }

    interface Presenter {
        fun getWxArticles(id: Int, curPage: Int)
    }
}