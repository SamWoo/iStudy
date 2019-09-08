package com.samwoo.istudy.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.samwoo.istudy.R
import com.wang.avi.AVLoadingIndicatorView

object MsgView {
    /**
     * 设置适配器的空布局
     * @param adapter 适配器
     * @param msg 空布局文字提示
     * @param ImgResId 空布局图片资源，若isLoad为true则不生效
     * @param isLoad 是否是加载中
     */
    fun <T : BaseQuickAdapter<*, BaseViewHolder>> setAdapterView(
        context: Context,
        adapter: T,
        msg: String,
        imgResId: Int,
        isLoad: Boolean
    ) {
        //声明全局变量
        var view: View? = null
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.layout_msg_view, null)
        }
        view!!.findViewById<TextView>(R.id.tv_msg).text = msg
        if (isLoad) {
            view!!.findViewById<ImageView>(R.id.iv_msg).visibility = View.GONE
            view!!.findViewById<AVLoadingIndicatorView>(R.id.avi).visibility = View.VISIBLE
        } else {
            view!!.findViewById<ImageView>(R.id.iv_msg).setImageResource(imgResId)
            view!!.findViewById<ImageView>(R.id.iv_msg).visibility = View.VISIBLE
            view!!.findViewById<AVLoadingIndicatorView>(R.id.avi).visibility = View.GONE
        }
        //数据得清空才会显示空布局
        adapter.getData().clear()
        adapter.setEmptyView(view)
        adapter.notifyDataSetChanged()
    }

    /**
     * 显示错误布局
     * @param adapter recyclerView的适配器
     * @param msg 错误信息
     */
    fun <T : BaseQuickAdapter<*, BaseViewHolder>> showErrorView(
        context: Context,
        adapter: T,
        msg: String
    ) {
        //替换错误布局图片
        setAdapterView(context, adapter, msg, R.mipmap.nodata, false)
    }

    /**
     * 显示空布局
     * @param adapter recyclerView的适配器
     */
    fun <T : BaseQuickAdapter<*, BaseViewHolder>> showEmptyView(
        context: Context,
        adapter: T
    ) {
        //替换空布局图片
        setAdapterView(context, adapter, "空空如也", R.mipmap.nodata, false)
    }

    /**
     * 显示加载中布局
     * @param adapter recyclerView的适配器
     */
    fun <T : BaseQuickAdapter<*, BaseViewHolder>> showLoadView(
        context: Context,
        adapter: T
    ) {
        setAdapterView(context, adapter, "加载中……", 0, true)
    }
}