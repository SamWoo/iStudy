package com.samwoo.istudy.fragment

import android.os.Build
import android.os.Bundle
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.samwoo.istudy.R
import com.samwoo.istudy.activity.ContentActivity
import com.samwoo.istudy.adapter.ArticlesAdapter
import com.samwoo.istudy.base.BaseFragment
import com.samwoo.istudy.bean.Article
import com.samwoo.istudy.bean.ArticlesListBean
import com.samwoo.istudy.constant.Constant
import com.samwoo.istudy.mvp.contract.SearchResultContract
import com.samwoo.istudy.mvp.presenter.SearchResultPresenter
import com.samwoo.istudy.widget.SpaceItemDecoration
import kotlinx.android.synthetic.main.fragment_refresh_layout.*
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.toast

class SearchResultFragment : BaseFragment(), SearchResultContract.View {
    companion object {
        fun instance(args: Bundle?): SearchResultFragment {
            val fragment = SearchResultFragment()
            fragment.arguments = args
            return fragment
        }

    }

    private var mPresenter: SearchResultPresenter? = null

    private var isRefresh = true //是否可刷新

    private var searchKey: String? = null
    private val datas = mutableListOf<Article>()

    private val searchResultAdapter: ArticlesAdapter by lazy {
        ArticlesAdapter(activity, datas)
    }

    private val itemDecoration by lazy {
        activity?.let { SpaceItemDecoration(it) }
    }

    private val linearLayoutManager: LinearLayoutManager by lazy {
        LinearLayoutManager(activity)
    }

    override fun getLayoutResId(): Int {
        return R.layout.fragment_refresh_layout
    }

    override fun initView() {
        mPresenter = SearchResultPresenter()
        mPresenter?.attachView(this)

        searchKey = arguments?.getString(Constant.SEARCH_KEY, "")

        swipeRefreshLayout.apply {
//            isRefreshing = true
            if (Build.VERSION.SDK_INT >= 23) {
                setColorSchemeColors(
                    resources.getColor(R.color.Pink),
                    resources.getColor(R.color.Deep_Orange),
                    resources.getColor(R.color.Blue)
                )
                setProgressBackgroundColorSchemeColor(resources.getColor(R.color.white))
            }
            setOnRefreshListener {
                isRefresh = true
                mPresenter?.getSearchResult(0, searchKey!!)
            }
        }

        recyclerView.apply {
            layoutManager = linearLayoutManager
            addItemDecoration(itemDecoration!!)
            itemAnimator = DefaultItemAnimator()
            adapter = searchResultAdapter
        }

        searchResultAdapter.run {
            bindToRecyclerView(recyclerView)
            openLoadAnimation(BaseQuickAdapter.SLIDEIN_BOTTOM)
            onItemClickListener = this@SearchResultFragment.onItemClickListener
            onItemChildClickListener = this@SearchResultFragment.onItemChildClickListener
            setOnLoadMoreListener(onRequestLoadMoreListener, recyclerView)
        }

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

    private val onItemChildClickListener = BaseQuickAdapter.OnItemChildClickListener { _, view, position ->
        val data = datas[position]
        when (view.id) {
            R.id.iv_like -> {
                activity?.toast("$data")
            }
        }

    }

    private val onRequestLoadMoreListener = BaseQuickAdapter.RequestLoadMoreListener {
        isRefresh = false
        swipeRefreshLayout.isRefreshing = false
        val page = datas.size / 20
        mPresenter?.getSearchResult(page, searchKey!!)
    }

    override fun lazyLoad() {
        mPresenter?.getSearchResult(0, searchKey!!)
    }

    override fun showSearchResult(data: ArticlesListBean) {
        data.datas.let {
            searchResultAdapter.run {
                when (isRefresh) {
                    true -> replaceData(it)
                    else -> addData(it)
                }

                val size = it.size
                if (size < data.size) {
                    loadMoreEnd(true)
                } else {
                    loadMoreComplete()
                }
            }
        }
    }

    override fun scrollToTop() {
        recyclerView.run {
            if (linearLayoutManager.findFirstCompletelyVisibleItemPosition() > 20) {
                scrollToPosition(0)
            } else {
                smoothScrollToPosition(0)
            }
        }
    }

    override fun showLoading() {
        swipeRefreshLayout.isRefreshing = isRefresh
    }

    override fun hideLoading() {
        swipeRefreshLayout.isRefreshing = false
        if (isRefresh) {
            searchResultAdapter.setEnableLoadMore(true)
        }
    }

    override fun showError(errorMsg: String) {
        activity?.toast("$errorMsg")
        searchResultAdapter.run {
            when (isRefresh) {
                true -> setEnableLoadMore(true)
                else -> loadMoreFail()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (mPresenter != null) {
            mPresenter?.detachView()
            mPresenter = null
        }
    }

}