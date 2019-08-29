package com.samwoo.istudy.activity

import android.content.Intent
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.samwoo.istudy.App.Companion.context
import com.samwoo.istudy.R
import com.samwoo.istudy.base.BaseActivity
import com.samwoo.istudy.constant.Constant
import com.samwoo.istudy.event.LoginEvent
import com.samwoo.istudy.fragment.*
import com.samwoo.istudy.util.Preference
import com.samwoo.istudy.util.SettingUtil
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.toast

class MainActivity : BaseActivity() {

    private var username: String by Preference(Constant.USERNAME_KEY, "")

    private val FRAGMENT_HOME = 0x01
    private val FRAGMENT_KNOWLEDGE_TREE = 0x02
    private val FRAGMENT_WX_ACCOUNT = 0x03
    private val FRAGMENT_NAVIGATION = 0X04
    private val FRAGMENT_PROJECT = 0x05


    private var mIndex = FRAGMENT_HOME

    private var homeFragment: HomeFragment? = null
    private var knowledgeTreeFragment: KnowledgeTreeFragment? = null
    private var wxAccountFragment: WxAccountFragment? = null
    private var navigationFragment: NavigationFragment? = null
    private var projectFragment: ProjectFragment? = null

    private lateinit var nav_nickname: TextView
    private lateinit var nav_avatar: CircleImageView

    //    override fun useEventBus(): Boolean =true
    override fun requestData() {}

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

        //抽屉item事件监听
        nav_view.run {
            nav_nickname = getHeaderView(0).findViewById(R.id.tv_nick)
            nav_avatar = getHeaderView(0).findViewById(R.id.profile_image)
            menu.findItem(R.id.nav_logout).isVisible = isLogin
            setNavigationItemSelectedListener(onDrawerNavigationItemSelectedListener)
        }

        //floatButton事件监听
        fab.run {
            setOnClickListener(onFabClickListener)
        }

        //nickname点击事件
        nav_nickname.run {
            text = if (!isLogin) getString(R.string.btn_login) else username
            setOnClickListener {
                if (!isLogin) {
                    val intent = intentFor<LoginActivity>()
                    startActivity(intent)
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
                } else {
//                    isClickable = false
                }
            }
        }

        nav_view.menu.findItem(R.id.nav_day_night).apply {
            icon = ContextCompat.getDrawable(
                context,
                if (SettingUtil.getIsNightMode()) R.drawable.ic_night else R.drawable.ic_day
            )
            title =
                getString(if (SettingUtil.getIsNightMode()) R.string.menu_night_mode else R.string.menu_day_mode)
        }


        //circleImage点击事件监听
        nav_avatar.run {
            setImageResource(if (!isLogin) R.mipmap.ic_launcher else R.mipmap.icon)
            setOnClickListener {
                if (isLogin) {
//                    val intent = intentFor<ProfileActivity>()
//                    startActivity(intent)
                } else {
                }
            }
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
            FRAGMENT_NAVIGATION -> {
                toolbar.title = getString(R.string.navigation)
                if (navigationFragment == null) {
                    navigationFragment = NavigationFragment.instance()
                    transaction.add(R.id.container, navigationFragment!!, "navigation")
                } else {
                    transaction.show(navigationFragment!!)
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
        navigationFragment?.let { transaction.hide(it) }
        projectFragment?.let { transaction.hide(it) }
    }


    //BottomNavigation点击事件监听
    private val onNavigationItemSelectListener =
        BottomNavigationView.OnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.action_home -> {
                    showFragment(FRAGMENT_HOME)
                }
                R.id.action_knowledge_system -> {
                    showFragment(FRAGMENT_KNOWLEDGE_TREE)
                }
                R.id.action_wx_account -> {
                    showFragment(FRAGMENT_WX_ACCOUNT)
                }
                R.id.action_navigation -> {
                    showFragment(FRAGMENT_NAVIGATION)
                }
                R.id.action_project -> {
                    showFragment(FRAGMENT_PROJECT)
                }
            }
            true
        }


