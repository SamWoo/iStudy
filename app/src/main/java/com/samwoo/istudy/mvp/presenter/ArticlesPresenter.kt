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

    override fun getArticleList(curPage: Int, id: Int) {
//        mView?.showLoading()
        wxArticlesModel.getArticleList(curPage, id, object : Callback<HttpResult<ArticlesListBean>, String> {
            override fun onSuccess(result: HttpResult<ArticlesListBean>) {
                if (isViewAttached()) {
                    mView?.run {
                        setArticles(result.data)
                        hideLoading()
                    }
                }
            }

            override fun onFail(msg: String) {
                if (isViewAttached()) {
                    mView?.apply {
                        showError(msg)
                        hideLoading()
                    }
                }
            }
        })
    }
}