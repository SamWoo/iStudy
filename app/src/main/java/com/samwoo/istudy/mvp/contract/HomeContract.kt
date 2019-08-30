package com.samwoo.istudy.mvp.contract

import com.samwoo.istudy.base.IView
import com.samwoo.istudy.bean.ArticlesListBean
import com.samwoo.istudy.bean.Banner

interface HomeContract {
    interface View : IView {
        fun scrollToTop()
        fun setBanner(list: List<Banner>)
        fun setArticles(list: ArticlesListBean)
    }

    interface Presenter {
        fun getBanners()
        fun getArticles(curPage: Int)
    }
}