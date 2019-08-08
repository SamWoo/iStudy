package com.samwoo.istudy.mvp.presenter

import com.samwoo.istudy.base.BasePresenter
import com.samwoo.istudy.bean.ArticlesListBean
import com.samwoo.istudy.bean.HttpResult
import com.samwoo.istudy.callback.Callback
import com.samwoo.istudy.mvp.contract.SearchResultContract
import com.samwoo.istudy.mvp.model.SearchResultModel

class SearchResultPresenter : BasePresenter<SearchResultContract.View>(), SearchResultContract.Presenter {

    private val model: SearchResultModel by lazy {
        SearchResultModel()
    }

    override fun getSearchResult(page: Int, key: String) {
        model.queryByKey(page, key, object : Callback<HttpResult<ArticlesListBean>, String> {
            override fun onSuccess(data: HttpResult<ArticlesListBean>) {
                if (isViewAttached()) {
                    mView?.run {
                        showLoading()
                        showSearchResult(data.data)
                        hideLoading()
                    }
                }

            }

            override fun onFail(msg: String) {
                if (isViewAttached()) {
                    mView?.run {
                        hideLoading()
                        showError(msg)
                    }
                }
            }

        })
    }

}