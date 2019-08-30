package com.samwoo.istudy.mvp.model

import com.samwoo.istudy.bean.HotKey
import com.samwoo.istudy.bean.HttpResult
import com.samwoo.istudy.callback.Callback
import com.samwoo.istudy.util.RequestUtil
import com.samwoo.istudy.util.handle

class SearchModel {
    // 获取当前搜索热词
    fun getHotKey(callback: Callback<HttpResult<List<HotKey>>, String>) {
        RequestUtil.service.getHotKey().handle(callback)
    }
}