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
                val byteCount = if (buffer.size < 64) buffer.size else 64
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
}