package com.samwoo.istudy.fragment

import android.app.Dialog
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.chad.library.adapter.base.BaseQuickAdapter
import com.samwoo.istudy.R
import com.samwoo.istudy.activity.ContentActivity
import com.samwoo.istudy.adapter.GirlAdapter
import com.samwoo.istudy.base.BaseFragment
import com.samwoo.istudy.bean.Girl
import com.samwoo.istudy.constant.Constant
import com.samwoo.istudy.mvp.contract.GirlContract
import com.samwoo.istudy.mvp.presenter.GirlPresenter
import com.samwoo.istudy.util.ImageLoader
import com.samwoo.istudy.widget.listener.MultiPointTouchListener
import kotlinx.android.synthetic.main.dialog_show_girl.*
import kotlinx.android.synthetic.main.fragment_refresh_layout.*
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.toast
import java.io.File

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

            val imageView = view.findViewById<ImageView>(R.id.image_girl)
            ImageLoader.load(activity, data.url, imageView)

            imageView.apply {
                setOnClickListener {
//                    dialog.dismiss()
                }
                setOnLongClickListener {
                    true
                }
                setOnTouchListener(MultiPointTouchListener())
            }

            //show the dialog
            dialog.show()
        }
    }

    //长按保存图片
    private fun saveImage(bitmap: Bitmap) {
        val file: File = File("/sdcard/myFolder")
        if (!file.exists()) file.mkdirs()
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