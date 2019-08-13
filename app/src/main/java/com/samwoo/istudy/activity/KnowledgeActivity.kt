package com.samwoo.istudy.activity

import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.google.android.material.tabs.TabLayout
import com.samwoo.istudy.R
import com.samwoo.istudy.adapter.KnowledgePagerAdapter
import com.samwoo.istudy.base.BaseActivity
import com.samwoo.istudy.bean.Knowledge
import com.samwoo.istudy.bean.KnowledgeTreeBody
import com.samwoo.istudy.constant.Constant
import com.samwoo.istudy.fragment.ArticlesFragment
import kotlinx.android.synthetic.main.activity_knowledge.*
import kotlinx.android.synthetic.main.activity_knowledge.toolbar
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.toast

class KnowledgeActivity : BaseActivity() {

    private var knowledges = mutableListOf<Knowledge>()
    private var title: String? = null

    private val pagerAdapter: KnowledgePagerAdapter by lazy {
        KnowledgePagerAdapter(supportFragmentManager, knowledges)
    }

    override fun useEventBus(): Boolean {
        return false
    }

    override fun getLayoutResId(): Int = R.layout.activity_knowledge

    override fun initView() {
        toolbar.run {
            title = this@KnowledgeActivity.title
            setSupportActionBar(this)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        viewPager.run {
            adapter = pagerAdapter
            addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
//            offscreenPageLimit=datas.size
        }

        tabLayout.run {
            setupWithViewPager(viewPager)
            addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(viewPager))
        }

        fab.run {
            setOnClickListener(onFabClickListener)
        }
    }

    override fun initData() {
        intent.extras.let {
            title = it.getString(Constant.CONTENT_TITLE_KEY, "")
            it.getSerializable(Constant.CONTENT_DATA_KEY).let {
                val data = it as KnowledgeTreeBody
                data.children.let { children ->
                    knowledges.addAll(children)
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_activity_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_search -> {
                toast(getString(R.string.action_search))
                val intent = intentFor<SearchActivity>()
                startActivity(intent)
                true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    //浮点按钮事件监听
    private val onFabClickListener = View.OnClickListener {
        if (pagerAdapter.count == 0) return@OnClickListener
        val fragment: ArticlesFragment = pagerAdapter.getItem(viewPager.currentItem) as ArticlesFragment
        fragment.scrollToTop()
    }
}