package com.samwoo.istudy.mvp.model

import com.samwoo.istudy.bean.HttpResult
import com.samwoo.istudy.bean.ProjectTreeBody
import com.samwoo.istudy.callback.Callback
import com.samwoo.istudy.util.RequestUtil
import com.samwoo.istudy.util.handle

class ProjectTreeModel {
    fun getProjectTree(callback: Callback<HttpResult<List<ProjectTreeBody>>, String>) {
        RequestUtil.service.getProjectTree().handle(callback)
    }
}