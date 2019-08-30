package com.samwoo.istudy.mvp.model

import com.samwoo.istudy.bean.ArticlesListBean
import com.samwoo.istudy.bean.Banner
import com.samwoo.istudy.bean.HttpResult
import com.samwoo.istudy.callback.Callback
import com.samwoo.istudy.util.RequestUtil
import com.samwoo.istudy.util.handle

class HomeModel {

    //获取Banner
    fun getBanners(callback: Callback<HttpResult<List<Banner>>, String>) {
        RequestUtil.service.getBanners().handle(callback)
    }

    //获取文章列表
    fun getArticles(curPage: Int, callback: Callback<HttpResult<ArticlesListBean>, String>) {
        RequestUtil.service.getArticles(curPage).handle(callback)
    }
}
