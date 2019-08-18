package com.samwoo.istudy.activity

import com.samwoo.istudy.R
import com.samwoo.istudy.base.BaseActivity
import com.samwoo.istudy.constant.Constant
import com.samwoo.istudy.fragment.SearchResultFragment
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
            Constant.TYPE.SEARCH_TYPE_KEY -> {
                toolbar.title = extras.getString(Constant.SEARCH_KEY, "")
                SearchResultFragment.instance(extras)
            }
            else -> {
                null
            }
        }
        fragment ?: return
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment, "")
            .commit()
    }

    override fun initData() {}
}