package com.samwoo.istudy.mvp.model

import com.samwoo.istudy.bean.HttpResult
import com.samwoo.istudy.bean.NavigationBean
import com.samwoo.istudy.callback.Callback
import com.samwoo.istudy.constant.HostType
import com.samwoo.istudy.util.RetrofitManager
import com.samwoo.istudy.util.handle

class NavigationModel {
    fun getNavList(callback: Callback<HttpResult<List<NavigationBean>>, String>) {
        RetrofitManager.getService(HostType.WAN_ANDROID).getNavList().handle(callback)
    }
}