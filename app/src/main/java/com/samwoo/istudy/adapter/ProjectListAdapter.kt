package com.samwoo.istudy.adapter

import android.content.Context
import android.text.Html
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.samwoo.istudy.R
import com.samwoo.istudy.bean.Article
import com.samwoo.istudy.util.ImageLoader

class ProjectListAdapter(private val context: Context?, datas: MutableList<Article>) :
    BaseQuickAdapter<Article, BaseViewHolder>(
        R.layout.item_project_list, datas
    ) {
    override fun convert(helper: BaseViewHolder?, item: Article?) {
        item ?: return
        helper ?: return
        helper.apply {
            setText(R.id.item_project_list_title_tv, Html.fromHtml(item.title))
            setText(R.id.item_project_list_content_tv, Html.fromHtml(item.desc))
            setText(R.id.item_project_list_author_tv, item.author)
            setText(R.id.item_project_list_time_tv, item.niceDate)
            setImageResource(
                R.id.iv_like,
                if (item.collect) R.drawable.ic_like else R.drawable.ic_like_not
            )
            addOnClickListener(R.id.iv_like)
        }
        context?.let { ImageLoader.load(it, item.envelopePic, helper.getView(R.id.item_project_list_iv)) }
    }

}