package com.samwoo.istudy.fragment

import android.os.Build
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import cn.bingoogolapple.bgabanner.BGABanner
import com.chad.library.adapter.base.BaseQuickAdapter
import com.samwoo.istudy.BuildConfig
import com.samwoo.istudy.R
import com.samwoo.istudy.activity.ContentActivity
import com.samwoo.istudy.adapter.HomeAdapter
import com.samwoo.istudy.base.BaseFragment
import com.samwoo.istudy.bean.*
import com.samwoo.istudy.constant.Constant
import com.samwoo.istudy.mvp.contract.HomeContract
import com.samwoo.istudy.mvp.presenter.HomePresenter
import com.samwoo.istudy.util.ImageLoader
import com.samwoo.istudy.widget.SpaceItemDecoration
import kotlinx.android.synthetic.main.fragment_refresh_layout.*
import kotlinx.android.synthetic.main.item_home_banner.view.*
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.toast

class HomeFragment : BaseFragment(), HomeContract.View {
    companion object {
        fun instance(): HomeFragment = HomeFragment()
    }

    private val articles = mutableListOf<Article>()
    private lateinit var banners: MutableList<Banner>
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


    override fun getLayoutResId(): Int {
        return R.layout.fragment_refresh_layout
    }

    override fun lazyLoad() {
        mPresenter?.getBanners()
        mPresenter?.getArticles(0)
    }

    override fun initView() {
        mPresenter = HomePresenter()
        mPresenter?.attachView(this)

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
            setEmptyView(R.layout.fragment_empty)
            addHeaderView(bannerView)
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
    private val onItemChildClickListener = BaseQuickAdapter.OnItemChildClickListener { _, _, position ->
        if (articles.size != 0) {
            val data = articles[position]
            activity?.toast("$data")
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

    override fun setBanner(bannerData: BannerList) {
        banners = bannerData.data
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
                    loadMoreEnd(isRefresh)
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
            } else {
                loadMoreFail()
            }
        }
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

    override fun onDestroyView() {
        super.onDestroyView()
        mPresenter?.detachView()
        mPresenter = null
        if (BuildConfig.DEBUG) Log.d("Sam", "HomeFragment DestroyView....")
    }
}