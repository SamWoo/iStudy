package com.samwoo.istudy.adapter

import android.content.Context
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.samwoo.istudy.R
import com.samwoo.istudy.bean.KnowledgeTreeBody

class KnowledgeTreeAdapter(private val context: Context?, datas: MutableList<KnowledgeTreeBody>) :
    BaseQuickAdapter<KnowledgeTreeBody, BaseViewHolder>(
        R.layout.item_knowledge_tree_list, datas
    ) {
    override fun convert(helper: BaseViewHolder?, item: KnowledgeTreeBody?) {
        item ?: return
        helper ?: return
        helper.setText(R.id.title_first, item?.name)
        item?.children.let {
            helper?.setText(R.id.title_second, it?.joinToString(" / ", transform = { child ->
                child.name
            }))
        }
    }
}