    //左侧抽屉菜单点击事件监听
    private val onDrawerNavigationItemSelectedListener =
        NavigationView.OnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_like -> {
                    if (isLogin) {//收藏列表,需登录
                        val intent = intentFor<GeneralActivity>(
                            Pair(Constant.TYPE_KEY, Constant.TYPE.COLLECT_TYPE_KEY)
                        )
                        startActivity(intent)
                    } else {
                        val intent = intentFor<LoginActivity>()
                        startActivity(intent)
                    }
                }
                R.id.nav_day_night -> {//日/夜j间模式切换
                    if (SettingUtil.getIsNightMode()) {
                        nav_view.menu.findItem(R.id.nav_day_night).apply {
                            icon = ContextCompat.getDrawable(context, R.drawable.ic_day)
                            title = getString(R.string.menu_day_mode)
                        }
                        SettingUtil.setIsNightMode(false)
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    } else {
                        nav_view.menu.findItem(R.id.nav_day_night).apply {
                            icon = ContextCompat.getDrawable(context, R.drawable.ic_night)
                            title = getString(R.string.menu_night_mode)
                        }
                        SettingUtil.setIsNightMode(true)
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    }
//                    window.setWindowAnimations(R.style.WindowAnimationFadeInOut)
//                    recreate()
                    reStartActivity()
                }
                R.id.nav_settings -> {//设置界面
                    val intent = intentFor<SettingsActivity>()
                    startActivity(intent)
                }
                R.id.nav_logout -> logout()
            }
            true
        }

    //重新加载, 会导致闪屏问题，不建议使用
    override fun recreate() {
        try {
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            if (homeFragment != null) fragmentTransaction.remove(homeFragment!!)
            if (knowledgeTreeFragment != null) fragmentTransaction.remove(knowledgeTreeFragment!!)
            if (wxAccountFragment != null) fragmentTransaction.remove(wxAccountFragment!!)
            if (navigationFragment != null) fragmentTransaction.remove(navigationFragment!!)
            if (projectFragment != null) fragmentTransaction.remove(projectFragment!!)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        super.recreate()
    }

    //restart activity
    private fun reStartActivity() {
        startActivity(intentFor<MainActivity>())
        finish()
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
    }

    //logout
    private fun logout() {
        val dialog = AlertDialog.Builder(this).run {
            setTitle(R.string.logout_title)
            setMessage(R.string.logout_msg)
            setIcon(R.mipmap.icon)
            setPositiveButton(R.string.confirm) { _, _ ->
                isLogin = false
                doAsync {
                    //            Preference.clearPreference()
                    Preference.deleteCookie()
                }
                EventBus.getDefault().post(LoginEvent(false))
            }
            setNegativeButton(R.string.cancel) { dialog, _ ->
                dialog.dismiss()
            }
            create()
        }
        dialog.show()
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
            FRAGMENT_NAVIGATION -> {
                navigationFragment?.scrollToTop()
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
                val intent = intentFor<SearchActivity>()
                startActivity(intent)
                true
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

    //LoginEvent
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onLoginEvent(event: LoginEvent) {
        nav_view.menu.findItem(R.id.nav_logout).isVisible = event.isLogin
        when (event.isLogin) {
            true -> {
                toast("登录成功!!")
                nav_nickname.text = username
                nav_avatar.setImageResource(R.mipmap.icon)
//                homeFragment?.lazyLoad()
            }
            else -> {
                toast("退出登录!!")
//                Preference.clearPreference()
                nav_nickname.text = getString(R.string.btn_login)
                nav_avatar.setImageResource(R.mipmap.ic_launcher)
//                homeFragment?.lazyLoad()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        homeFragment = null
        knowledgeTreeFragment = null
        wxAccountFragment = null
        navigationFragment = null
        projectFragment = null
    }
}
