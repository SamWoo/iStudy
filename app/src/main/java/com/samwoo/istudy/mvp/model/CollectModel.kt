package com.samwoo.istudy.mvp.model

import com.samwoo.istudy.bean.ArticlesListBean
import com.samwoo.istudy.bean.HttpResult
import com.samwoo.istudy.callback.Callback
import com.samwoo.istudy.util.RequestUtil
import com.samwoo.istudy.util.handle

class CollectModel {
    fun getCollectList(page: Int, callback: Callback<HttpResult<ArticlesListBean>, String>) {
        RequestUtil.service.getCollectList(page).handle(callback)
    }

    fun addCollectArticle(id: Int, callback: Callback<HttpResult<Any>, String>) {
        RequestUtil.service.addCollectArticle(id).handle(callback)
    }

    fun addExtraCollectArticle(
        title: String,
        author: String,
        link: String,
        callback: Callback<HttpResult<Any>, String>
    ) {
        RequestUtil.service.addExtraCollectArticle(title, author, link).handle(callback)
    }

    fun cancleCollectArticle(id: Int, callback: Callback<HttpResult<Any>, String>) {
        RequestUtil.service.cancelCollectArticle(id).handle(callback)
    }

    fun removeCollectArticle(
        id: Int,
        originId: Int = -1,
        callback: Callback<HttpResult<Any>, String>
    ) {
        RequestUtil.service.removeCollectArticle(id, originId).handle(callback)
    }
}