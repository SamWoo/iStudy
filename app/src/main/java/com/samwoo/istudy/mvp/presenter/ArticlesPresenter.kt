package com.samwoo.istudy.mvp.presenter

import com.samwoo.istudy.base.BasePresenter
import com.samwoo.istudy.bean.ArticlesListBean
import com.samwoo.istudy.bean.HttpResult
import com.samwoo.istudy.callback.Callback
import com.samwoo.istudy.mvp.contract.ArticlesContract
import com.samwoo.istudy.mvp.model.ArticlesModel
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class ArticlesPresenter : BasePresenter<ArticlesContract.View>(), ArticlesContract.Presenter {
    private val wxArticlesModel: ArticlesModel by lazy {
        ArticlesModel()
    }

    override fun getArticleList(curPage: Int, id: Int) {
        mView?.showLoading()
        wxArticlesModel.getArticleList(curPage, id, object : Callback<HttpResult<ArticlesListBean>, String> {
            override fun onSuccess(result: HttpResult<ArticlesListBean>) {
                if (isViewAttached()) {
                    mView?.apply {
                        hideLoading()
                        setArticles(result.data)
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