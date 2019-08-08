package com.samwoo.istudy.adapter

import android.content.Context
import androidx.core.content.ContextCompat
import com.samwoo.istudy.R
import com.samwoo.istudy.bean.NavigationBean
import q.rorbin.verticaltablayout.adapter.TabAdapter
import q.rorbin.verticaltablayout.widget.ITabView

class NavTabAdapter(val context: Context?, val list: List<NavigationBean>) : TabAdapter {
    override fun getIcon(position: Int): ITabView.TabIcon? {
        return null
    }

    override fun getBadge(position: Int): ITabView.TabBadge? {
        return null
    }

    override fun getBackground(position: Int): Int {
        return -1
    }

    override fun getTitle(position: Int): ITabView.TabTitle {
        return ITabView.TabTitle.Builder()
            .setContent(list[position].name)
            .setTextColor(
                ContextCompat.getColor(context!!, R.color.Red),
                ContextCompat.getColor(context!!, R.color.white)
            )
            .build()
    }

    override fun getCount(): Int {
        return list.size
    }

}