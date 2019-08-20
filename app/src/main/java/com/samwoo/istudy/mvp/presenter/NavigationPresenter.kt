package com.samwoo.istudy.mvp.presenter

import com.samwoo.istudy.base.BasePresenter
import com.samwoo.istudy.bean.HttpResult
import com.samwoo.istudy.bean.NavigationBean
import com.samwoo.istudy.callback.Callback
import com.samwoo.istudy.mvp.contract.NavigationContract
import com.samwoo.istudy.mvp.model.NavigationModel
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class NavigationPresenter : BasePresenter<NavigationContract.View>(), NavigationContract.Presenter {
    private val model: NavigationModel by lazy {
        NavigationModel()
    }

    override fun getNavList() {
        mView?.showLoading()
        model.getNavList(object : Callback<HttpResult<List<NavigationBean>>, String> {
            override fun onSuccess(data: HttpResult<List<NavigationBean>>) {
                if (isViewAttached()) {
                    mView?.apply {
                        hideLoading()
                        setNavList(data.data)
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