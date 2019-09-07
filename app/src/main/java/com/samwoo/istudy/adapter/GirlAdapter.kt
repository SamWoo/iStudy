package com.samwoo.istudy.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.samwoo.istudy.R
import com.samwoo.istudy.bean.Girl
import com.samwoo.istudy.util.DisplayManager
import com.samwoo.istudy.util.DisplayManager.dp2px
import com.samwoo.istudy.util.ImageLoader

class GirlAdapter(val context: Context?, data: MutableList<Girl>) :
    BaseQuickAdapter<Girl, BaseViewHolder>(R.layout.item_girl_layout, data) {
    //解决上下滚动后item因复用导致的位置错乱问题
//    override fun getItemViewType(position: Int): Int = position

    override fun getItemId(position: Int): Long = position.toLong()

    override fun convert(helper: BaseViewHolder?, item: Girl?) {
        item ?: return
        helper ?: return

//        helper.setIsRecyclable(false) //禁止复用，不推荐使用,易导致OOM
        if (item.url.isNotEmpty()) {
            if (item.url.contains("7xi8d6") || item.url.contains("img.gank.io")) {
                helper.getView<LinearLayout>(R.id.meizhi_card).visibility = View.GONE
                val cardView = helper.getView<CardView>(R.id.cardView_girl)
                val params = cardView.layoutParams as StaggeredGridLayoutManager.LayoutParams
                params.setMargins(dp2px(4), 0, dp2px(4), 0)
                cardView.layoutParams = params
            } else {
                helper.getView<LinearLayout>(R.id.meizhi_card).visibility = View.VISIBLE
                val imageView = helper.getView<ImageView>(R.id.meizhi)
                //缩放比例
                val scale: Float =
                    item.width / ((DisplayManager.getScreenWidth()!! - 30).div(2f))

                val params: ViewGroup.LayoutParams = imageView.layoutParams
                params.height = (item.height / scale).toInt()
                imageView.layoutParams = params
                context?.let {
                    ImageLoader.load(context, item.url, imageView)
                }

                val desc =
                    if (item.publishedAt != null)
                        item.publishedAt.split("T")[0]
                    else
                        item.createdAt.split("T")[0]
                helper.setText(R.id.title, desc)
            }
        } else {
            return
        }
    }
}