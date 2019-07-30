package com.samwoo.istudy.mvp.contract

import com.samwoo.istudy.base.IView
import com.samwoo.istudy.bean.Article

interface ProjectListContract {
    interface View : IView {
        fun scrollTop()
        fun setProjectList(list: List<Article>)
    }

    interface Presenter {
        fun getProjectList(curPage: Int, cid: Int)
    }
}