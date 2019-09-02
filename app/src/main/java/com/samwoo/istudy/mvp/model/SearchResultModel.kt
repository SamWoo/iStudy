package com.samwoo.istudy.mvp.model

import com.samwoo.istudy.bean.ArticlesListBean
import com.samwoo.istudy.bean.HttpResult
import com.samwoo.istudy.callback.Callback
import com.samwoo.istudy.constant.HostType
import com.samwoo.istudy.util.RetrofitManager
import com.samwoo.istudy.util.handle

class SearchResultModel {
    // 根据关键字搜索
    fun queryByKey(
        page: Int,
        key: String,
        callback: Callback<HttpResult<ArticlesListBean>, String>
    ) {
        RetrofitManager.getService(HostType.WAN_ANDROID).queryByKey(page, key).handle(callback)
    }
}