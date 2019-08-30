package com.samwoo.istudy.mvp.model

import com.samwoo.istudy.bean.ArticlesListBean
import com.samwoo.istudy.bean.HttpResult
import com.samwoo.istudy.callback.Callback
import com.samwoo.istudy.util.RequestUtil
import com.samwoo.istudy.util.handle

class ArticlesModel {
    fun getArticleList(curPage: Int, id: Int, callback: Callback<HttpResult<ArticlesListBean>, String>) {
        RequestUtil.service.getArticleList(curPage, id).handle(callback)
    }
}