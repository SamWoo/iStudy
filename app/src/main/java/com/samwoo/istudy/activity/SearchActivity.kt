package com.samwoo.istudy.activity

import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.samwoo.istudy.R
import com.samwoo.istudy.adapter.SearchHistoryAdapter
import com.samwoo.istudy.base.BaseActivity
import com.samwoo.istudy.bean.HotKey
import com.samwoo.istudy.bean.SearchHistoryBean
import com.samwoo.istudy.mvp.contract.SearchContract
import com.samwoo.istudy.mvp.presenter.SearchPresenter
import com.samwoo.istudy.util.randomColor
import com.samwoo.istudy.widget.SpaceItemDecoration
import com.zhy.view.flowlayout.FlowLayout
import com.zhy.view.flowlayout.TagAdapter
import kotlinx.android.synthetic.main.activity_search.*
import org.jetbrains.anko.toast

class SearchActivity : BaseActivity(), SearchContract.View {

    private var hotKeys = mutableListOf<HotKey>()
    private var searchHistory = mutableListOf<SearchHistoryBean>()

    private val mPresenter: SearchPresenter by lazy {
        SearchPresenter()
    }

    private val searchHistoryAdapter: SearchHistoryAdapter by lazy {
        SearchHistoryAdapter(this, searchHistory)
    }

    private val linearLayoutManager: LinearLayoutManager by lazy {
        LinearLayoutManager(this)
    }

    private val recyclerViewItemDecoration by lazy {
        SpaceItemDecoration(this)
    }

    override fun getLayoutResId(): Int {
        return R.layout.activity_search
    }

    override fun initView() {
        mPresenter.attachView(this)

        toolbar.apply {
            setSupportActionBar(this)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        hot_key_flow_layout.apply {
            setOnTagClickListener { _, position, _ ->
                if (hotKeys.size > 0) {
                    val hotKey = hotKeys[position]
                    queryByKey(hotKey.name)
                    true
                }
                false
            }
        }

        rv_search_history.apply {
            layoutManager = linearLayoutManager
            itemAnimator = DefaultItemAnimator()
            adapter = searchHistoryAdapter
        }

        searchHistoryAdapter.run {
            bindToRecyclerView(rv_search_history)
            onItemClickListener = this@SearchActivity.onItemClickListener
            onItemChildClickListener = this@SearchActivity.onItemChildClickListener
//            setEmptyView(R.layout.fragment_empty)
        }

        search_history_clear.setOnClickListener {
            searchHistory.clear()
            searchHistoryAdapter.replaceData(searchHistory)
            mPresenter.clearAllHistory()
        }

        // 获取搜索热词
        mPresenter.getHotKey()
    }

    override fun onResume() {
        super.onResume()
        mPresenter.queryHistory()
    }

    private val onItemClickListener = BaseQuickAdapter.OnItemClickListener { _, _, position ->
        queryByKey(searchHistory[position].key)
    }

    private val onItemChildClickListener = BaseQuickAdapter.OnItemChildClickListener { _, _, position ->
        val id = searchHistory[position].id
        mPresenter.deleteById(id)
    }

    override fun initData() {
    }

    override fun showHotKey(data: List<HotKey>) {
        hotKeys.addAll(data)
        hot_key_flow_layout.adapter = tagAdapter
    }

    private val tagAdapter = object : TagAdapter<HotKey>(hotKeys) {
        override fun getView(parent: FlowLayout?, position: Int, hotKey: HotKey?): View {
            val tv: TextView = LayoutInflater.from(parent?.context).inflate(
                R.layout.flow_layout_tv,
                hot_key_flow_layout,
                false
            ) as TextView
            tv.apply {
                text = hotKey?.name
                setTextColor(resources.getColor(R.color.white))
                setBackgroundColor(randomColor())
            }
            return tv
        }
    }

    override fun showSearchHistory(data: List<SearchHistoryBean>) {
        Log.d("Sam","history---->$data")
        data.let {
            searchHistoryAdapter.run {
                replaceData(it)
            }
        }
    }

    override fun showDeleteAll(msg: String) {
        toast(msg)
    }

    override fun showLoading() {
    }

    override fun hideLoading() {
    }

    override fun showError(errorMsg: String) {
        toast(errorMsg)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_search, menu)
        val searchView = menu?.findItem(R.id.action_search)?.actionView as SearchView
        searchView.apply {
            // 设置最大宽度mSearchView.setMaxWidth
            maxWidth = Integer.MAX_VALUE
            // 设置搜索框文本的hint,与mSearchAutoComplete.setHint功能一样
            queryHint = getString(R.string.search_hint)
            // 设置搜索框直接展开显示。左侧有放大镜(在搜索框中) 右侧无叉叉 有输入内容后有叉叉 不能关闭搜索框
            this.onActionViewExpanded()
            // false设置搜索框直接展开显示,true缩减为一个搜索图标。左侧有放大镜(在搜索框中) 右侧有叉叉 可以关闭搜索框
//            isIconified = true
            // 监听文本变化，调用查询
            setOnQueryTextListener(onQueryTextListenter)
            // 设置提交按钮是否可用(可见)
            isSubmitButtonEnabled = true
            // 设置"提交搜索"按钮的图标,就是那个">"右箭头提交按钮
            findViewById<ImageView>(R.id.search_go_btn).setImageResource(R.drawable.ic_search_white_24dp)
        }

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return super.onOptionsItemSelected(item)
    }

    private val onQueryTextListenter = object : SearchView.OnQueryTextListener {
        override fun onQueryTextChange(newText: String?): Boolean {
            // 文本改变的时候回调
            return false
        }

        override fun onQueryTextSubmit(query: String?): Boolean {
            queryByKey(query.toString())
            return false
        }
    }

    fun queryByKey(key: String) {
        toast("关键词--->$key")
        mPresenter.saveSearchKey(key)
        searchHistoryAdapter.notifyDataSetChanged()
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }

}