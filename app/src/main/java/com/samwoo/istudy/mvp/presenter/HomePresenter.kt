package com.samwoo.istudy.mvp.presenter

import com.samwoo.istudy.base.BasePresenter
import com.samwoo.istudy.bean.ArticlesListBean
import com.samwoo.istudy.bean.Banner
import com.samwoo.istudy.bean.BannerList
import com.samwoo.istudy.bean.HttpResult
import com.samwoo.istudy.callback.Callback
import com.samwoo.istudy.mvp.contract.HomeContract
import com.samwoo.istudy.mvp.model.HomeModel
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.util.logging.Handler

class HomePresenter : BasePresenter<HomeContract.View>(), HomeContract.Presenter {

    private val homeModel: HomeModel by lazy {
        HomeModel()
    }

    override fun getBanners() {
        homeModel.getBanners(object : Callback<HttpResult<List<Banner>>, String> {
            override fun onSuccess(result: HttpResult<List<Banner>>) {
                if (isViewAttached()) mView?.setBanner(result.data)
            }

            override fun onFail(data: String) {
                if (isViewAttached()) mView?.showError(data)
            }
        })
    }

    override fun getArticles(curPage: Int) {
        mView?.showLoading()
        homeModel.getArticles(curPage, object : Callback<HttpResult<ArticlesListBean>, String> {
            override fun onSuccess(result: HttpResult<ArticlesListBean>) {
                if (isViewAttached()) {
                    mView?.hideLoading()
                    mView?.setArticles(result.data)
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