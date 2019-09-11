package com.samwoo.istudy.fragment

import android.os.Build
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import cn.bingoogolapple.bgabanner.BGABanner
import com.chad.library.adapter.base.BaseQuickAdapter
import com.samwoo.istudy.App
import com.samwoo.istudy.BuildConfig
import com.samwoo.istudy.R
import com.samwoo.istudy.activity.ContentActivity
import com.samwoo.istudy.activity.LoginActivity
import com.samwoo.istudy.adapter.HomeAdapter
import com.samwoo.istudy.base.BaseFragment
import com.samwoo.istudy.bean.Article
import com.samwoo.istudy.bean.ArticlesListBean
import com.samwoo.istudy.bean.Banner
import com.samwoo.istudy.constant.Constant
import com.samwoo.istudy.mvp.contract.CollectContract
import com.samwoo.istudy.mvp.contract.HomeContract
import com.samwoo.istudy.mvp.presenter.CollectPresenter
import com.samwoo.istudy.mvp.presenter.HomePresenter
import com.samwoo.istudy.util.ImageLoader
import com.samwoo.istudy.util.NetworkUtil
import com.samwoo.istudy.view.MsgView
import com.samwoo.istudy.widget.SpaceItemDecoration
import kotlinx.android.synthetic.main.fragment_refresh_layout.*
import kotlinx.android.synthetic.main.item_home_banner.view.*
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.toast

class HomeFragment : BaseFragment(), HomeContract.View, CollectContract.View {
    companion object {
        fun instance(): HomeFragment = HomeFragment()
    }

    private val articles = mutableListOf<Article>()
    private var banners = listOf<Banner>()
    private var bannerView: View? = null
    private var isRefresh = true
    //LinearLayoutManager
    private val linearLayoutManager = LinearLayoutManager(activity)

    //homeAdapter
    private val homeAdapter: HomeAdapter by lazy {
        HomeAdapter(activity, articles)
    }

    //bannerAdapter
    private val bannerAdapter: BGABanner.Adapter<ImageView, String> by lazy {
        BGABanner.Adapter<ImageView, String> { _, itemView, model, _ ->
            ImageLoader.load(activity, model, itemView)
        }
    }

    //RecyclerView Divider
    private val recyclerViewItemDecoration by lazy {
        activity?.let { SpaceItemDecoration(it) }
    }

    private var mPresenter: HomePresenter? = null
    //collect
    private var collectPresenter: CollectPresenter? = null

    override fun getLayoutResId(): Int = R.layout.fragment_refresh_layout

    override fun lazyLoad() {
        mPresenter?.getBanners()
        mPresenter?.getArticles(0)
    }

    override fun initView() {
        mPresenter = HomePresenter()
        mPresenter?.attachView(this)
        collectPresenter = CollectPresenter()
        collectPresenter?.attachView(this)

        swipeRefreshLayout.run {
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
            adapter = homeAdapter
            itemAnimator = DefaultItemAnimator()
            addItemDecoration(recyclerViewItemDecoration!!)
        }

        bannerView = layoutInflater.inflate(R.layout.item_home_banner, null)
        bannerView?.banner?.run {
            setDelegate(bannerDelegate)
        }

        homeAdapter.run {
            bindToRecyclerView(recyclerView)
            openLoadAnimation(BaseQuickAdapter.SLIDEIN_RIGHT)
            setOnLoadMoreListener(onRequestLoadMoreListener, recyclerView)
            onItemClickListener = this@HomeFragment.onItemClickListener
            onItemChildClickListener = this@HomeFragment.onItemChildClickListener
            addHeaderView(bannerView)
        }
        MsgView.showLoadView(context!!, homeAdapter)
    }

    //BannerClickListener
    private val bannerDelegate = BGABanner.Delegate<ImageView, String> { _, _, _, position ->
        if (banners.size > 0) {
            val data = banners[position]
            val intent = activity!!.intentFor<ContentActivity>(
                Pair(Constant.CONTENT_URL_KEY, data.url),
                Pair(Constant.CONTENT_TITLE_KEY, data.title),
                Pair(Constant.CONTENT_ID_KEY, data.id)
            )
            startActivity(intent)
        }
    }

