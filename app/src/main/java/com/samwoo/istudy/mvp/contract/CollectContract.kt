package com.samwoo.istudy.mvp.contract

import com.samwoo.istudy.base.IView
import com.samwoo.istudy.bean.ArticlesListBean

interface CollectContract {
    interface View : IView {
        fun collectSuccess()
        fun collectFail()
        fun showCollectList(data:ArticlesListBean)
        fun cancleCollectSuccess()
        fun cancleCollectFail()
    }

    interface Presenter {
        fun getCollectList(page: Int)
        fun addCollectArticle(id: Int)
        fun addExtraCollectArticle(title: String, author: String, link: String)
        fun cancleCollectArticle(id: Int)
        fun removeCollectArticle(id: Int, originId: Int)
    }
}