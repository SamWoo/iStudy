package com.samwoo.istudy.api

import com.samwoo.istudy.bean.*
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import rx.Observable

interface ApiService {
    /**
     *获取文章列表
     * https://www.wanandroid.com/article/list/0/json
     * 方法：GET
     * 参数：页码，拼接在连接中，从0开始。
     **/
    @GET("/article/list/{curPage}/json")
    fun getArticles(@Path("curPage") pageNum: Int): Observable<HttpResult<ArticlesListBean>>

    /**
     * 首页Banner
     */
    @GET("/banner/json")
    fun getBanners(): Observable<BannerList>

    /**
     * 获取知识体系
     * http://www.wanandroid.com/tree/json
     */
    @GET("/tree/json")
    fun getKnowledgeTree(): Observable<HttpResult<List<KnowledgeTreeBody>>>

    /**
     * 项目数据
     * http://www.wanandroid.com/project/tree/json
     */
    @GET("/project/tree/json")
    fun getProjectTree(): Observable<HttpResult<List<ProjectTreeBody>>>

    /**
     * 项目列表数据
     * http://www.wanandroid.com/project/list/1/json?cid=294
     * @param page
     * @param cid
     */
    @GET("/project/list/{curPage}/json")
    fun getProjectList(@Path("curPage") curPage: Int, @Query("cid") cid: Int): Observable<HttpResult<ArticlesListBean>>
}