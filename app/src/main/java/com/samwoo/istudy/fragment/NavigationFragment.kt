package com.samwoo.istudy.fragment

import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.samwoo.istudy.R
import com.samwoo.istudy.adapter.NavTabAdapter
import com.samwoo.istudy.adapter.NaviAdapter
import com.samwoo.istudy.base.BaseFragment
import com.samwoo.istudy.bean.NavigationBean
import com.samwoo.istudy.mvp.contract.NavigationContract
import com.samwoo.istudy.mvp.presenter.NavigationPresenter
import kotlinx.android.synthetic.main.fragment_nav.*
import q.rorbin.verticaltablayout.VerticalTabLayout
import q.rorbin.verticaltablayout.widget.TabView

class NavigationFragment : BaseFragment(), NavigationContract.View {

    companion object {
        fun instance(): NavigationFragment {
            return NavigationFragment()
        }
    }

    private var mPresenter: NavigationPresenter? = null

    private var datas = mutableListOf<NavigationBean>()

    private val navTabAdapter: NavTabAdapter by lazy {
        NavTabAdapter(activity, datas)
    }

    private val naviAdapter: NaviAdapter by lazy {
        NaviAdapter(activity, datas)
    }

    private val linearLayoutManager: LinearLayoutManager by lazy {
        LinearLayoutManager(activity)
    }


    override fun getLayoutResId(): Int {
        return R.layout.fragment_nav
    }

    override fun initView() {
        mPresenter = NavigationPresenter()
        mPresenter?.attachView(this)

        nav_tab.apply {
            //            setTabAdapter(navTabAdapter)
            addOnTabSelectedListener(onTabSelectedListener)
        }

        recyclerView.apply {
            layoutManager = linearLayoutManager
            itemAnimator = DefaultItemAnimator()
            recyclerView.adapter = naviAdapter
            addOnScrollListener(onScrollListener)
        }

        naviAdapter.run {
            bindToRecyclerView(recyclerView)
            openLoadAnimation(BaseQuickAdapter.SLIDEIN_BOTTOM)
        }
    }

    //vertical_tab栏和RecyclerView联动
    private val onTabSelectedListener = object : VerticalTabLayout.OnTabSelectedListener {
        override fun onTabSelected(tab: TabView?, position: Int) {
            linearLayoutManager.scrollToPositionWithOffset(position, 0)
        }

        override fun onTabReselected(tab: TabView?, position: Int) {}
    }

    //RecyclerView和tab栏联动
    private val onScrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            nav_tab.setTabSelected(linearLayoutManager.findFirstVisibleItemPosition())
        }
    }

    override fun lazyLoad() {
        mPresenter?.getNavList()

    }

    override fun setNavList(list: List<NavigationBean>) {
        list.let {
            nav_tab.setTabAdapter(NavTabAdapter(activity, it))
            naviAdapter.replaceData(it)
        }
    }

    override fun scrollToTop() {
        recyclerView.run {
            if (linearLayoutManager.findFirstCompletelyVisibleItemPosition() > 10) scrollToPosition(0)
            else smoothScrollToPosition(0)
        }
        nav_tab.run {
            setTabSelected(0)
        }
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
        if (mPresenter != null) {
            mPresenter?.detachView()
            mPresenter = null
        }
    }
}