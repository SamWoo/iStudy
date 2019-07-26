package com.samwoo.istudy.mvp.model

import com.samwoo.istudy.callback.Callback

interface IArticlesModel:IModel{
    fun getArticles(curPage:Int, callback: Callback)
}