    //RefreshListener
    private val onRefreshListener = SwipeRefreshLayout.OnRefreshListener {
        isRefresh = true
        mPresenter?.getBanners()
        mPresenter?.getArticles(0)

    }

    //RequestMoreListener
    private val onRequestLoadMoreListener = BaseQuickAdapter.RequestLoadMoreListener {
        isRefresh = false
        swipeRefreshLayout.isRefreshing = false
        val page = homeAdapter.data.size / 20
        mPresenter?.getArticles(page)

    }

    //ItemClickListener
    private val onItemClickListener = BaseQuickAdapter.OnItemClickListener { _, _, position ->
        if (articles.size != 0) {
            val data = articles[position]
            val intent = activity!!.intentFor<ContentActivity>(
                Pair(Constant.CONTENT_URL_KEY, data.link),
                Pair(Constant.CONTENT_TITLE_KEY, data.title),
                Pair(Constant.CONTENT_ID_KEY, data.id)
            )
            startActivity(intent)
        }
    }

    //ItemChildClickListener
    private val onItemChildClickListener =
        BaseQuickAdapter.OnItemChildClickListener { _, view, position ->
            if (articles.size != 0) {
                val data = articles[position]
                when (view.id) {
                    R.id.iv_like -> {
                        if (isLogin) {
                            if (!NetworkUtil.isNetworkAvailable(App.context)) {
                                activity?.toast("网络未连接!!")
                                return@OnItemChildClickListener
                            }
                            val collect = data.collect
                            data.collect = !collect
                            homeAdapter.setData(position, data)
                            when (collect) {
                                true -> collectPresenter?.cancleCollectArticle(data.id)
                                else -> collectPresenter?.addCollectArticle(data.id)
                            }
                        } else {
                            val intent = activity!!.intentFor<LoginActivity>()
                            startActivity(intent)
                        }
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

    override fun setBanner(data: List<Banner>) {
        this.banners = data
        val bannerFeedList = ArrayList<String>()
        val bannerTitleList = ArrayList<String>()
        for (item in banners) {
            bannerFeedList.add(item.imagePath)
            bannerTitleList.add(item.title)
        }
        bannerView?.banner?.run {
            setAutoPlayAble(bannerFeedList.size > 1)
            setData(bannerFeedList, bannerTitleList)
            setAdapter(bannerAdapter)
        }
    }

    override fun setArticles(result: ArticlesListBean) {
        result.datas.let {
            homeAdapter.run {
                if (isRefresh) {
                    replaceData(it)
                } else {
                    addData(it)
                }
                val size = it.size
                if (size < result.size) {
                    loadMoreEnd(false)
                } else {
                    loadMoreComplete()
                }
            }
        }
    }

    override fun showLoading() {
//        swipeRefreshLayout.isRefreshing = isRefresh
    }

    override fun hideLoading() {
        swipeRefreshLayout?.isRefreshing = false
        if (isRefresh) {
            homeAdapter.run {
                setEnableLoadMore(true)
            }
        }
    }

    override fun showError(errorMsg: String) {
        homeAdapter.run {
            if (isRefresh) {
                setEnableLoadMore(true)
                MsgView.showErrorView(context!!, homeAdapter, "加载失败...o(╥﹏╥)o")
            } else {
                loadMoreFail()
            }
        }
    }

    override fun cancleCollectFail() {
        App.context.toast("取消失败!!")
    }

    override fun cancleCollectSuccess() {
        App.context.toast("取消成功!!")
    }

    override fun collectFail() {
        App.context.toast("收藏失败!!")
    }

    override fun collectSuccess() {
        App.context.toast("收藏成功!!")
    }

    override fun showCollectList(data: ArticlesListBean) {}

    override fun onDestroyView() {
        super.onDestroyView()
        mPresenter?.detachView()
        mPresenter = null
        collectPresenter?.detachView()
        collectPresenter = null
        if (BuildConfig.DEBUG) Log.d("Sam", "HomeFragment DestroyView....")
    }


}