package com.samwoo.istudy.mvp.contract

import com.samwoo.istudy.base.IView
import com.samwoo.istudy.bean.ArticlesListBean

interface ArticlesContract {
    interface View : IView {
        fun scrollToTop()
        fun setArticles(list: ArticlesListBean)
    }

    interface Presenter {
        fun getArticleList(curPage: Int, id: Int)
    }
}