package com.samwoo.istudy.fragment

import androidx.recyclerview.widget.LinearLayoutManager
import com.samwoo.istudy.R
import com.samwoo.istudy.adapter.NavTabAdapter
import com.samwoo.istudy.base.BaseFragment
import com.samwoo.istudy.bean.NavigationBean
import com.samwoo.istudy.mvp.contract.NavigationContract
import com.samwoo.istudy.mvp.presenter.NavigationPresenter
import kotlinx.android.synthetic.main.fragment_nav.*

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
            setTabAdapter(navTabAdapter)
        }

    }

    override fun lazyLoad() {
        mPresenter?.getNavList()
    }

    override fun setNavList(list: List<NavigationBean>) {
        list.let {
            nav_tab.setTabAdapter(NavTabAdapter(activity, it))
        }
    }

    override fun scrollToTop() {

    }

    override fun showLoading() {

    }

    override fun hideLoading() {

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