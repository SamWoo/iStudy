package com.samwoo.istudy.mvp.contract

import com.samwoo.istudy.base.IView
import com.samwoo.istudy.bean.ArticlesListBean
import com.samwoo.istudy.bean.BannerList
import com.samwoo.istudy.bean.HttpResult

interface HomeContract {
    interface View : IView {
        fun scrollToTop()
        fun setBanner(banners: BannerList)
        fun setArticles(list: ArticlesListBean)
    }

    interface Presenter {
        fun getBanners()
        fun getArticles(curPage: Int)
    }
}