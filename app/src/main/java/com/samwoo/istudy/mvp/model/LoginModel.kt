package com.samwoo.istudy.mvp.model

import com.samwoo.istudy.bean.HttpResult
import com.samwoo.istudy.bean.LoginData
import com.samwoo.istudy.callback.Callback
import com.samwoo.istudy.util.RequestUtil
import com.samwoo.istudy.util.handle

class LoginModel {
    fun login(
        username: String,
        password: String,
        callback: Callback<HttpResult<LoginData>, String>
    ) {
        RequestUtil.service.login(username, password).handle(callback)
    }
}