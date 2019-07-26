package com.samwoo.istudy.api
import com.samwoo.istudy.bean.ArticlesListBean
import com.samwoo.istudy.bean.Banner
import com.samwoo.istudy.bean.HttpResult
import retrofit2.http.GET
import retrofit2.http.Path
import rx.Observable

interface ApiService{
    /**
     *获取文章列表
     * https://www.wanandroid.com/article/list/0/json
     * 方法：GET
     * 参数：页码，拼接在连接中，从0开始。
     **/
    @GET("/article/list/{curPage}/json")
    fun getArticles(@Path("curPage") pageNum:Int):Observable<HttpResult<ArticlesListBean>>

    /**
     * 首页Banner
     */
    @GET("/banner/json")
    fun getBanner():Observable<HttpResult<Banner>>
}