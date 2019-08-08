package com.samwoo.istudy.mvp.presenter

import com.samwoo.istudy.base.BasePresenter
import com.samwoo.istudy.bean.HttpResult
import com.samwoo.istudy.bean.NavigationBean
import com.samwoo.istudy.callback.Callback
import com.samwoo.istudy.mvp.contract.NavigationContract
import com.samwoo.istudy.mvp.model.NavigationModel

class NavigationPresenter : BasePresenter<NavigationContract.View>(), NavigationContract.Presenter {
    private val model: NavigationModel by lazy {
        NavigationModel()
    }

    override fun getNavList() {
        model.getNavList(object : Callback<HttpResult<List<NavigationBean>>, String> {
            override fun onSuccess(data: HttpResult<List<NavigationBean>>) {
                if (isViewAttached()) {
                    mView?.run {
                        showLoading()
                        setNavList(data.data)
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