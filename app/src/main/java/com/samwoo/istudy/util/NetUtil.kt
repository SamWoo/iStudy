package com.samwoo.istudy.util

import android.os.Build
import android.preference.Preference
import com.google.gson.Gson
import com.samwoo.istudy.BuildConfig
import com.samwoo.istudy.api.ApiService
import okhttp3.Cache
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File

object NetUtil {
    private var retrofit: Retrofit? = null
    val service: ApiService by lazy { getRetrofit()!!.create(ApiService::class.java) }

    //token
//    private var token:String by Preference("token","")
    private fun getRetrofit(): Retrofit? {
        if (retrofit == null) {
            synchronized(NetUtil::class.java) {
                if (retrofit == null) {
                    retrofit = Retrofit.Builder()
                        .baseUrl("https://www.wanandroid.com")
                        .client(getOKHttpClient())
                        .addConverterFactory(GsonConverterFactory.create())
                        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                        .build()
                }
            }
        }
        return retrofit
    }

    private fun getOKHttpClient(): OkHttpClient {
        val builder = OkHttpClient().newBuilder()
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        if (BuildConfig.DEBUG) {
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        } else {
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.NONE
        }

//        val cacheFile= File(App.context.cacheDir,"cache")
//        val cache= Cache(cacheFile, 1024*1024*50)
        builder.run {
            addInterceptor(httpLoggingInterceptor)
            addInterceptor(addHttpInterceptor())
            addInterceptor(addCacheInterceptor())
//            cache(cache)
        }
        return builder.build()
    }

    //add header
    private fun addHttpInterceptor(): Interceptor {
        return Interceptor { chain ->
            val builder = chain.request().newBuilder()
            val request = builder.addHeader("Content-type", "application/json;charset=utf-8").build()
            chain.proceed(request)
        }
    }

    //add cache
    private fun addCacheInterceptor(): Interceptor {
        return Interceptor { chain ->
            var request = chain.request()
            if (!NetworkUtil.isNetworkAvailable(App.context)) {
                request = request.newBuilder()
                    .cacheControl(CacheControl.FORCE_CACHE)
                    .build()
            }
            val response = chain.proceed(request)
            if (NetworkUtil.isNetworkAvailable(App.context)) {
                val maxAge = 0
                // 有网络时 设置缓存超时时间0个小时 ,意思就是不读取缓存数据,只对get有用,post没有缓冲
                response.newBuilder()
                    .header("Cache-Control", "public,max-age=" + maxAge)
                    .removeHeader("Retrofit") // 清除头信息，因为服务器如果不支持，会返回一些干扰信息，不清除下面无法生效
                    .build()
            } else {
                // 无网络时，设置超时为4周  只对get有用,post没有缓冲
                val maxStale = 60 * 60 * 24 * 28
                response.newBuilder()
                    .header("Cache-Control", "public,only-if-cached,max-stale=" + maxStale)
                    .removeHeader("nyn")
                    .build()
            }
            response
        }
    }

    //add Header
    private fun addHeaderInterceptor(): Interceptor {
        return Interceptor { chain ->
            val originalRequest = chain.request()
            val requestBuilder = originalRequest.newBuilder()
//                .header("token", token)
                .method(originalRequest.method(), originalRequest.body())
            val request = requestBuilder.build()
            chain.proceed(request)
        }
    }
}