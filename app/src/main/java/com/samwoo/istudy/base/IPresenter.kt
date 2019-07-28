package com.samwoo.istudy.base

interface IPresenter<T : IView> {
    /**
     * 绑定View
     *
     * @param view
     */
    fun attachView(view: T)
    /**
     * 解绑View
     */
    fun detachView()
    /**
     * 判断View是否已经销毁
     *
     * @return
     */
    fun isViewAttached(): Boolean
}