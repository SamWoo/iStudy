package com.samwoo.istudy.adapter

import android.content.Context
import android.view.View
import android.widget.LinearLayout
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.samwoo.istudy.R
import com.samwoo.istudy.bean.Girl
import com.samwoo.istudy.util.ImageLoader

class GirlAdapter(val context: Context?, data: MutableList<Girl>) :
    BaseQuickAdapter<Girl, BaseViewHolder>(R.layout.item_girl_layout, data) {
    override fun convert(helper: BaseViewHolder?, item: Girl?) {
        item ?: return
        helper ?: return
        if (item.url.isNotEmpty()
            && !item.url.contains("7xi8d6")
            && !item.url.contains("img.gank.io")
        ) {
            helper.getView<LinearLayout>(R.id.meizhi_card).visibility = View.VISIBLE
            context?.let {
                ImageLoader.load(context, item.url, helper.getView(R.id.meizhi))
            }
            val desc = item.createdAt.split("T")[0]
            helper.setText(R.id.title, desc)
        } else {
            helper.getView<LinearLayout>(R.id.meizhi_card).visibility = View.GONE
        }

    }

}