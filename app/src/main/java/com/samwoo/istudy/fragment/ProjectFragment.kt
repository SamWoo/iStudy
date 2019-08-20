package com.samwoo.istudy.fragment

import android.util.Log
import com.google.android.material.tabs.TabLayout
import com.samwoo.istudy.BuildConfig
import com.samwoo.istudy.R
import com.samwoo.istudy.adapter.ProjectPagerAdapter
import com.samwoo.istudy.base.BaseFragment
import com.samwoo.istudy.bean.ProjectTreeBody
import com.samwoo.istudy.mvp.contract.ProjectTreeContract
import com.samwoo.istudy.mvp.presenter.ProjectTreePresenter
import kotlinx.android.synthetic.main.fragment_tab_viewpager.*

class ProjectFragment : BaseFragment(), ProjectTreeContract.View {
    companion object {
        fun instance(): ProjectFragment = ProjectFragment()
    }

    private var mPresenter: ProjectTreePresenter? = null

    private val projectTree = mutableListOf<ProjectTreeBody>()

    private val viewPagerAdapter: ProjectPagerAdapter by lazy {
        ProjectPagerAdapter(childFragmentManager!!, projectTree)
    }

    override fun getLayoutResId(): Int {
        return R.layout.fragment_tab_viewpager
    }

    override fun initView() {
        mPresenter = ProjectTreePresenter()
        mPresenter?.attachView(this)

        tabLayout.run {
            setupWithViewPager(viewPager)
            addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(viewPager))
        }

        viewPager.run {
            addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
        }

    }

    override fun lazyLoad() {
        mPresenter?.getProjectTree()
    }

    override fun scrollToTop() {
        if (viewPagerAdapter.count == 0) return
        val fragment: ProjectListFragment = viewPagerAdapter.getItem(viewPager.currentItem) as ProjectListFragment
        fragment.scrollToTop()
    }

    override fun setProjectTree(data: List<ProjectTreeBody>) {
        data.let {
            projectTree.addAll(it)
            viewPager.run {
                adapter = viewPagerAdapter
//                offscreenPageLimit = projectTree.size
            }
        }
    }

    override fun doReConnect() {
        //如果projectTree不为null则说明非第一次进入，此时恢复网络连接不可再次请求数据，否则会造成“越界”问题
        if (projectTree.isEmpty()) super.doReConnect()
    }

    override fun showLoading() {
        loadingDialog.show()
    }

    override fun hideLoading() {
        loadingDialog.hide()
    }

    override fun showError(errorMsg: String) {
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mPresenter?.detachView()
        mPresenter = null
        if (BuildConfig.DEBUG) Log.d("Sam", "ProjectFragment onDestroyView....")
    }
}