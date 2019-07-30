package com.samwoo.istudy.fragment

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.chad.library.adapter.base.BaseQuickAdapter
import com.samwoo.istudy.R
import com.samwoo.istudy.activity.ContentActivity
import com.samwoo.istudy.adapter.ProjectListAdapter
import com.samwoo.istudy.base.BaseFragment
import com.samwoo.istudy.bean.Article
import com.samwoo.istudy.constant.Constant
import com.samwoo.istudy.mvp.contract.ProjectListContract
import com.samwoo.istudy.mvp.presenter.ProjectListPresenter
import com.samwoo.istudy.widget.SpaceItemDecoration
import kotlinx.android.synthetic.main.fragment_refresh_layout.*
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.toast

class ProjectListFragment : BaseFragment(), ProjectListContract.View {

    private var cid: Int = -1
    private var isRefresh = true

    companion object {
        fun instance(cid: Int): ProjectListFragment {
            val fragment = ProjectListFragment()
            val args = Bundle()
            args.putInt(Constant.CONTENT_CID_KEY, cid)
            fragment.arguments = args
            return fragment
        }
    }

    private val mPresenter: ProjectListPresenter by lazy {
        ProjectListPresenter()
    }

    private val datas = mutableListOf<Article>()

    private val projectListAdapter: ProjectListAdapter by lazy {
        ProjectListAdapter(activity, datas)
    }

    //RecyclerView Divider
    private val recyclerViewItemDecoration by lazy {
        activity?.let { SpaceItemDecoration(it) }
    }

    private val linearLayoutManager: LinearLayoutManager by lazy {
        LinearLayoutManager(activity)
    }

    override fun getLayoutResId(): Int {
        return R.layout.fragment_refresh_layout
    }

    override fun initView() {
        mPresenter.attachView(this)
        cid = arguments!!.getInt(Constant.CONTENT_CID_KEY) ?: -1

        swipeRefreshLayout.run {
            isRefreshing = true
            if (Build.VERSION.SDK_INT >= 23) {
                setColorSchemeColors(
                    resources.getColor(R.color.Pink),
                    resources.getColor(R.color.Deep_Orange),
                    resources.getColor(R.color.Blue)
                )
                setProgressBackgroundColorSchemeColor(resources.getColor(R.color.white))
            }
            setOnRefreshListener(onRefreshListener)
        }

        recyclerView.run {
            layoutManager = linearLayoutManager
            addItemDecoration(recyclerViewItemDecoration!!)
            itemAnimator = DefaultItemAnimator()
            adapter = projectListAdapter
        }

        projectListAdapter.run {
            setOnLoadMoreListener(onRequestLoadMoreListener, recyclerView)
            onItemClickListener = this@ProjectListFragment.onItemClickListener
            onItemChildClickListener = this@ProjectListFragment.onItemChildClickListener
            setEmptyView(R.layout.fragment_empty)
        }
    }

    private val onRefreshListener = SwipeRefreshLayout.OnRefreshListener {
        isRefresh = true
        mPresenter.getProjectList(1, cid)
    }

    private val onRequestLoadMoreListener = BaseQuickAdapter.RequestLoadMoreListener {
        isRefresh = false
        swipeRefreshLayout.isRefreshing = false
        val page = datas.size / 20 + 1
        mPresenter.getProjectList(page, cid)
    }

    private val onItemClickListener = BaseQuickAdapter.OnItemClickListener { _, _, position ->
        if (datas.size != 0) {
            val data = datas[position]
            val intent = activity!!.intentFor<ContentActivity>(
                Pair(Constant.CONTENT_URL_KEY, data.link),
                Pair(Constant.CONTENT_TITLE_KEY, data.title),
                Pair(Constant.CONTENT_ID_KEY, data.id)
            )
            startActivity(intent)
        }
    }

    private val onItemChildClickListener = BaseQuickAdapter.OnItemChildClickListener { _, _, position ->
        if (datas.size != 0) {
            val data = datas[position]
            activity?.toast("${data}")
        }
    }

    override fun lazyLoad() {
        mPresenter.getProjectList(1, cid)
    }

    override fun scrollTop() {
        recyclerView.run {
            if (linearLayoutManager.findFirstCompletelyVisibleItemPosition() > 20) {
                scrollToPosition(0)
            } else {
                smoothScrollToPosition(0)
            }
        }
    }

    override fun setProjectList(list: List<Article>) {
        list.let {
            projectListAdapter.run {
                if (isRefresh) {
                    replaceData(it)
                } else {
                    addData(it)
                }
                val size = it.size
                if (size < list.size) {
                    loadMoreEnd()
                } else {
                    loadMoreComplete()
                }
            }
        }
    }

    override fun showLoading() {
        swipeRefreshLayout.isRefreshing = isRefresh
    }

    override fun hideLoading() {
        swipeRefreshLayout.isRefreshing = false
        if (isRefresh) {
            projectListAdapter.run {
                setEnableLoadMore(true)
            }
        }
    }

    override fun showError(errorMsg: String) {
        projectListAdapter.run {
            if (isRefresh) {
                setEnableLoadMore(true)
            } else {
                loadMoreFail()
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }
}