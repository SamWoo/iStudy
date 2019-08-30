package com.samwoo.istudy.adapter

import android.content.Context
import android.text.Html
import android.view.View
import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.samwoo.istudy.R
import com.samwoo.istudy.bean.Article
import com.samwoo.istudy.util.ImageLoader

class CollectionAdapter(private val context: Context?, datas: MutableList<Article>) :
    BaseQuickAdapter<Article, BaseViewHolder>(
        R.layout.item_collection_list, datas
    ) {
    override fun convert(helper: BaseViewHolder?, item: Article?) {
        item ?: return
        helper ?: return
        helper.apply {
            setText(R.id.tv_article_title, Html.fromHtml(item.title))
            setText(R.id.tv_article_author, item.author)
            setText(R.id.tv_article_date, item.niceDate)
            addOnClickListener(R.id.iv_like)
        }

        if (item.envelopePic.isNotEmpty()) {
            helper.getView<ImageView>(R.id.iv_article_thumbnail).visibility = View.VISIBLE
            context?.let {
                ImageLoader.load(
                    context,
                    item.envelopePic,
                    helper.getView(R.id.iv_article_thumbnail)
                )
            }
        } else {
            helper.getView<ImageView>(R.id.iv_article_thumbnail).visibility = View.GONE
        }
    }

}