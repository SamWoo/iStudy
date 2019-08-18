package com.samwoo.istudy.mvp.presenter

import android.util.Log
import com.samwoo.istudy.base.BasePresenter
import com.samwoo.istudy.bean.ArticlesListBean
import com.samwoo.istudy.bean.BannerList
import com.samwoo.istudy.bean.HttpResult
import com.samwoo.istudy.callback.Callback
import com.samwoo.istudy.mvp.contract.HomeContract
import com.samwoo.istudy.mvp.model.HomeModel
import com.samwoo.istudy.util.SLog

class HomePresenter : BasePresenter<HomeContract.View>(), HomeContract.Presenter {

    private val homeModel: HomeModel by lazy {
        HomeModel()
    }

    override fun getBanners() {
        homeModel.getBanners(object : Callback<BannerList, String> {
            override fun onSuccess(data: BannerList) {
                if (isViewAttached()) {
                    SLog.d("banner=${data}")
                    mView?.setBanner(data)
                    mView?.hideLoading()
                }
            }

            override fun onFail(data: String) {
                if (isViewAttached()) {
                    mView?.showError(data)
                }
            }

        })
    }

    override fun getArticles(curPage: Int) {
        homeModel.getArticles(curPage, object : Callback<HttpResult<ArticlesListBean>, String> {
            override fun onSuccess(result: HttpResult<ArticlesListBean>) {
                if (isViewAttached()) {
                    SLog.d("articles=${result}")
                    mView?.setArticles(result.data)
                    mView?.hideLoading()
                }
            }

            override fun onFail(msg: String) {
                if (isViewAttached()) {
                    mView?.showError(msg)
                }
            }

        })
    }

}