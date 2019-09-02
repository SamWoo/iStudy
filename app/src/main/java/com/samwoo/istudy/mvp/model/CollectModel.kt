package com.samwoo.istudy.mvp.model

import com.samwoo.istudy.bean.ArticlesListBean
import com.samwoo.istudy.bean.HttpResult
import com.samwoo.istudy.callback.Callback
import com.samwoo.istudy.constant.HostType
import com.samwoo.istudy.util.RetrofitManager
import com.samwoo.istudy.util.handle

class CollectModel {
    fun getCollectList(page: Int, callback: Callback<HttpResult<ArticlesListBean>, String>) {
        RetrofitManager.getService(HostType.WAN_ANDROID).getCollectList(page).handle(callback)
    }

    fun addCollectArticle(id: Int, callback: Callback<HttpResult<Any>, String>) {
        RetrofitManager.getService(HostType.WAN_ANDROID).addCollectArticle(id).handle(callback)
    }

    fun addExtraCollectArticle(
        title: String,
        author: String,
        link: String,
        callback: Callback<HttpResult<Any>, String>
    ) {
        RetrofitManager.getService(HostType.WAN_ANDROID).addExtraCollectArticle(title, author, link).handle(callback)
    }

    fun cancleCollectArticle(id: Int, callback: Callback<HttpResult<Any>, String>) {
        RetrofitManager.getService(HostType.WAN_ANDROID).cancelCollectArticle(id).handle(callback)
    }

    fun removeCollectArticle(
        id: Int,
        originId: Int = -1,
        callback: Callback<HttpResult<Any>, String>
    ) {
        RetrofitManager.getService(HostType.WAN_ANDROID).removeCollectArticle(id, originId).handle(callback)
    }
}