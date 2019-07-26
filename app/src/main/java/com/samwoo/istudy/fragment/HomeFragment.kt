package com.samwoo.istudy.fragment

import com.samwoo.istudy.R
import com.samwoo.istudy.base.BaseFragment

import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.item_home_list.*

class HomeFragment : BaseFragment(){


    override fun getLayoutResId(): Int {
        return R.layout.fragment_home
    }

    override fun initView() {

    }

    companion object{
        fun instance():HomeFragment= HomeFragment()
    }
}