package com.samwoo.istudy.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.PagerAdapter
import com.samwoo.istudy.bean.WxAccountBody
import com.samwoo.istudy.fragment.ArticlesFragment

class WxAccountPagerAdapter(fm:FragmentManager, private val datas: MutableList<WxAccountBody>) :
    FragmentStatePagerAdapter(fm) {
    private val fragments = mutableListOf<Fragment>()

    init {
        fragments.clear()
        datas.forEach {
            fragments.add(ArticlesFragment.instance(it.id))
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

    override fun getItemPosition(`object`: Any): Int {
        return PagerAdapter.POSITION_NONE
    }

}