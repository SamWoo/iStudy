package com.samwoo.istudy.util.interceptor

import com.samwoo.istudy.constant.Constant
import com.samwoo.istudy.util.Preference
import okhttp3.Interceptor
import okhttp3.Response

/**
 * @desc 设置请求头
 */
class HeaderInterceptor : Interceptor {
    //token
    private var token: String by Preference(Constant.TOKEN_KEY, "")

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val builder = request.newBuilder()
        builder.addHeader("Content-type", "application/json; charset=utf-8")

        val domain = request.url().host()
        val url = request.url().toString()
        if (domain.isNotEmpty() && (url.contains(Constant.COLLECTIONS_WEBSITE)
                    || url.contains(Constant.UNCOLLECTIONS_WEBSITE)
                    || url.contains(Constant.ARTICLE_WEBSITE)
                    || url.contains(Constant.PROJECT_WEBSITE))
        ) {
            val spDomain: String by Preference(domain, "")
            val cookie: String = if (spDomain.isNotEmpty()) spDomain else ""
            if (cookie.isNotEmpty()) {
                //将cookie添加到请求头
                builder.addHeader(Constant.COOKIE_NAME, cookie)
            }
        }
        return chain.proceed(builder.build())
    }
}