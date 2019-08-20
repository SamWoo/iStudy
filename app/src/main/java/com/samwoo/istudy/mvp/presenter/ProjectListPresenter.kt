package com.samwoo.istudy.mvp.presenter

import com.samwoo.istudy.base.BasePresenter
import com.samwoo.istudy.bean.ArticlesListBean
import com.samwoo.istudy.bean.HttpResult
import com.samwoo.istudy.callback.Callback
import com.samwoo.istudy.mvp.contract.ProjectListContract
import com.samwoo.istudy.mvp.model.ProjectListModel
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class ProjectListPresenter : BasePresenter<ProjectListContract.View>(), ProjectListContract.Presenter {
    private val projectListModel: ProjectListModel by lazy {
        ProjectListModel()
    }

    override fun getProjectList(curPage: Int, cid: Int) {
        mView?.showLoading()
        projectListModel.getProjectList(curPage, cid, object : Callback<HttpResult<ArticlesListBean>, String> {
            override fun onSuccess(result: HttpResult<ArticlesListBean>) {
                if (isViewAttached()) {
                    mView?.apply {
                        hideLoading()
                        setProjectList(result.data)
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