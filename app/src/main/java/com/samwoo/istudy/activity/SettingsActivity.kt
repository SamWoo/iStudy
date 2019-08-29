package com.samwoo.istudy.activity

import android.app.Fragment
import android.app.FragmentTransaction
import com.samwoo.istudy.R
import com.samwoo.istudy.base.BaseActivity
import com.samwoo.istudy.fragment.SettingsFragment
import kotlinx.android.synthetic.main.activity_general.*

class SettingsActivity : BaseActivity() {
    override fun requestData() {}

    override fun getLayoutResId(): Int = R.layout.activity_general

    override fun initView() {
        toolbar.apply {
            title = getString(R.string.settings)
            setSupportActionBar(this)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        //setup fragment
        setupFragment()
    }

    //    @SuppressLint("WrongConstant")
    private fun setupFragment() {
        val fragment = Fragment.instantiate(this, SettingsFragment::class.java.name)
        fragmentManager.beginTransaction()
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .replace(R.id.container, fragment)
            .commitAllowingStateLoss()
    }

    override fun initData() {
    }

}