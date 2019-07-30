package com.samwoo.istudy.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.samwoo.istudy.bean.ProjectTreeBody
import com.samwoo.istudy.fragment.ProjectListFragment

class ProjectPagerAdapter(fm: FragmentManager, private val datas: MutableList<ProjectTreeBody>) :
    FragmentStatePagerAdapter(fm) {
    private val fragments = mutableListOf<Fragment>()

    init {
        fragments.clear()
        datas.forEach {
            fragments.add(ProjectListFragment.instance(it.id))
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