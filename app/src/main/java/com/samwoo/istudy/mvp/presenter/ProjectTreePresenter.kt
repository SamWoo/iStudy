package com.samwoo.istudy.mvp.presenter

import com.samwoo.istudy.base.BasePresenter
import com.samwoo.istudy.bean.HttpResult
import com.samwoo.istudy.bean.ProjectTreeBody
import com.samwoo.istudy.callback.Callback
import com.samwoo.istudy.mvp.contract.ProjectTreeContract
import com.samwoo.istudy.mvp.model.ProjectTreeModel
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class ProjectTreePresenter : BasePresenter<ProjectTreeContract.View>(), ProjectTreeContract.Presenter {
    private val projectTreeModel: ProjectTreeModel by lazy {
        ProjectTreeModel()
    }

    override fun getProjectTree() {
        mView?.showLoading()
        projectTreeModel.getProjectTree(object : Callback<HttpResult<List<ProjectTreeBody>>, String> {
            override fun onSuccess(result: HttpResult<List<ProjectTreeBody>>) {
                if (isViewAttached()) {
                    mView?.apply {
                        hideLoading()
                        setProjectTree(result.data)
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