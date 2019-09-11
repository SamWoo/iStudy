package com.samwoo.istudy.fragment

import android.app.Dialog
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ImageView
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.chad.library.adapter.base.BaseQuickAdapter
import com.samwoo.istudy.App
import com.samwoo.istudy.R
import com.samwoo.istudy.adapter.GirlAdapter
import com.samwoo.istudy.base.BaseFragment
import com.samwoo.istudy.bean.Girl
import com.samwoo.istudy.constant.Constant
import com.samwoo.istudy.mvp.contract.GirlContract
import com.samwoo.istudy.mvp.presenter.GirlPresenter
import com.samwoo.istudy.service.ImageService
import com.samwoo.istudy.util.ImageLoader
import com.samwoo.istudy.util.NetworkUtil
import com.samwoo.istudy.util.PermissionUtil
import com.samwoo.istudy.view.MsgView
import com.samwoo.istudy.widget.listener.MultiPointTouchListener
import kotlinx.android.synthetic.main.fragment_refresh_layout.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.jetbrains.anko.alert
import org.jetbrains.anko.toast

class GirlFragment : BaseFragment(), GirlContract.View {

    companion object {
        fun instance(args: Bundle?): GirlFragment {
            val fragment = GirlFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private var mPresenter: GirlPresenter? = null
    private val photos = mutableListOf<Girl>()
    private var isRefresh: Boolean = true

    //读写SD的权限
    private val permissions: Array<String> = arrayOf(
        Constant.WRITE_EXTERNAL_STORAGE,
        Constant.READ_EXTERNAL_STORAGE
    )

    private val staggeredGridLayoutManager: StaggeredGridLayoutManager by lazy {
        StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
    }

    private val girlAdapter: GirlAdapter by lazy {
        GirlAdapter(activity, photos)
    }

    override fun getLayoutResId(): Int = R.layout.fragment_refresh_layout

    override fun initView() {
        mPresenter = GirlPresenter()
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
            setOnRefreshListener(onRefreshListener)
        }

        staggeredGridLayoutManager.gapStrategy =
            StaggeredGridLayoutManager.GAP_HANDLING_NONE //可防止Item切换
        recyclerView.run {
            layoutManager = staggeredGridLayoutManager
            adapter = girlAdapter
            itemAnimator = DefaultItemAnimator()
            addOnScrollListener(onScrollListener)
        }

        girlAdapter.run {
            bindToRecyclerView(recyclerView)
            openLoadAnimation(BaseQuickAdapter.SLIDEIN_BOTTOM)
            setOnLoadMoreListener(onRequestLoadMoreListener, recyclerView)
            onItemClickListener = this@GirlFragment.onItemClickListener
        }
        MsgView.showLoadView(context!!, girlAdapter)
    }

    //RefreshListener
    private val onRefreshListener = SwipeRefreshLayout.OnRefreshListener {
        swipeRefreshLayout.isRefreshing = false
        isRefresh = true
        mPresenter?.getGirlPhoto(1)
    }

    //ScrollListener 曾经删除过Item，则滑到顶部的时候刷新布局，避免错乱
    private val onScrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
//            val layoutManager = recyclerView.layoutManager as StaggeredGridLayoutManager
            staggeredGridLayoutManager.invalidateSpanAssignments() //防止第一行到顶部有空白
        }
    }

    //RequestMoreListener
    private val onRequestLoadMoreListener = BaseQuickAdapter.RequestLoadMoreListener {
        isRefresh = false
        swipeRefreshLayout.isRefreshing = false
        val page = girlAdapter.data.size / 20 + 1
        mPresenter?.getGirlPhoto(page)
    }

    //ItemClickListener
    private val onItemClickListener = BaseQuickAdapter.OnItemClickListener { _, _, position ->
        if (photos.size != 0) {
            val data = photos[position]
            val view = LayoutInflater.from(context).inflate(R.layout.dialog_show_girl, null)
            val dialog = Dialog(activity, R.style.ShowDialog).apply {
                setContentView(view)
                setCancelable(true)
            }

            //show photo
            val imageView = view.findViewById<ImageView>(R.id.image_girl)
            ImageLoader.load(activity, data.url, imageView)
            imageView.apply {
                setOnTouchListener(MultiPointTouchListener())
            }

            //download photo
            val btn_download = view.findViewById<ImageView>(R.id.photo_download)
            btn_download.run {
                setOnClickListener {
                    if (!NetworkUtil.isNetworkAvailable(App.context)) {
                        App.context.toast("当前网络不可用!!请检查网络设置!!")
                        return@setOnClickListener
                    }
                    activity!!.alert(R.string.download_photo, R.string.tip_msg) {
                        positiveButton(R.string.confirm) {
                            if (PermissionUtil.checkMultiPermission(activity!!, permissions, 0)) {
                                App.context.toast("正在下载中...")
                                mPresenter?.savePhoto(App.context, data.url, data.desc, this@run)
                            } else {
                                App.context.toast("请授权读写SD卡权限!!")
                            }
                        }
                        negativeButton(R.string.cancel) {
                            dismiss()
                        }
                    }.show()
                }
            }
            dialog.show() //show the dialog
        }
    }

    override fun lazyLoad() {
        mPresenter?.getGirlPhoto(1)
    }

    /**
     * 加载成功时先在后台获取图片的width和height，
     * 这样就可以不出现加载时重绘导致的闪屏了,
     * 但是这样会导致进入的时候呈现一片空白界面。
     * 最理想的解决方式就是“让后台下发数据是带上width和height”
     */
    override fun onSuccess(data: List<Girl>) {
        ImageService.startService(activity!!, data)
    }

    //后台处理完通知前台显示数据
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun dataEvent(data: List<Girl>) {
        showGirlPhoto(data)
    }

    private fun showGirlPhoto(data: List<Girl>) {
        data.let {
            girlAdapter.run {
                when (isRefresh) {
                    true -> replaceData(it)
                    else -> addData(it)
                }
                val size = it.size
                if (size < 20) {
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
        swipeRefreshLayout.isRefreshing = false
    }

    override fun showError(errorMsg: String) {
        girlAdapter.run {
            if (isRefresh) {
                setEnableLoadMore(true)
                MsgView.showErrorView(context!!, girlAdapter, "加载失败...o(╥﹏╥)o")
            } else {
                loadMoreFail()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mPresenter?.detachView()
        mPresenter = null
    }
}