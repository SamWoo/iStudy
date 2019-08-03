package com.samwoo.istudy.adapter

import android.content.Context
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.samwoo.istudy.R
import com.samwoo.istudy.bean.SearchHistoryBean

class SearchHistoryAdapter(private val context: Context?, data: MutableList<SearchHistoryBean>) :
    BaseQuickAdapter<SearchHistoryBean, BaseViewHolder>(R.layout.item_search_history, data) {
    override fun convert(helper: BaseViewHolder?, item: SearchHistoryBean?) {
        helper ?: return
        item ?: return
        helper.apply {
            setText(R.id.tv_search_key, item.key)
            addOnClickListener(R.id.iv_clear)
        }
    }

}