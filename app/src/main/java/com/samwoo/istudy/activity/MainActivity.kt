package com.samwoo.istudy.activity

import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.samwoo.istudy.R
import com.samwoo.istudy.base.BaseActivity
import com.samwoo.istudy.fragment.HomeFragment
import com.samwoo.istudy.fragment.KnowledgeTreeFragment
import com.samwoo.istudy.fragment.ProjectFragment
import com.samwoo.istudy.fragment.WxAccountFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import org.jetbrains.anko.toast

class MainActivity : BaseActivity() {
    private val FRAGMENT_HOME = 0x01
    private val FRAGMENT_KNOWLEDGE_TREE = 0x02
    private val FRAGMENT_WX_ACCOUNT = 0x03
    private val FRAGMENT_PROJECT = 0x05


    private var mIndex = FRAGMENT_HOME

    private var homeFragment: HomeFragment? = null
    private var knowledgeTreeFragment: KnowledgeTreeFragment? = null
    private var wxAccountFragment: WxAccountFragment? = null
    private var projectFragment: ProjectFragment? = null

    override fun getLayoutResId(): Int {
        return R.layout.activity_main
    }

    override fun initData() {
    }

    override fun initView() {
        toolbar.run {
            title = getString(R.string.app_name)
            setSupportActionBar(this)
        }

        bottom_navigation.run {
            setOnNavigationItemSelectedListener(onNavigationItemSelectListener)
            selectedItemId = R.id.action_home
        }

        drawer_layout.run {
            var toggle = ActionBarDrawerToggle(
                this@MainActivity,
                this,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
            )
            addDrawerListener(toggle)
            toggle.syncState()
        }

        nav_view.run {
            setNavigationItemSelectedListener(onDrawerNavigationItemSelectedListener)
        }

        fab.run {
            setOnClickListener(onFabClickListener)
        }

        showFragment(mIndex)
    }

    private fun showFragment(index: Int) {
        val transaction = supportFragmentManager.beginTransaction()
        hideFragments(transaction)
        mIndex = index
        when (index) {
            FRAGMENT_HOME -> {
                toolbar.title = getString(R.string.home)
                if (homeFragment == null) {
                    homeFragment = HomeFragment.instance()
                    transaction.add(R.id.container, homeFragment!!, "home")
                } else {
                    transaction.show(homeFragment!!)
                }
            }
            FRAGMENT_KNOWLEDGE_TREE -> {
                toolbar.title = getString(R.string.knowledge_system)
                if (knowledgeTreeFragment == null) {
                    knowledgeTreeFragment = KnowledgeTreeFragment.instance()
                    transaction.add(R.id.container, knowledgeTreeFragment!!, "knowledge")
                } else {
                    transaction.show(knowledgeTreeFragment!!)
                }
            }
            FRAGMENT_WX_ACCOUNT -> {
                toolbar.title = getString(R.string.wx_account)
                if (wxAccountFragment == null) {
                    wxAccountFragment = WxAccountFragment.instance()
                    transaction.add(R.id.container, wxAccountFragment!!, "wxaccount")
                } else {
                    transaction.show(wxAccountFragment!!)
                }
            }
            FRAGMENT_PROJECT -> {
                toolbar.title = getString(R.string.project)
                if (projectFragment == null) {
                    projectFragment = ProjectFragment.instance()
                    transaction.add(R.id.container, projectFragment!!, "project")
                } else {
                    transaction.show(projectFragment!!)
                }
            }
        }
        transaction.commit()
    }

    private fun hideFragments(transaction: FragmentTransaction) {
        homeFragment?.let { transaction.hide(it) }
        knowledgeTreeFragment?.let { transaction.hide(it) }
        wxAccountFragment?.let { transaction.hide(it) }
        projectFragment?.let { transaction.hide(it) }
    }


    //BottomNavigation点击事件监听
    private val onNavigationItemSelectListener = BottomNavigationView.OnNavigationItemSelectedListener {
        when (it.itemId) {
            R.id.action_home -> {
                showFragment(FRAGMENT_HOME)
                true
            }
            R.id.action_knowledge_system -> {
                showFragment(FRAGMENT_KNOWLEDGE_TREE)
                true
            }
            R.id.action_wx_account -> {
                showFragment(FRAGMENT_WX_ACCOUNT)
                true
            }
            R.id.action_project -> {
                showFragment(FRAGMENT_PROJECT)
                true
            }
            else -> {
                false
            }
        }
    }


    //左侧抽屉菜单点击事件监听
    private val onDrawerNavigationItemSelectedListener = NavigationView.OnNavigationItemSelectedListener {
        when (it.itemId) {
            R.id.nav_home -> {
                true
            }
            else -> {
                false
            }
        }
    }

    //浮点按钮事件监听
    private val onFabClickListener = View.OnClickListener {
        when (mIndex) {
            FRAGMENT_HOME -> {
                homeFragment?.scrollToTop()
            }
            FRAGMENT_KNOWLEDGE_TREE -> {
                knowledgeTreeFragment?.scrollToTop()
            }
            FRAGMENT_WX_ACCOUNT -> {
                wxAccountFragment?.scrollToTop()
            }
            FRAGMENT_PROJECT -> {
                projectFragment?.scrollToTop()
            }
        }
    }

    //搜索菜单
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_activity_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_search -> {
                toast("搜索")
            }
        }
        return super.onOptionsItemSelected(item)
    }

    /**
     * 2s内连续点击两次则退出
     */
    private var mExitTime: Long = 0

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (System.currentTimeMillis().minus(mExitTime) <= 2000) {
                finish()
            } else {
                mExitTime = System.currentTimeMillis()
            }
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

}
