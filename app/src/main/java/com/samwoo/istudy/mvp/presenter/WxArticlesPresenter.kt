package com.samwoo.istudy.mvp.presenter

import com.samwoo.istudy.base.BasePresenter
import com.samwoo.istudy.bean.ArticlesListBean
import com.samwoo.istudy.bean.HttpResult
import com.samwoo.istudy.callback.Callback
import com.samwoo.istudy.mvp.contract.WxArticlesContract
import com.samwoo.istudy.mvp.model.WxArticlesModel

class WxArticlesPresenter : BasePresenter<WxArticlesContract.View>(), WxArticlesContract.Presenter {
    private val wxArticlesModel: WxArticlesModel by lazy {
        WxArticlesModel()
    }

    override fun getWxArticles(id: Int, curPage: Int) {
        mView?.showLoading()
        wxArticlesModel.getWxArticles(id, curPage, object : Callback<HttpResult<ArticlesListBean>, String> {
            override fun onSuccess(data: HttpResult<ArticlesListBean>) {
                mView?.run {
                    setWxArticles(data.data)
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