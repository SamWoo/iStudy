package com.samwoo.istudy.fragment

import android.os.Build
import android.os.Bundle
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.chad.library.adapter.base.BaseQuickAdapter
import com.samwoo.istudy.R
import com.samwoo.istudy.activity.ContentActivity
import com.samwoo.istudy.adapter.WxArticlesAdapter
import com.samwoo.istudy.base.BaseFragment
import com.samwoo.istudy.bean.Article
import com.samwoo.istudy.bean.ArticlesListBean
import com.samwoo.istudy.constant.Constant
import com.samwoo.istudy.mvp.contract.WxArticlesContract
import com.samwoo.istudy.mvp.presenter.WxArticlesPresenter
import com.samwoo.istudy.widget.SpaceItemDecoration
import kotlinx.android.synthetic.main.fragment_refresh_layout.*
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.toast

class WxArticlesFragment : BaseFragment(), WxArticlesContract.View {
    companion object {
        fun instance(cid: Int): WxArticlesFragment {
            val fragment = WxArticlesFragment()
            val args = Bundle()
            args.putInt(Constant.CONTENT_CID_KEY, cid)
            fragment.arguments = args
            return fragment
        }
    }

    private var cid: Int = -1

    private val mPresenter: WxArticlesPresenter by lazy {
        WxArticlesPresenter()
    }

    private var datas = mutableListOf<Article>()

    private var isRefresh = true

    //RecyclerView Divider
    private val recyclerViewItemDecoration by lazy {
        activity?.let { SpaceItemDecoration(it) }
    }

    private val linearLayoutManager: LinearLayoutManager by lazy {
        LinearLayoutManager(activity)
    }

    private val wxArticlesAdapter: WxArticlesAdapter by lazy {
        WxArticlesAdapter(activity, datas)
    }

    override fun getLayoutResId(): Int {
        return R.layout.fragment_refresh_layout
    }

    override fun initView() {
        mPresenter.attachView(this)
        cid = arguments!!.getInt(Constant.CONTENT_CID_KEY)

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
            itemAnimator = DefaultItemAnimator()
            addItemDecoration(recyclerViewItemDecoration!!)
            adapter = wxArticlesAdapter
        }

        wxArticlesAdapter.run {
            bindToRecyclerView(recyclerView)
            openLoadAnimation(BaseQuickAdapter.SLIDEIN_LEFT)
            setOnLoadMoreListener(onRequestLoadMoreListener)
            onItemClickListener = this@WxArticlesFragment.onItemClickListener
            onItemChildClickListener = this@WxArticlesFragment.onItemChildClickListener
            setEmptyView(R.layout.fragment_empty)
        }

    }

    private val onRefreshListener = SwipeRefreshLayout.OnRefreshListener {
        isRefresh=true
        mPresenter.getWxArticles(id, 1)
    }

    private val onRequestLoadMoreListener = BaseQuickAdapter.RequestLoadMoreListener {
        isRefresh = false
        swipeRefreshLayout.isRefreshing = false
        val page = datas.size / 20 + 1
        mPresenter.getWxArticles(cid, page)
    }

    private val onItemClickListener = BaseQuickAdapter.OnItemClickListener { _, _, position ->
        val data = datas[position]
        val intent = activity?.intentFor<ContentActivity>(
                Pair(Constant.CONTENT_URL_KEY, data.link),
                Pair(Constant.CONTENT_TITLE_KEY, data.title),
                Pair(Constant.CONTENT_ID_KEY, data.id)
        )
        startActivity(intent)
    }

    private val onItemChildClickListener = BaseQuickAdapter.OnItemChildClickListener { _, _, position ->
        val data = datas[position]
        activity?.toast("${data}")
    }

    override fun lazyLoad() {
        mPresenter.getWxArticles(cid, 1)
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

    override fun setWxArticles(list: ArticlesListBean) {
        list?.datas.let {
            wxArticlesAdapter.run {
                if (isRefresh) {
                    replaceData(it)
                } else {
                    addData(it)
                }

                val size = it.size
                if (size < list.size) {
                    loadMoreEnd(true)
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
            wxArticlesAdapter.setEnableLoadMore(true)
        }
    }

    override fun showError(errorMsg: String) {
        wxArticlesAdapter.run {
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