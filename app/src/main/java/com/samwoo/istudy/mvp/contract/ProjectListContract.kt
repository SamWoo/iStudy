package com.samwoo.istudy.mvp.contract

import com.samwoo.istudy.base.IView
import com.samwoo.istudy.bean.Article
import com.samwoo.istudy.bean.ArticlesListBean

interface ProjectListContract {
    interface View : IView {
        fun scrollToTop()
        fun setProjectList(list: ArticlesListBean)
    }

    interface Presenter {
        fun getProjectList(curPage: Int, cid: Int)
    }
}