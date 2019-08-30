package com.samwoo.istudy.mvp.model

import com.samwoo.istudy.bean.ArticlesListBean
import com.samwoo.istudy.bean.HttpResult
import com.samwoo.istudy.callback.Callback
import com.samwoo.istudy.util.RequestUtil
import com.samwoo.istudy.util.handle

class ProjectListModel {
    fun getProjectList(curPage: Int, cid: Int, callback: Callback<HttpResult<ArticlesListBean>, String>) {
        RequestUtil.service.getProjectList(curPage, cid).handle(callback)
    }
}