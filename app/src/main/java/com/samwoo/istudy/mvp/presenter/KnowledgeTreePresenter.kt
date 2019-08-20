package com.samwoo.istudy.mvp.presenter

import com.samwoo.istudy.base.BasePresenter
import com.samwoo.istudy.bean.HttpResult
import com.samwoo.istudy.bean.KnowledgeTreeBody
import com.samwoo.istudy.callback.Callback
import com.samwoo.istudy.mvp.contract.KnowledgeTreeContract
import com.samwoo.istudy.mvp.model.KnowledgeTreeModel
import com.samwoo.istudy.util.SLog
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class KnowledgeTreePresenter : BasePresenter<KnowledgeTreeContract.View>(), KnowledgeTreeContract.Presenter {
    private val knowledgeTreeModel by lazy {
        KnowledgeTreeModel()
    }

    override fun getKnowledgeTree() {
        mView?.showLoading()
        knowledgeTreeModel.getKnowledgeTree(object : Callback<HttpResult<List<KnowledgeTreeBody>>, String> {
            override fun onSuccess(result: HttpResult<List<KnowledgeTreeBody>>) {
                SLog.d("Sam", "result--->${result.data}")
                if (isViewAttached()) {
                    mView?.apply {
                        hideLoading()
                        setKnowledgeTree(result.data)
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