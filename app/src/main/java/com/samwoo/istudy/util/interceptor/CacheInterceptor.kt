package com.samwoo.istudy.util.interceptor

import com.samwoo.istudy.App
import com.samwoo.istudy.util.NetworkUtil
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.Response

/**
 * @desc 设置缓存拦截器
 */
class CacheInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        if (!NetworkUtil.isNetworkAvailable(App.context)) {
            request = request.newBuilder()
                .cacheControl(CacheControl.FORCE_CACHE)
                .build()
        }
        val response = chain.proceed(request)
        if (NetworkUtil.isNetworkAvailable(App.context)) {
            //设置缓存超时时间，有网络时设置超时为0，即不读取缓存数据，只对get有效，post没有缓存
            val maxAge = 60 * 3
            response.newBuilder()
                .header("Cache-Control", "public, max-age=$maxAge")
                .removeHeader("Retrofit")
                .build()
        } else {
            //无网络时，设置缓存超时4周，只对get有效，post没有缓存
            val maxAge = 60 * 60 * 24 * 7 * 4
            response.newBuilder()
                .header("Cache-Control", "public,only-if-cache, max-stale=$maxAge")
                .removeHeader("nyn")
                .build()
        }
        return response
    }

}