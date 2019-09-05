package com.samwoo.istudy.fragment

import android.app.Dialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ImageView
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.chad.library.adapter.base.BaseQuickAdapter
import com.samwoo.istudy.R
import com.samwoo.istudy.adapter.GirlAdapter
import com.samwoo.istudy.base.BaseFragment
import com.samwoo.istudy.bean.Girl
import com.samwoo.istudy.mvp.contract.GirlContract
import com.samwoo.istudy.mvp.presenter.GirlPresenter
import com.samwoo.istudy.util.ImageLoader
import com.samwoo.istudy.util.NetworkUtil
import com.samwoo.istudy.util.PermissionUtil
import com.samwoo.istudy.widget.listener.MultiPointTouchListener
import kotlinx.android.synthetic.main.fragment_refresh_layout.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.toast
import org.jetbrains.anko.uiThread
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.lang.Exception
import java.net.URL

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
        "android.permission.WRITE_EXTERNAL_STORAGE",
        "android.permission.READ_EXTERNAL_STORAGE"
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

        staggeredGridLayoutManager.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_NONE
        recyclerView.run {
            layoutManager = staggeredGridLayoutManager
            adapter = girlAdapter
            itemAnimator = DefaultItemAnimator()
        }

        girlAdapter.run {
            bindToRecyclerView(recyclerView)
            openLoadAnimation(BaseQuickAdapter.SLIDEIN_BOTTOM)
            setOnLoadMoreListener(onRequestLoadMoreListener, recyclerView)
            onItemClickListener = this@GirlFragment.onItemClickListener
        }
    }

    //RefreshListener
    private val onRefreshListener = SwipeRefreshLayout.OnRefreshListener {
        swipeRefreshLayout.isRefreshing = true
        isRefresh = true
        mPresenter?.getGirlPhoto(1)

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
                    if (!NetworkUtil.isNetworkAvailable(activity!!)) {
                        activity!!.toast("当前网络不可用!!请检查网络设置!!")
                        return@setOnClickListener
                    }
                    activity!!.alert(R.string.download_photo, R.string.tip_msg) {
                        positiveButton(R.string.confirm) {
                            val ret =
                                PermissionUtil.checkMultiPermission(activity!!, permissions, 0)
                            if (ret) {
                                activity!!.toast("正在下载中...")
                                mPresenter?.savePhoto(activity!!, data.url, data.desc, this@run)
                            } else {
                                activity!!.toast("请授权读写SD卡权限!!")
                            }
                        }
                        negativeButton(R.string.cancel) {
                            dismiss()
                        }
                    }.show()
                }
            }

            //show the dialog
            dialog.show()
        }
    }

    override fun lazyLoad() {
        mPresenter?.getGirlPhoto(1)
    }

    override fun showGirlPhoto(data: List<Girl>) {
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
    }

    override fun hideLoading() {
    }

    override fun showError(errorMsg: String) {
        girlAdapter.run {
            if (isRefresh) {
                setEnableLoadMore(true)
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