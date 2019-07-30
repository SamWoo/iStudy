package com.samwoo.istudy.mvp.presenter

import com.samwoo.istudy.base.BasePresenter
import com.samwoo.istudy.bean.HttpResult
import com.samwoo.istudy.bean.ProjectTreeBody
import com.samwoo.istudy.callback.Callback
import com.samwoo.istudy.mvp.contract.ProjectTreeContract
import com.samwoo.istudy.mvp.model.ProjectTreeModel

class ProjectTreePresenter : BasePresenter<ProjectTreeContract.View>(), ProjectTreeContract.Presenter {
    private val projectTreeModel: ProjectTreeModel by lazy {
        ProjectTreeModel()
    }

    override fun getProjectTree() {
        projectTreeModel.getProjectTree(object : Callback<HttpResult<List<ProjectTreeBody>>, String> {
            override fun onSuccess(result: HttpResult<List<ProjectTreeBody>>) {
                mView?.setProjectTree(result.data)
                mView?.hideLoading()
            }

            override fun onFail(msg: String) {
                mView?.hideLoading()
                mView?.showError(msg)
            }

        })
    }

}