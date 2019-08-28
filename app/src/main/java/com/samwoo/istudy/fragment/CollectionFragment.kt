package com.samwoo.istudy.fragment

import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.samwoo.istudy.App
import com.samwoo.istudy.R
import com.samwoo.istudy.activity.ContentActivity
import com.samwoo.istudy.adapter.ArticlesAdapter
import com.samwoo.istudy.base.BaseFragment
import com.samwoo.istudy.bean.Article
import com.samwoo.istudy.bean.ArticlesListBean
import com.samwoo.istudy.constant.Constant
import com.samwoo.istudy.mvp.contract.CollectContract
import com.samwoo.istudy.mvp.presenter.CollectPresenter
import com.samwoo.istudy.util.NetworkUtil
import com.samwoo.istudy.util.SLog
import com.samwoo.istudy.widget.SpaceItemDecoration
import kotlinx.android.synthetic.main.fragment_refresh_layout.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.toast

class CollectionFragment : BaseFragment(), CollectContract.View {
    companion object {
        fun instance(args: Bundle?): CollectionFragment {
            val fragment = CollectionFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private var datas = mutableListOf<Article>()
    private var isRefresh = true

    private var mPresenter: CollectPresenter? = null

    private val linearLayoutManager by lazy {
        LinearLayoutManager(activity)
    }

    private val itemDecoration by lazy {
        activity?.let { SpaceItemDecoration(it) }
    }

    private val collectAdapter by lazy {
        ArticlesAdapter(activity, datas)
    }

    override fun getLayoutResId(): Int = R.layout.fragment_refresh_layout

    override fun initView() {
        mPresenter = CollectPresenter()
        mPresenter?.attachView(this)

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
                isRefreshing = false
                showLoading()
                isRefresh = true
                mPresenter?.getCollectList(0)
            }
        }

        recyclerView.apply {
            layoutManager = linearLayoutManager
            addItemDecoration(itemDecoration!!)
            itemAnimator = DefaultItemAnimator()
            adapter = collectAdapter
        }

        collectAdapter.run {
            bindToRecyclerView(recyclerView)
            openLoadAnimation(BaseQuickAdapter.SLIDEIN_BOTTOM)
            onItemClickListener = this@CollectionFragment.onItemClickListener
            onItemChildClickListener = this@CollectionFragment.onItemChildClickListener
            setOnLoadMoreListener(onRequestLoadMoreListener, recyclerView)
            setEmptyView(R.layout.layout_empty)
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

    private val onItemChildClickListener =
        BaseQuickAdapter.OnItemChildClickListener { _, view, position ->
            if (datas.size != 0) {
                val data = datas[position]
                when (view.id) {
                    R.id.iv_like -> {
                        if (!NetworkUtil.isNetworkAvailable(App.context)) {
                            activity?.toast("网络不可用!!")
                            return@OnItemChildClickListener
                        }
//                        mPresenter?.removeCollectArticle(data.id, data.originId)
//                        collectAdapter.remove(position)

                        val dailog = AlertDialog.Builder(activity!!).run {
                            setTitle(R.string.cancle_collect_title)
                            setMessage(R.string.cancle_collect_msg)
                            setIcon(R.mipmap.icon)
                            setPositiveButton(R.string.confirm) { dailog, which ->
                                mPresenter?.removeCollectArticle(data.id, data.originId)
                                collectAdapter.remove(position)
                            }
                            setNegativeButton(R.string.cancel) { dailog, which ->
                                dailog.dismiss()
                            }
                            create()
                        }
                        dailog.show()
                    }
                }
            }
        }

    private val onRequestLoadMoreListener = BaseQuickAdapter.RequestLoadMoreListener {
        isRefresh = false
        swipeRefreshLayout.isRefreshing = false
        val page = datas.size / 20
        mPresenter?.getCollectList(page)
    }


    override fun lazyLoad() {
        mPresenter?.getCollectList(0)
    }

    override fun collectSuccess() {}

    override fun collectFail() {}

    override fun showCollectList(data: ArticlesListBean) {
        data.datas.let {
            collectAdapter.run {
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

    override fun cancleCollectSuccess() {
        activity?.toast("取消成功!!")
    }

    override fun cancleCollectFail() {
    }

    override fun showLoading() {
//        loadingDialog.show()
    }

    override fun hideLoading() {
//        loadingDialog.hide()
        if (isRefresh) collectAdapter.setEnableLoadMore(true)
    }

    override fun showError(errorMsg: String) {
        activity?.toast(errorMsg)
        collectAdapter.run {
            when (isRefresh) {
                true -> setEnableLoadMore(true)
                else -> loadMoreFail()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mPresenter?.detachView()
        mPresenter = null
    }

}