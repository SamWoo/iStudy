package com.samwoo.istudy.mvp.model

import com.samwoo.istudy.bean.HttpResult
import com.samwoo.istudy.bean.LoginData
import com.samwoo.istudy.bean.UserInfo
import com.samwoo.istudy.callback.Callback
import com.samwoo.istudy.constant.HostType
import com.samwoo.istudy.util.RetrofitManager
import com.samwoo.istudy.util.handle

class LoginModel {
    fun login(
        username: String,
        password: String,
        callback: Callback<HttpResult<LoginData>, String>
    ) {
        RetrofitManager.getService(HostType.WAN_ANDROID).login(username, password).handle(callback)
    }

    fun getUserInfo(callback: Callback<HttpResult<UserInfo>, String>){
        RetrofitManager.getService(HostType.WAN_ANDROID).getUserInfo().handle(callback)
    }
}