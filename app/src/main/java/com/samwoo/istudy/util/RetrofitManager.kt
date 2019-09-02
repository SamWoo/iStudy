package com.samwoo.istudy.util

import android.util.SparseArray
import com.samwoo.istudy.App
import com.samwoo.istudy.BuildConfig
import com.samwoo.istudy.api.ApiService
import com.samwoo.istudy.constant.Constant
import com.samwoo.istudy.constant.HostType
import com.samwoo.istudy.util.interceptor.CacheInterceptor
import com.samwoo.istudy.util.interceptor.HeaderInterceptor
import com.samwoo.istudy.util.interceptor.SaveCookieInterceptor
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit


class RetrofitManager {
    private val DEFAULT_TIMEOUT: Long = 60
    private val DEFAULT_TIMEOUT_WRITE: Long = 60
    private var retrofit: Retrofit? = null

    private var okHttpClient: OkHttpClient? = null


//    val service: ApiService by lazy { getRetrofit()!!.create(ApiService::class.java) }

    constructor(@HostType.HostTypeChecker hostType: Int) {
        val mRetrofit = Retrofit.Builder()
            .baseUrl(Constant.getHost(hostType))
            .client(getOKHttpClient())
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
        service = mRetrofit.create(ApiService::class.java)
        serviceArray.put(hostType, service)
    }

    companion object {
        private var service: ApiService? = null
        private var managerArray = SparseArray<RetrofitManager>(HostType.TYPE_COUNT)
        private var serviceArray = SparseArray<ApiService>(HostType.TYPE_COUNT)

        fun getInstance(hostType: Int): RetrofitManager {
            var manager: RetrofitManager? = managerArray.get(hostType)
            if (manager == null) {
                manager = RetrofitManager(hostType)
                managerArray.put(hostType, manager)
            }
            return manager
        }

        fun getService(hostType: Int): ApiService {
            service = serviceArray.get(hostType)
            if (service == null) {
                val manager = RetrofitManager(hostType)
            }
            return service as ApiService
        }
    }

    //token
//    private var token:String by Preference("token","")

    //单玩安卓url请求处理方式
    private fun getRetrofit(): Retrofit? {
        if (retrofit == null) {
            synchronized(RetrofitManager::class.java) {
                if (retrofit == null) {
                    retrofit = Retrofit.Builder()
                        .baseUrl(Constant.BASE_URL)
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

    private fun getOKHttpClient(): OkHttpClient? {
        if (okHttpClient == null) {
            synchronized(RetrofitManager::class.java) {
                //设置请求的缓存大小和位置
                val cacheFile = File(App.context.cacheDir, "cache")
                val cache = Cache(cacheFile, Constant.MAX_CACHE_SIZE)
                if (okHttpClient == null) {
                    val builder = OkHttpClient().newBuilder()
                    val httpLoggingInterceptor = HttpLoggingInterceptor()
                    if (BuildConfig.DEBUG) {
                        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
                    } else {
                        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.NONE
                    }

                    okHttpClient = builder.run {
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
                        build()
                    }
                }
            }
        }
        return okHttpClient
    }
}