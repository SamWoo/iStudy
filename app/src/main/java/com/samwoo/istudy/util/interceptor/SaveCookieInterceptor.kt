package com.samwoo.istudy.util.interceptor

import com.samwoo.istudy.constant.Constant
import com.samwoo.istudy.util.CookieUtil
import okhttp3.Interceptor
import okhttp3.Response

class SaveCookieInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)
        val requsetUrl = request.url().toString()
        val domain = request.url().host()
        //登录成功时保存cookie
        if ((requsetUrl.contains(Constant.SAVE_USER_LOGIN_KEY)
                    || requsetUrl.contains(Constant.SAVE_USER_REGISTER_KEY))
            && !response.header(Constant.SET_COOKIE_KEY).isNotEmpty()
        ) {
            val cookies = response.headers(Constant.SET_COOKIE_KEY)
            val cookie = CookieUtil.encodeCookie(cookies)
            CookieUtil.saveCookie(requsetUrl, domain, cookie)
        }
        return response
    }
}