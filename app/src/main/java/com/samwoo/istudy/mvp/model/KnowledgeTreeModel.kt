package com.samwoo.istudy.mvp.model

import com.samwoo.istudy.bean.HttpResult
import com.samwoo.istudy.bean.KnowledgeTreeBody
import com.samwoo.istudy.callback.Callback
import com.samwoo.istudy.constant.HostType
import com.samwoo.istudy.util.RetrofitManager
import com.samwoo.istudy.util.handle

class KnowledgeTreeModel {
    //获取知识体系
    fun getKnowledgeTree(callback: Callback<HttpResult<List<KnowledgeTreeBody>>, String>) {
        RetrofitManager.getService(HostType.WAN_ANDROID).getKnowledgeTree().handle(callback)
    }
}