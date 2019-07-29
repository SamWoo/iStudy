package com.samwoo.istudy.bean

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

//返回体
data class HttpResult<T>(
    val data: T,
    val errorCode: Int,
    val errorMsg: String
)

//文章列表
@Parcelize
data class ArticlesListBean(
    val curPage: Int,
    val datas: MutableList<Article>,
    val offset: Int,
    val over: Boolean,
    val pageCount: Int,
    val size: Int,
    val total: Int
) : Parcelable

//文章
@Parcelize
data class Article(
    val apkLink: String,
    val author: String,
    val chapterId: Int,
    val chapterName: String,
    val collect: Boolean,
    val courseId: Int,
    val desc: String,
    val envelopePic: String,
    val fresh: Boolean,
    val id: Int,
    val link: String,
    val niceDate: String,
    val origin: String,
    val projectLink: String,
    val publishTime: Long,
    val superChapterId: Int,
    val superChapterName: String,
    val tags: MutableList<Tag>,
    val title: String,
    val type: Int,
    val userId: Int,
    val visible: Int,
    val zan: Int
) : Parcelable

@Parcelize
data class Tag(
    val name: String,
    val url: String
) : Parcelable

//轮播图
@Parcelize
data class BannerList(
    val errorCode: Int,
    val errorMsg: String,
    var data: MutableList<Banner>
) : Parcelable

@Parcelize
data class Banner(
    val desc: String,
    val id: Int,
    val imagePath: String,
    val isVisible: Int,
    val order: Int,
    val title: String,
    val type: Int,
    val url: String
) : Parcelable

//知识体系

data class KnowledgeTreeBody(
    val children: MutableList<Knowledge>,
    val courseId: Int,
    val id: Int,
    val name: String,
    val order: Int,
    val parentChapterId: Int,
    val userControlSetTop: Boolean,
    val visible: Int
) : Serializable

data class Knowledge(
    val children: MutableList<Any>,
    val courseId: Int,
    val id: Int,
    val name: String,
    val order: Int,
    val parentChapterId: Int,
    val userControlSetTop: Boolean,
    val visible: Int
) : Serializable

//Hot Words
@Parcelize
data class HotKey(
    val id: Int,
    val link: String,
    val name: String,
    val order: Int,
    val visible: Int
) : Parcelable