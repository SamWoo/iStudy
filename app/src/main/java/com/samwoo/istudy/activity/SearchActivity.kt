package com.samwoo.istudy.activity

import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
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
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.samwoo.istudy.R
import com.samwoo.istudy.adapter.SearchHistoryAdapter
import com.samwoo.istudy.base.BaseActivity
import com.samwoo.istudy.bean.HotKey
import com.samwoo.istudy.bean.SearchHistoryBean
import com.samwoo.istudy.constant.Constant
import com.samwoo.istudy.mvp.contract.SearchContract
import com.samwoo.istudy.mvp.presenter.SearchPresenter
import com.samwoo.istudy.util.SLog
import com.samwoo.istudy.util.randomColor
import com.samwoo.istudy.widget.SpaceItemDecoration
import com.zhy.view.flowlayout.FlowLayout
import com.zhy.view.flowlayout.TagAdapter
import kotlinx.android.synthetic.main.activity_search.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.intentFor
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

    override fun requestData() {
        // 获取搜索热词
        mPresenter.getHotKey()
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
            itemAnimator = DefaultItemAnimator() as RecyclerView.ItemAnimator
            adapter = searchHistoryAdapter
        }

        searchHistoryAdapter.run {
            bindToRecyclerView(rv_search_history)
            onItemClickListener = this@SearchActivity.onItemClickListener
            onItemChildClickListener = this@SearchActivity.onItemChildClickListener
//            setEmptyView(R.layout.layout_empty)
        }

        search_history_clear.setOnClickListener {
            alert(R.string.clear_all_history, R.string.clear_history) {
                positiveButton(getString(R.string.confirm)) {
                    searchHistory.clear()
                    searchHistoryAdapter.replaceData(searchHistory)
                    mPresenter.clearAllHistory()
                }
                negativeButton(getString(R.string.cancel)) {
                    dismiss()
                }
            }.show()
        }
    }

    override fun onResume() {
        super.onResume()
        // 获取搜索历史
        mPresenter.queryHistory()
    }
    /**
     * 监听点击item事件
     */
    private val onItemClickListener = BaseQuickAdapter.OnItemClickListener { _, _, position ->
        if (searchHistoryAdapter.data.size != 0) {
            val item = searchHistoryAdapter.data[position]
            queryByKey(item.key)
        }
    }

    /**
     * 监听点击item子控件事件
     */
    private val onItemChildClickListener = BaseQuickAdapter.OnItemChildClickListener { _, view, position ->
        if (searchHistoryAdapter.data.size != 0) {
            val item = searchHistoryAdapter.data[position]
            when (view.id) {
                R.id.iv_clear -> {
                    mPresenter.deleteById(item.id)
                    searchHistoryAdapter.remove(position)
                }
            }
        }
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
//                setBackgroundColor(randomColor())
                background = getBackGround()
            }
            return tv
        }
    }

    private fun getBackGround(): Drawable {
        val drawable = GradientDrawable()
        drawable.apply {
            cornerRadius = 8f
            setColor(randomColor())
        }
        return drawable
    }

    override fun showSearchHistory(data: List<SearchHistoryBean>) {
        Log.d("Sam", "history---->$data")
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
            // 文本提交或点击输入法搜索图标的时候回调
            queryByKey(query.toString())
            return false
        }
    }

    fun queryByKey(key: String) {
        toast("关键词--->$key")
        mPresenter.saveSearchKey(key)
        val intent = intentFor<GeneralActivity>(
            Pair(Constant.TYPE_KEY, Constant.TYPE.SEARCH_TYPE_KEY),
            Pair(Constant.SEARCH_KEY, key.trim())
        )
        startActivity(intent)
        overridePendingTransition(R.anim.left_in, R.anim.right_out)
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }

}