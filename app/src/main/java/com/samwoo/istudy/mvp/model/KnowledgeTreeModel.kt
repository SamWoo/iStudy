package com.samwoo.istudy.mvp.model

import com.samwoo.istudy.bean.HttpResult
import com.samwoo.istudy.bean.KnowledgeTreeBody
import com.samwoo.istudy.callback.Callback
import com.samwoo.istudy.util.RequestUtil
import com.samwoo.istudy.util.handle

class KnowledgeTreeModel {
    //获取知识体系
    fun getKnowledgeTree(callback: Callback<HttpResult<List<KnowledgeTreeBody>>, String>) {
        RequestUtil.service.getKnowledgeTree().handle(callback)
    }
}