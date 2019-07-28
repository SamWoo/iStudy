package com.samwoo.istudy.base

interface IView {
    fun showLoading()

    fun hideLoading()

    fun showError(errorMsg: String)
}