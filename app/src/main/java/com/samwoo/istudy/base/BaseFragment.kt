package com.samwoo.istudy.base

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.samwoo.istudy.BuildConfig

abstract class BaseFragment : Fragment() {
    /**
     * View 的初始化状态，只有初始化完毕才加载数据
     */
    private var isViewInitiated: Boolean = false
    /**
     * View 能否可见，只有可见时才去加载数据
     */
    private var isVisibleToUser: Boolean = false
    /**
     * 数据能否已经初始化，避免重复请求数据
     */
    private var isDataInitiated: Boolean = false

    private var contentView: View? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (BuildConfig.DEBUG) Log.d("Sam", "Fragment createView......")
        /**
         * 以下代码主要解决viewpager中有多个fragment时预加载导致OOM leaked问题
         * 设置下面代码则无需再在viewpager中设置offscreenPageLimit的值
         */
        if (contentView != null) {
            (contentView?.parent as ViewGroup)?.removeView(contentView)
            return contentView
        }
        contentView = inflater?.inflate(getLayoutResId(), null)
        return contentView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isViewInitiated = true
        initView()
        prepareFetchData()
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        this.isVisibleToUser = isVisibleToUser
        prepareFetchData()
    }

    private fun prepareFetchData() {
        if (isViewInitiated && isViewInitiated && !isDataInitiated) {
            lazyLoad()
            isDataInitiated = true
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        isViewInitiated = true
        prepareFetchData()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    //获取fragment布局文件
    abstract fun getLayoutResId(): Int

    //初始化控件
    abstract fun initView()

    //lazyLoad
    abstract fun lazyLoad()
}