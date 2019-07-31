package com.samwoo.istudy.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.samwoo.istudy.bean.WxAccountBody
import com.samwoo.istudy.fragment.WxArticlesFragment

class WxAccountPagerAdapter(fm: FragmentManager, private val datas: MutableList<WxAccountBody>) : FragmentStatePagerAdapter(fm) {
    private val fragments = mutableListOf<Fragment>()

    init {
        fragments.clear()
        datas.forEach {
            fragments.add(WxArticlesFragment.instance(it.id))
        }
    }

    override fun getItem(position: Int): Fragment {
        return fragments[position]
    }

    override fun getCount(): Int {
        return datas.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return datas[position].name
    }

}