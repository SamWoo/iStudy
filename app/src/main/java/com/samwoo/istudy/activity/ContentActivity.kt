package com.samwoo.istudy.activity

import com.samwoo.istudy.R
import com.samwoo.istudy.base.BaseActivity
import com.samwoo.istudy.constant.Constant
import kotlinx.android.synthetic.main.activity_content.*

class ContentActivity : BaseActivity() {
    private lateinit var title: String
    private var id: Int = -1
    private lateinit var url: String

    override fun getLayoutResId(): Int {
        return R.layout.activity_content
    }

    override fun initView() {
        intent.extras?.let {
            id = it.getInt(Constant.CONTENT_ID_KEY, -1)
            title = it.getString(Constant.CONTENT_TITLE_KEY, "")
            url = it.getString(Constant.CONTENT_URL_KEY, "")
        }

        toolbar.run {
            title = ""
            setSupportActionBar(this)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

    }

    override fun initData() {}

    fun initAgentWeb() {

    }
}