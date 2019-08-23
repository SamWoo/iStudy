package com.samwoo.istudy.util

import com.samwoo.istudy.App
import com.samwoo.istudy.BuildConfig
import com.samwoo.istudy.api.ApiService
import com.samwoo.istudy.constant.Constant
import com.samwoo.istudy.constant.Constant.BASE_URL
import com.samwoo.istudy.util.interceptor.CacheInterceptor
import com.samwoo.istudy.util.interceptor.HeaderInterceptor
import com.samwoo.istudy.util.interceptor.SaveCookieInterceptor
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import okio.Buffer
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.io.EOFException
import java.io.File
import java.nio.charset.Charset
import java.util.concurrent.TimeUnit


object RequestUtil {
    private val DEFAULT_TIMEOUT: Long = 60
    private val DEFAULT_TIMEOUT_WRITE: Long = 60
    private var retrofit: Retrofit? = null
    val service: ApiService by lazy { getRetrofit()!!.create(ApiService::class.java) }

    //token
//    private var token:String by Preference("token","")
    private fun getRetrofit(): Retrofit? {
        if (retrofit == null) {
            synchronized(RequestUtil::class.java) {
                if (retrofit == null) {
                    retrofit = Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .client(getOKHttpClient())
                        .addConverterFactory(GsonConverterFactory.create())
                        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                        .addConverterFactory(ScalarsConverterFactory.create())
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

        //设置请求的缓存大小和位置
        val cacheFile = File(App.context.cacheDir, "cache")
        val cache = Cache(cacheFile, Constant.MAX_CACHE_SIZE)

        builder.run {
            connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
            writeTimeout(DEFAULT_TIMEOUT_WRITE, TimeUnit.SECONDS)
            readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
            retryOnConnectionFailure(true) //错误重连
            //addNetworkInterceptor(networkInterceptor)
            addInterceptor(httpLoggingInterceptor)
            addInterceptor(HeaderInterceptor())
            addInterceptor(SaveCookieInterceptor())
            addInterceptor(CacheInterceptor())
            cache(cache) //添加缓存
        }
        return builder.build()
    }
}