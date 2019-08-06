package com.samwoo.istudy.api

import com.samwoo.istudy.bean.*
import retrofit2.http.*
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
     * 微信公众号
     * https://wanandroid.com/wxarticle/chapters/json
     */
    @GET("/wxarticle/chapters/json")
    fun getWxAccount(): Observable<HttpResult<List<WxAccountBody>>>

    /**
     *  获取知识体系下的文章 或 查看某个公众号历史数据
     * https://www.wanandroid.com/article/list/0/json?cid=60
     * @param curPage
     * @param cid
     */
    @GET("/article/list/{curPage}/json")
    fun getArticleList(@Path("curPage") curPage: Int, @Query("cid") cid: Int): Observable<HttpResult<ArticlesListBean>>

    /**
     * 项目数据
     * http://www.wanandroid.com/project/tree/json
     */
    @GET("/project/tree/json")
    fun getProjectTree(): Observable<HttpResult<List<ProjectTreeBody>>>

    /**
     * 项目列表数据
     * http://www.wanandroid.com/project/list/1/json?cid=294
     * @param curPage
     * @param cid 分类的id，上述二级目录的id
     */
    @GET("/project/list/{curPage}/json")
    fun getProjectList(@Path("curPage") curPage: Int, @Query("cid") cid: Int): Observable<HttpResult<ArticlesListBean>>

    /**
     * 搜索热词
     * https://www.wanandroid.com//hotkey/json
     */
    @GET("/hotkey/json")
    fun getHotKey(): Observable<HttpResult<List<HotKey>>>

    /**
     * 搜索
     * http://www.wanandroid.com/article/query/0/json
     * @param page
     * @param key
     */
    @POST("/article/query/{page}/json")
    @FormUrlEncoded
    fun queryByKey(@Path("page") page: Int, @Field("k") key: String): Observable<HttpResult<ArticlesListBean>>
}