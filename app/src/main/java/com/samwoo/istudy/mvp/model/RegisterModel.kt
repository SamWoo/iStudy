package com.samwoo.istudy.mvp.model

import com.samwoo.istudy.bean.HttpResult
import com.samwoo.istudy.bean.LoginData
import com.samwoo.istudy.callback.Callback
import com.samwoo.istudy.constant.HostType
import com.samwoo.istudy.util.RetrofitManager
import com.samwoo.istudy.util.handle

class RegisterModel {
    fun register(
        username: String,
        password: String,
        repassword: String,
        callback: Callback<HttpResult<LoginData>, String>
    ) {
        RetrofitManager.getService(HostType.WAN_ANDROID).register(username, password, repassword).handle(callback)
    }
}