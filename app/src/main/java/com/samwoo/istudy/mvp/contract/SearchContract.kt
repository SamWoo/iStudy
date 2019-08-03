package com.samwoo.istudy.mvp.contract

import com.samwoo.istudy.base.IView
import com.samwoo.istudy.bean.HotKey
import com.samwoo.istudy.bean.SearchHistoryBean

interface SearchContract {
    interface View : IView {
        fun showHotKey(data: List<HotKey>)
        fun showSearchHistory(data: List<SearchHistoryBean>)
        fun showDeleteAll(msg:String)
    }

    interface Presenter {
        fun getHotKey()
        fun deleteById(id: Long)
        fun saveSearchKey(key: String)
        fun clearAllHistory()
        fun queryHistory()
    }
}