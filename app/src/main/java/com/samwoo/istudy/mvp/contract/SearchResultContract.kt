package com.samwoo.istudy.mvp.contract

import com.samwoo.istudy.base.IView
import com.samwoo.istudy.bean.ArticlesListBean

interface SearchResultContract {
    interface View : IView {
        fun showSearchResult(data: ArticlesListBean)
        fun scrollToTop()
    }

    interface Presenter {
        fun getSearchResult(page: Int, key: String)
    }
}