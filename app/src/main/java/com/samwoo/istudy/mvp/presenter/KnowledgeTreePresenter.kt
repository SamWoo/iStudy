package com.samwoo.istudy.mvp.presenter

import android.util.Log
import com.samwoo.istudy.BuildConfig
import com.samwoo.istudy.base.BasePresenter
import com.samwoo.istudy.bean.HttpResult
import com.samwoo.istudy.bean.KnowledgeTreeBody
import com.samwoo.istudy.callback.Callback
import com.samwoo.istudy.mvp.contract.KnowledgeTreeContract
import com.samwoo.istudy.mvp.model.KnowledgeTreeModel

class KnowledgeTreePresenter : BasePresenter<KnowledgeTreeContract.View>(), KnowledgeTreeContract.Presenter {
    private val knowledgeTreeModel by lazy {
        KnowledgeTreeModel()
    }

    override fun getKnowledgeTree() {
        knowledgeTreeModel.getKnowledgeTree(object : Callback<HttpResult<List<KnowledgeTreeBody>>, String> {
            override fun onSuccess(result: HttpResult<List<KnowledgeTreeBody>>) {
                if(BuildConfig.DEBUG) Log.d("Sam","result--->${result.data}")
                if (isViewAttached()){
                    mView?.run {
                        setKnowledgeTree(result.data)
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