package com.samwoo.istudy.base

abstract class BasePresenter<T : IView> : IPresenter<T> {
    var mView: T? = null
    override fun attachView(view: T) {
        mView = view
    }

    override fun detachView() {
        mView = null
    }

    override fun isViewAttached(): Boolean {
        return mView != null
    }
}