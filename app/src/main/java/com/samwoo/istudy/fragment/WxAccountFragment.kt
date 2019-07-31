package com.samwoo.istudy.fragment

import com.google.android.material.tabs.TabLayout
import com.samwoo.istudy.R
import com.samwoo.istudy.adapter.WxAccountPagerAdapter
import com.samwoo.istudy.base.BaseFragment
import com.samwoo.istudy.bean.WxAccountBody
import com.samwoo.istudy.mvp.contract.WxAccountContract
import com.samwoo.istudy.mvp.presenter.WxAccountPresenter
import kotlinx.android.synthetic.main.fragment_tab_viewpager.*
import org.jetbrains.anko.toast

class WxAccountFragment : BaseFragment(), WxAccountContract.View {

    companion object {
        fun instance(): WxAccountFragment = WxAccountFragment()
    }

    private val mPresenter: WxAccountPresenter by lazy {
        WxAccountPresenter()
    }

    private val wxAccount = mutableListOf<WxAccountBody>()

    private val viewPagerAdapter: WxAccountPagerAdapter by lazy {
        WxAccountPagerAdapter(fragmentManager!!, wxAccount)
    }

    override fun getLayoutResId(): Int = R.layout.fragment_tab_viewpager

    override fun initView() {
        mPresenter.attachView(this)

        tabLayout.run {
            setupWithViewPager(viewPager)
            addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(viewPager))
        }

        viewPager.run {
            addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
        }
    }

    override fun lazyLoad() {
        mPresenter.getWxAccount()
    }

    override fun scrollToTop() {
        if (viewPagerAdapter.count == 0) return
        val fragment: ArticlesFragment = viewPagerAdapter.getItem(viewPager.currentItem) as ArticlesFragment
        fragment.scrollToTop()
    }

    override fun setWxAccount(data: List<WxAccountBody>) {
        data.let {
            wxAccount.addAll(data)
            viewPager.run {
                adapter = viewPagerAdapter
                offscreenPageLimit = wxAccount.size
            }
        }
    }

    override fun showLoading() {}

    override fun hideLoading() {}

    override fun showError(errorMsg: String) {
        activity?.toast(errorMsg)
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }
}