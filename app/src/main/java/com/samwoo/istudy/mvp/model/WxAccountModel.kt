package com.samwoo.istudy.mvp.model

import com.samwoo.istudy.bean.HttpResult
import com.samwoo.istudy.bean.WxAccountBody
import com.samwoo.istudy.callback.Callback
import com.samwoo.istudy.util.RequestUtil
import com.samwoo.istudy.util.handle

class WxAccountModel {
    fun getWxAccount(callback: Callback<HttpResult<List<WxAccountBody>>, String>) {
        RequestUtil.service.getWxAccount().handle(callback)
    }
}