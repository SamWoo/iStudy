package com.samwoo.istudy.mvp.presenter

import com.samwoo.istudy.base.BasePresenter
import com.samwoo.istudy.bean.ArticlesListBean
import com.samwoo.istudy.bean.HttpResult
import com.samwoo.istudy.callback.Callback
import com.samwoo.istudy.mvp.contract.ArticlesContract
import com.samwoo.istudy.mvp.model.ArticlesModel

class ArticlesPresenter : BasePresenter<ArticlesContract.View>(), ArticlesContract.Presenter {
    private val wxArticlesModel: ArticlesModel by lazy {
        ArticlesModel()
    }

    override fun getArticles(id: Int, curPage: Int) {
        mView?.showLoading()
        wxArticlesModel.getArticles(id, curPage, object : Callback<HttpResult<ArticlesListBean>, String> {
            override fun onSuccess(data: HttpResult<ArticlesListBean>) {
                mView?.run {
                    setArticles(data.data)
                    hideLoading()
                }
            }

            override fun onFail(msg: String) {
                mView?.apply {
                    showError(msg)
                    hideLoading()
                }
            }

        })
    }
}