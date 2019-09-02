package com.samwoo.istudy.mvp.model

import com.samwoo.istudy.bean.HttpResult
import com.samwoo.istudy.bean.WxAccountBody
import com.samwoo.istudy.callback.Callback
import com.samwoo.istudy.constant.HostType
import com.samwoo.istudy.util.RetrofitManager
import com.samwoo.istudy.util.handle

class WxAccountModel {
    fun getWxAccount(callback: Callback<HttpResult<List<WxAccountBody>>, String>) {
        RetrofitManager.getService(HostType.WAN_ANDROID).getWxAccount().handle(callback)
    }
}