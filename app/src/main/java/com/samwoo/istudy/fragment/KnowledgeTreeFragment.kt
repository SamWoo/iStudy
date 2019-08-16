package com.samwoo.istudy.fragment

import android.os.Build
import android.util.Log
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.samwoo.istudy.BuildConfig
import com.samwoo.istudy.R
import com.samwoo.istudy.activity.KnowledgeActivity
import com.samwoo.istudy.adapter.KnowledgeTreeAdapter
import com.samwoo.istudy.base.BaseFragment
import com.samwoo.istudy.bean.KnowledgeTreeBody
import com.samwoo.istudy.constant.Constant
import com.samwoo.istudy.mvp.contract.KnowledgeTreeContract
import com.samwoo.istudy.mvp.presenter.KnowledgeTreePresenter
import com.samwoo.istudy.widget.SpaceItemDecoration
import kotlinx.android.synthetic.main.fragment_refresh_layout.*
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.toast

class KnowledgeTreeFragment : BaseFragment(), KnowledgeTreeContract.View {

    companion object {
        fun instance(): KnowledgeTreeFragment = KnowledgeTreeFragment()
    }

    private val datas = mutableListOf<KnowledgeTreeBody>()

    private var mPresenter: KnowledgeTreePresenter? = null

    private val linearLayoutManager by lazy {
        LinearLayoutManager(activity)
    }

    private val knowledgeTreeAdapter: KnowledgeTreeAdapter by lazy {
        KnowledgeTreeAdapter(activity, datas)
    }

    private val itemDecoration by lazy {
        activity?.let {
            SpaceItemDecoration(it)
        }
    }

    override fun getLayoutResId(): Int {
        return R.layout.fragment_refresh_layout
    }

    override fun initView() {
        mPresenter = KnowledgeTreePresenter()
        mPresenter?.attachView(this)

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

            setOnRefreshListener {
                mPresenter?.getKnowledgeTree()
            }
        }

        recyclerView.run {
            layoutManager = linearLayoutManager
            itemAnimator = DefaultItemAnimator()
            adapter = knowledgeTreeAdapter
            addItemDecoration(itemDecoration!!)
        }

        knowledgeTreeAdapter.run {
            bindToRecyclerView(recyclerView)
            setEnableLoadMore(false)
            onItemClickListener = BaseQuickAdapter.OnItemClickListener { _, _, position ->
                if (datas.size != 0) {
                    val data = datas[position]
                    val intent = activity!!.intentFor<KnowledgeActivity>(
                        Pair(Constant.CONTENT_TITLE_KEY, data.name),
                        Pair(Constant.CONTENT_DATA_KEY, data)
                    )
                    startActivity(intent)
                }
            }
            setEmptyView(R.layout.fragment_empty)
        }


    }

    override fun lazyLoad() {
        mPresenter?.getKnowledgeTree()
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

    override fun setKnowledgeTree(data: List<KnowledgeTreeBody>) {
        data.let {
            knowledgeTreeAdapter.run { replaceData(data) }
        }
        Log.d("Sam", "datas======>${datas}")
    }

    override fun showLoading() {
        swipeRefreshLayout.isRefreshing = true
    }

    override fun hideLoading() {
        swipeRefreshLayout.isRefreshing = false
        knowledgeTreeAdapter.loadMoreComplete()
    }

    override fun showError(errorMsg: String) {
        knowledgeTreeAdapter.run { loadMoreFail() }
        activity?.toast(errorMsg)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mPresenter?.detachView()
        mPresenter = null
        if (BuildConfig.DEBUG) Log.d("Sam", "KnowledgeTreeFragment DestroyView....")
    }

}