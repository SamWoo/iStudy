package com.samwoo.istudy.activity

import androidx.fragment.app.Fragment
import com.samwoo.istudy.R
import com.samwoo.istudy.base.BaseActivity
import com.samwoo.istudy.constant.Constant
import com.samwoo.istudy.fragment.*
import kotlinx.android.synthetic.main.activity_general.*

class GeneralActivity : BaseActivity() {

    override fun useEventBus(): Boolean = false

    override fun requestData() {}

    override fun getLayoutResId(): Int {
        return R.layout.activity_general
    }

    override fun initView() {
        val extras = intent.extras
        val type = extras.getString(Constant.TYPE_KEY, "")

        toolbar.apply {
            title = ""
            setSupportActionBar(this)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        val fragment = when (type) {
            Constant.TYPE.SEARCH_TYPE_KEY -> {//搜索结果
                toolbar.title = extras.getString(Constant.SEARCH_KEY, "")
                SearchResultFragment.instance(extras)
            }
            Constant.TYPE.COLLECT_TYPE_KEY -> {//收藏List
                toolbar.title = getString(R.string.my_collected)
                CollectionFragment.instance(extras)
            }
            else -> {
                null
            }
        }
        fragment ?: return
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment as Fragment, "")
            .commit()
    }

    override fun initData() {}
}