package com.samwoo.istudy.util

import com.samwoo.istudy.App
import com.samwoo.istudy.BuildConfig
import com.samwoo.istudy.api.ApiService
import com.samwoo.istudy.constant.Constant.BASE_URL
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import okio.Buffer
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.io.EOFException
import java.nio.charset.Charset
import java.util.concurrent.TimeUnit


object NetUtil {
    private val DEFAULT_TIMEOUT: Long = 60
    private val DEFAULT_TIMEOUT_WRITE: Long = 60
    private var retrofit: Retrofit? = null
    val service: ApiService by lazy { getRetrofit()!!.create(ApiService::class.java) }

    //token
//    private var token:String by Preference("token","")
    private fun getRetrofit(): Retrofit? {
        if (retrofit == null) {
            synchronized(NetUtil::class.java) {
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

//        val cacheFile= File(App.context.cacheDir,"cache")
//        val cache= Cache(cacheFile, 1024*1024*50)
        builder.run {
            connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
            writeTimeout(DEFAULT_TIMEOUT_WRITE, TimeUnit.SECONDS)
            readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
            addNetworkInterceptor(networkInterceptor)
            addInterceptor(httpLoggingInterceptor)
//            addInterceptor(addHttpInterceptor())
//            addInterceptor(addCacheInterceptor())
//            cache(cache)
        }
        return builder.build()
    }

    private val networkInterceptor = object : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val request = chain.request()
            //the request url
            val url = request.url().toString()
            //the request method
            val method = request.method()
            val t1 = System.nanoTime()
            //                LogPrint.w(String.format(Locale.getDefault(), "1. Sending %s request [url = %s]", method, url));
            //the request body
            val requestBody = request.body()
            if (requestBody != null) {
                val sb = StringBuilder("Request Body [")
                val buffer = Buffer()
                requestBody!!.writeTo(buffer)
                var charset = Charset.forName("UTF-8")
                val contentType = requestBody!!.contentType()
                if (contentType != null) {
                    charset = contentType!!.charset(charset)
                }
                if (contentType != null && isPlaintext(buffer)) {
                    sb.append(buffer.readString(charset))
                    sb.append(" (Content-Type = ").append(contentType!!.toString()).append(",")
                        .append(requestBody!!.contentLength()).append("-byte body)")
                } else {
                    sb.append(" (Content-Type = ").append(contentType!!.toString())
                        .append(",binary ").append(requestBody!!.contentLength()).append("-byte body omitted)")
                }
                sb.append("]")
            }
            val response = chain.proceed(request)
            val t2 = System.nanoTime()
            val body = response.body()

            val source = body.source()
            source.request(java.lang.Long.MAX_VALUE) // Buffer the entire body.
            val buffer = source.buffer()
            var charset = Charset.defaultCharset()
            val contentType = body.contentType()
            if (contentType != null) {
                charset = contentType!!.charset(charset)
            }
            val bodyString = buffer.clone().readString(charset)
            return response
        }

        fun isPlaintext(buffer: Buffer): Boolean {
            try {
                val prefix = Buffer()
                val byteCount = if (buffer.size() < 64) buffer.size() else 64
                buffer.copyTo(prefix, 0, byteCount)
                for (i in 0..15) {
                    if (prefix.exhausted()) {
                        break
                    }
                    val codePoint = prefix.readUtf8CodePoint()
                    if (Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
                        return false
                    }
                }
                return true
            } catch (e: EOFException) {
                return false // Truncated UTF-8 sequence.
            }
        }
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