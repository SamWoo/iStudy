package com.samwoo.istudy.mvp.model

import com.samwoo.istudy.bean.ArticlesListBean
import com.samwoo.istudy.bean.HttpResult
import com.samwoo.istudy.callback.Callback
import com.samwoo.istudy.constant.HostType
import com.samwoo.istudy.util.RetrofitManager
import com.samwoo.istudy.util.handle

class ProjectListModel {
    fun getProjectList(curPage: Int, cid: Int, callback: Callback<HttpResult<ArticlesListBean>, String>) {
        RetrofitManager.getService(HostType.WAN_ANDROID).getProjectList(curPage, cid).handle(callback)
    }
}