package com.samwoo.istudy.mvp.model

import com.samwoo.istudy.bean.HttpResult
import com.samwoo.istudy.bean.LoginData
import com.samwoo.istudy.callback.Callback
import com.samwoo.istudy.util.RequestUtil
import com.samwoo.istudy.util.handle

class RegisterModel {
    fun register(
        username: String,
        password: String,
        repassword: String,
        callback: Callback<HttpResult<LoginData>, String>
    ) {
        RequestUtil.service.register(username, password, repassword).handle(callback)
    }
}