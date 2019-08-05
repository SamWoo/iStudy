package com.samwoo.istudy.mvp.presenter

import android.util.Log
import com.samwoo.istudy.base.BasePresenter
import com.samwoo.istudy.bean.HotKey
import com.samwoo.istudy.bean.HttpResult
import com.samwoo.istudy.bean.SearchHistoryBean
import com.samwoo.istudy.callback.Callback
import com.samwoo.istudy.mvp.contract.SearchContract
import com.samwoo.istudy.mvp.model.SearchModel
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.litepal.BuildConfig
import org.litepal.LitePal

class SearchPresenter : BasePresenter<SearchContract.View>(), SearchContract.Presenter {
    private val searchModel: SearchModel by lazy {
        SearchModel()
    }

    override fun getHotKey() {
        searchModel.getHotKey(object : Callback<HttpResult<List<HotKey>>, String> {
            override fun onSuccess(result: HttpResult<List<HotKey>>) {
                if (isViewAttached()) {
                    mView?.run {
                        showHotKey(result.data)
                    }
                }

            }

            override fun onFail(msg: String) {
                if (isViewAttached()) {
                    mView?.run {
                        showError(msg)
                    }
                }
            }

        })
    }

    override fun deleteById(id: Long) {
        doAsync {
            LitePal.delete(SearchHistoryBean::class.java, id)
        }
    }

    override fun saveSearchKey(key: String) {
        doAsync {
            val historyBean = SearchHistoryBean(key.trim())
            val beans = LitePal.where("key='${key.trim()}'").find(SearchHistoryBean::class.java)
            if (beans.size != 0) deleteById(beans[0].id)
            historyBean.save()
            if(BuildConfig.DEBUG) Log.d("Sam", "save bean--->${key.trim()}/${historyBean}")
        }
    }

    override fun clearAllHistory() {
        doAsync {
            LitePal.deleteAll(SearchHistoryBean::class.java)
            uiThread { mView?.showDeleteAll("搜索历史已清空！") }
        }
    }

    override fun queryHistory() {
        doAsync {
            val historyBeans = LitePal.findAll(SearchHistoryBean::class.java)
            historyBeans.reverse()
            uiThread {
                mView?.showSearchHistory(historyBeans)
            }
        }
    }


}