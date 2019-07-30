package com.samwoo.istudy.fragment

import com.google.android.material.tabs.TabLayout
import com.samwoo.istudy.R
import com.samwoo.istudy.adapter.ProjectPagerAdapter
import com.samwoo.istudy.base.BaseFragment
import com.samwoo.istudy.bean.ProjectTreeBody
import com.samwoo.istudy.mvp.contract.ProjectTreeContract
import com.samwoo.istudy.mvp.presenter.ProjectTreePresenter
import kotlinx.android.synthetic.main.fragment_project.*

class ProjectFragment : BaseFragment(), ProjectTreeContract.View {
    companion object {
        fun instance() = ProjectFragment()
    }

    private val mPresenter: ProjectTreePresenter by lazy {
        ProjectTreePresenter()
    }

    private val projectTree = mutableListOf<ProjectTreeBody>()

    private val viewPagerAdapter: ProjectPagerAdapter by lazy {
        ProjectPagerAdapter(fragmentManager!!, projectTree)
    }

    override fun getLayoutResId(): Int {
        return R.layout.fragment_project
    }

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
        mPresenter.getProjectTree()
    }

    override fun scrollTop() {
        if (viewPagerAdapter.count == 0) return
    }

    override fun setProjectTree(data: List<ProjectTreeBody>) {
        data?.let {
            projectTree.addAll(it)
            viewPager.run {
                adapter = viewPagerAdapter
                offscreenPageLimit = projectTree.size
            }
        }
    }

    override fun showLoading() {
    }

    override fun hideLoading() {
    }

    override fun showError(errorMsg: String) {
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }
}