package com.samwoo.istudy.mvp.presenter

import com.samwoo.istudy.base.BasePresenter
import com.samwoo.istudy.bean.ArticlesListBean
import com.samwoo.istudy.bean.HttpResult
import com.samwoo.istudy.callback.Callback
import com.samwoo.istudy.mvp.contract.CollectContract
import com.samwoo.istudy.mvp.model.CollectModel
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class CollectPresenter : BasePresenter<CollectContract.View>(), CollectContract.Presenter {
    private val model = CollectModel()

    override fun getCollectList(page: Int) {
        mView?.showLoading()
        model.getCollectList(page, object : Callback<HttpResult<ArticlesListBean>, String> {
            override fun onSuccess(data: HttpResult<ArticlesListBean>) {
                if (isViewAttached()) {
                    mView?.apply {
                        hideLoading()
                        showCollectList(data.data)
                    }
                }
            }

            override fun onFail(msg: String) {
                if (isViewAttached()) {
                    doAsync {
                        Thread.sleep(2 * 1000)
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

    override fun addCollectArticle(id: Int) {
        model.addCollectArticle(id, object : Callback<HttpResult<Any>, String> {
            override fun onSuccess(data: HttpResult<Any>) {
                if (isViewAttached()) {
                    mView?.collectSuccess()
                }
            }

            override fun onFail(msg: String) {
                if (isViewAttached()) {
                    mView?.collectFail()
                }
            }
        })
    }

    override fun addExtraCollectArticle(title: String, author: String, link: String) {
        model.addExtraCollectArticle(title, author, link, object : Callback<HttpResult<Any>, String> {
            override fun onSuccess(data: HttpResult<Any>) {
                if (isViewAttached()) {
                    mView?.collectSuccess()
                }
            }

            override fun onFail(msg: String) {
                if (isViewAttached()) {
                    mView?.collectFail()
                }
            }
        })
    }

    override fun cancleCollectArticle(id: Int) {
        model.cancleCollectArticle(id, object : Callback<HttpResult<Any>, String> {
            override fun onSuccess(data: HttpResult<Any>) {
                if (isViewAttached()) {
                    mView?.cancleCollectSuccess()
                }
            }

            override fun onFail(msg: String) {
                if (isViewAttached()) {
                    mView?.cancleCollectFail()
                }
            }
        })
    }

    override fun removeCollectArticle(id: Int, originId: Int) {
        model.removeCollectArticle(id, originId, object : Callback<HttpResult<Any>, String> {
            override fun onSuccess(data: HttpResult<Any>) {
                if (isViewAttached()) {
                    mView?.cancleCollectSuccess()
                }
            }

            override fun onFail(msg: String) {
                if (isViewAttached()) {
                    mView?.cancleCollectFail()
                }
            }
        })
    }

}