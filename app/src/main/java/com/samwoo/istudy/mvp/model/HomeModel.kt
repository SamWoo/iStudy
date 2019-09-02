package com.samwoo.istudy.mvp.model

import com.samwoo.istudy.bean.ArticlesListBean
import com.samwoo.istudy.bean.Banner
import com.samwoo.istudy.bean.HttpResult
import com.samwoo.istudy.callback.Callback
import com.samwoo.istudy.constant.HostType
import com.samwoo.istudy.util.RetrofitManager
import com.samwoo.istudy.util.handle

class HomeModel {

    //获取Banner
    fun getBanners(callback: Callback<HttpResult<List<Banner>>, String>) {
        RetrofitManager.getService(HostType.WAN_ANDROID).getBanners().handle(callback)
    }

    //获取文章列表
    fun getArticles(curPage: Int, callback: Callback<HttpResult<ArticlesListBean>, String>) {
        RetrofitManager.getService(HostType.WAN_ANDROID).getArticles(curPage).handle(callback)
    }
}
