package com.samwoo.istudy.fragment

import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.chad.library.adapter.base.BaseQuickAdapter
import com.samwoo.istudy.App
import com.samwoo.istudy.BuildConfig
import com.samwoo.istudy.R
import com.samwoo.istudy.activity.ContentActivity
import com.samwoo.istudy.activity.LoginActivity
import com.samwoo.istudy.adapter.ArticlesAdapter
import com.samwoo.istudy.base.BaseFragment
import com.samwoo.istudy.bean.Article
import com.samwoo.istudy.bean.ArticlesListBean
import com.samwoo.istudy.constant.Constant
import com.samwoo.istudy.mvp.contract.ArticlesContract
import com.samwoo.istudy.mvp.contract.CollectContract
import com.samwoo.istudy.mvp.presenter.ArticlesPresenter
import com.samwoo.istudy.mvp.presenter.CollectPresenter
import com.samwoo.istudy.util.NetworkUtil
import com.samwoo.istudy.view.LoadingDialog
import com.samwoo.istudy.widget.SpaceItemDecoration
import kotlinx.android.synthetic.main.fragment_refresh_layout.*
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.toast

/**
 * 1. 知识体系下的文章  https://www.wanandroid.com/article/list/0/json?cid=60
 * 2. 查看某个公众号历史数据  https://wanandroid.com/wxarticle/list/408/1/json
 * 因1,2
 */

class ArticlesFragment : BaseFragment(), ArticlesContract.View, CollectContract.View {
    companion object {
        fun instance(cid: Int): ArticlesFragment {
            val fragment = ArticlesFragment()
            val args = Bundle()
            args.putInt(Constant.CONTENT_CID_KEY, cid)
            fragment.arguments = args
            return fragment
        }
    }

    private var cid: Int = 0

    private var mPresenter: ArticlesPresenter? = null
    private var collectPresenter: CollectPresenter? = null

    private var datas = mutableListOf<Article>()

    private var isRefresh = true

    //RecyclerView Divider
    private val recyclerViewItemDecoration by lazy {
        activity?.let { SpaceItemDecoration(it) }
    }

    private val linearLayoutManager = LinearLayoutManager(activity)

    private val articlesAdapter: ArticlesAdapter by lazy {
        ArticlesAdapter(activity, datas)
    }

    override fun getLayoutResId(): Int {
        return R.layout.fragment_refresh_layout
    }

    override fun initView() {
        mPresenter = ArticlesPresenter()
        mPresenter?.attachView(this)
        collectPresenter = CollectPresenter()
        collectPresenter?.attachView(this)
        cid = arguments!!.getInt(Constant.CONTENT_CID_KEY) ?: 0

        swipeRefreshLayout.run {
            //            isRefreshing = true
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
            adapter = articlesAdapter
        }

        articlesAdapter.run {
            bindToRecyclerView(recyclerView)
            openLoadAnimation(BaseQuickAdapter.SLIDEIN_LEFT)
            setOnLoadMoreListener(onRequestLoadMoreListener, recyclerView)
            onItemClickListener = this@ArticlesFragment.onItemClickListener
            onItemChildClickListener = this@ArticlesFragment.onItemChildClickListener
            setEmptyView(R.layout.layout_empty)
        }

    }

    private val onRefreshListener = SwipeRefreshLayout.OnRefreshListener {
        swipeRefreshLayout.isRefreshing = false
        isRefresh = true
        mPresenter?.getArticleList(0, cid)
    }

    private val onRequestLoadMoreListener = BaseQuickAdapter.RequestLoadMoreListener {
        isRefresh = false
        swipeRefreshLayout.isRefreshing = false
        val page = datas.size / 20
        mPresenter?.getArticleList(page, cid)
    }

    private val onItemClickListener = BaseQuickAdapter.OnItemClickListener { _, _, position ->
        if (datas.size != 0) {
            val data = datas[position]
            val intent = activity?.intentFor<ContentActivity>(
                Pair(Constant.CONTENT_URL_KEY, data.link),
                Pair(Constant.CONTENT_TITLE_KEY, data.title),
                Pair(Constant.CONTENT_ID_KEY, data.id)
            )
            startActivity(intent)
        }
    }

    private val onItemChildClickListener =
        BaseQuickAdapter.OnItemChildClickListener { _, view, position ->
            if (datas.size != 0) {
                val data = datas[position]
//            activity?.toast("${data}")
                when (view.id) {
                    R.id.iv_like -> {
                        if (isLogin) {
                            if (!NetworkUtil.isNetworkAvailable(App.context)) {
                                activity?.toast("网络不可用!!")
                                return@OnItemChildClickListener
                            }
                            val collect = data.collect
                            data.collect = !collect
                            articlesAdapter.setData(position, data)
                            when (collect) {
                                true -> collectPresenter?.cancleCollectArticle(data.id)
                                else -> collectPresenter?.addCollectArticle(data.id)
                            }
                        }else{
                            val intent=activity!!.intentFor<LoginActivity>()
                            startActivity(intent)
                        }
                    }
                }
            }
        }

    override fun lazyLoad() {
        mPresenter?.getArticleList(0, cid)
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

    override fun setArticles(list: ArticlesListBean) {
        list.datas.let {
            articlesAdapter.run {
                if (isRefresh) {
                    replaceData(it)
                } else {
                    addData(it)
                }

                val size = it.size
                if (size < list.size) {
                    loadMoreEnd(false)
                } else {
                    loadMoreComplete()
                }
            }
        }
    }

    override fun showLoading() {
//        swipeRefreshLayout.isRefreshing = isRefresh
        loadingDialog?.show()
    }

    override fun hideLoading() {
//        swipeRefreshLayout.isRefreshing = false
        loadingDialog?.hide()
        if (isRefresh) {
            articlesAdapter.setEnableLoadMore(true)
        }
    }

    override fun showError(errorMsg: String) {
        articlesAdapter.run {
            if (isRefresh) {
                setEnableLoadMore(true)
            } else {
                loadMoreFail()
            }
        }
    }

    override fun cancleCollectFail() {
        activity?.toast("取消失败!!")
    }

    override fun cancleCollectSuccess() {
        activity?.toast("取消成功!!")
    }

    override fun collectFail() {
        activity?.toast("收藏失败!!")
    }

    override fun collectSuccess() {
        activity?.toast("收藏成功!!")
    }

    override fun showCollectList(data: ArticlesListBean) {}

    override fun onDestroyView() {
        super.onDestroyView()
        mPresenter?.detachView()
        mPresenter = null
        collectPresenter?.detachView()
        collectPresenter = null
        if (BuildConfig.DEBUG) Log.d("Sam", "ArticlesFragment DestroyView.....")
    }
}