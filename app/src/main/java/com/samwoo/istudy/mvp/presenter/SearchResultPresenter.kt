package com.samwoo.istudy.mvp.presenter

import com.samwoo.istudy.base.BasePresenter
import com.samwoo.istudy.bean.ArticlesListBean
import com.samwoo.istudy.bean.HttpResult
import com.samwoo.istudy.callback.Callback
import com.samwoo.istudy.mvp.contract.SearchResultContract
import com.samwoo.istudy.mvp.model.SearchResultModel
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class SearchResultPresenter : BasePresenter<SearchResultContract.View>(), SearchResultContract.Presenter {

    private val model: SearchResultModel by lazy {
        SearchResultModel()
    }

    override fun getSearchResult(page: Int, key: String) {
        mView?.showLoading()
        model.queryByKey(page, key, object : Callback<HttpResult<ArticlesListBean>, String> {
            override fun onSuccess(data: HttpResult<ArticlesListBean>) {
                if (isViewAttached()) {
                    mView?.apply {
                        hideLoading()
                        showSearchResult(data.data)
                    }
                }
            }

            override fun onFail(msg: String) {
                if (isViewAttached()) {
                    doAsync {
                        //休眠2s模拟加载过程
                        for (ratio in 0..10) Thread.sleep(200)
                        ////处理完成，回到主线程在界面上显示
                        uiThread {
                            mView?.apply {
                                hideLoading()
                                showError(msg)
                            }
                        }
                    }
                }
            }
        })
    }
}