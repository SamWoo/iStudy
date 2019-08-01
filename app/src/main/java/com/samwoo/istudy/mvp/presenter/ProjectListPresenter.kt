package com.samwoo.istudy.mvp.presenter

import com.samwoo.istudy.base.BasePresenter
import com.samwoo.istudy.bean.ArticlesListBean
import com.samwoo.istudy.bean.HttpResult
import com.samwoo.istudy.callback.Callback
import com.samwoo.istudy.mvp.contract.ProjectListContract
import com.samwoo.istudy.mvp.model.ProjectListModel

class ProjectListPresenter : BasePresenter<ProjectListContract.View>(), ProjectListContract.Presenter {
    private val projectListModel: ProjectListModel by lazy {
        ProjectListModel()
    }

    override fun getProjectList(curPage: Int, cid: Int) {
        mView?.showLoading()
        projectListModel.getProjectList(curPage, cid, object : Callback<HttpResult<ArticlesListBean>, String> {
            override fun onSuccess(result: HttpResult<ArticlesListBean>) {
                if (isViewAttached()){
                    mView?.run {
                        setProjectList(result.data)
                        hideLoading()
                    }
                }
            }

            override fun onFail(msg: String) {
                if (isViewAttached()){
                    mView?.run {
                        showError(msg)
                        hideLoading()
                    }
                }
            }
        })
    }
}