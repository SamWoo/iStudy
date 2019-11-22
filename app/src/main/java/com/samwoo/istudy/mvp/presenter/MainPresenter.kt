package com.samwoo.istudy.mvp.presenter

import com.samwoo.istudy.base.BasePresenter
import com.samwoo.istudy.bean.HttpResult
import com.samwoo.istudy.bean.UserInfo
import com.samwoo.istudy.callback.Callback
import com.samwoo.istudy.constant.Constant
import com.samwoo.istudy.mvp.contract.MainContract
import com.samwoo.istudy.mvp.model.MainModel
import com.samwoo.istudy.util.Preference
import com.samwoo.istudy.util.SLog
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class MainPresenter :BasePresenter<MainContract.View>(),MainContract.Presenter{
    //userinfo
    private var level: Int by Preference(Constant.LEVEL_KEY, 1)
    private var rank: Int by Preference(Constant.RANK_KEY, 1)
    private var coinCount: Int by Preference(Constant.COIN_KEY, 1)

    private val model: MainModel by lazy { MainModel() }

    override fun getUserInfo() {
        model.getUserInfo(object : Callback<HttpResult<UserInfo>, String> {
            override fun onSuccess(data: HttpResult<UserInfo>) {
                SLog.d("UserInfo-----> ${data.data}")
                level = data.data.level
                rank = data.data.rank
                coinCount = data.data.coinCount
                if (isViewAttached()) mView?.getUserInfoSuccess()
            }

            override fun onFail(msg: String) {
                if (isViewAttached()) {
                    doAsync { uiThread { mView?.showError(msg) } }
                }
            }
        })
    }

}