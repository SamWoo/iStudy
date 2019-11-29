package com.samwoo.istudy.activity

import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
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
import com.samwoo.istudy.App
import com.samwoo.istudy.App.Companion.context
import com.samwoo.istudy.R
import com.samwoo.istudy.base.BaseActivity
import com.samwoo.istudy.constant.Constant
import com.samwoo.istudy.event.LoginEvent
import com.samwoo.istudy.fragment.*
import com.samwoo.istudy.mvp.contract.MainContract
import com.samwoo.istudy.mvp.presenter.MainPresenter
import com.samwoo.istudy.util.NetworkUtil
import com.samwoo.istudy.util.Preference
import com.samwoo.istudy.util.SLog
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
import org.jetbrains.anko.uiThread
import java.io.IOException
import java.net.URL

class MainActivity : BaseActivity(), MainContract.View {

    private var username: String by Preference(Constant.USERNAME_KEY, "")
    private var level: Int by Preference(Constant.LEVEL_KEY, 1)
    private var rank: Int by Preference(Constant.RANK_KEY, 1)
    private var coinCount: Int by Preference(Constant.COIN_KEY, 1)

    private var mPresenter: MainPresenter? = null

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
    private lateinit var nav_rank: TextView
    private lateinit var nav_level: TextView
    private lateinit var nav_coin: TextView
    private lateinit var nav_avatar: CircleImageView

    //    override fun useEventBus(): Boolean =true
    override fun requestData() {}

    override fun getLayoutResId(): Int {
        return R.layout.activity_main
    }

    //检查自动升级功能
    override fun initData() {
        //检查版本是否需要更新升级
//        UpdateAppUtil.bind(this).checkUpdate()
    }

    override fun initView() {
        mPresenter = MainPresenter()
        mPresenter?.attachView(this)

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
            nav_rank = getHeaderView(0).findViewById(R.id.tv_rank)
            nav_level = getHeaderView(0).findViewById(R.id.tv_level)
            nav_coin = getHeaderView(0).findViewById(R.id.tv_coin)
            if (isLogin && NetworkUtil.isNetworkConnected(App.context)) mPresenter?.getUserInfo()
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
        //获取Bing每日一图
        if (NetworkUtil.isNetworkConnected(context)) getBingPic(Constant.BING_PIC_URL)
    }

    private fun getBingPic(url: String) {
        var pic: Drawable? = null
        doAsync {
            try {
                //            val picUrl = URL(url).readText()
//            pic = Drawable.createFromStream(URL(url).openStream(), "bing_pic.jpg")
                val b = BitmapFactory.decodeStream(URL(url).openStream())
//                val bm = Bitmap.createScaledBitmap(b, 320, 180, false)
                pic = BitmapDrawable(resources, b)
                SLog.d("Sam", "$pic")
            }catch (exception:IOException){
                if (pic == null) {
                    pic = ContextCompat.getDrawable(context, R.drawable.side_nav_bar)
                }
            }
            uiThread {
                pic!!.alpha = 240
                nav_view.getHeaderView(0).background = pic
            }
        }

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
                R.id.nav_gallery -> {
                    val intent = intentFor<GeneralActivity>(
                        Pair(Constant.TYPE_KEY, Constant.TYPE.GANK_GIRL_PHOTOS)
                    )
                    startActivity(intent)

                }
                R.id.nav_day_night -> {//日/夜间模式切换
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
            setTitle(R.string.tip_msg)
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
        dialog.window.setWindowAnimations(R.style.DialogInOutStyle)

//        val view = LayoutInflater.from(this).inflate(R.layout.dialog_msg, null)
//        val dialog = TipDialog(this, view, R.style.TipDialogTheme)
//        dialog.setTitle("提示")
//        dialog.setMessage("是否退出？")
//        dialog.setCancleBtnText("我再想想！")
//        dialog.setConfirmBtnText("马上升级")
//        dialog.setConfirmBtnClickListener(View.OnClickListener {
//            dialog.dismiss()
//            isLogin = false
//                doAsync {
//                    //            Preference.clearPreference()
//                    Preference.deleteCookie()
//                }
//                EventBus.getDefault().post(LoginEvent(false))
//        })
//        dialog.setCancleBtnClickListener(View.OnClickListener {
//            dialog.dismiss()
//        })
//        dialog.show()
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
//                toast("搜索")
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
                mPresenter?.getUserInfo()
                nav_nickname.text = username
                nav_avatar.setImageResource(R.mipmap.icon)
//                homeFragment?.lazyLoad()
            }
            else -> {
                toast("退出登录!!")
//                Preference.clearPreference()
                nav_nickname.text = getString(R.string.btn_login)
                nav_avatar.setImageResource(R.mipmap.ic_launcher)
                nav_coin.text = resources.getString(R.string.coin_count)
                nav_rank.text = resources.getString(R.string.rank)
                nav_level.text = resources.getString(R.string.level)
//                homeFragment?.lazyLoad()
            }
        }
    }

    override fun getUserInfoSuccess() {
        nav_coin.text = coinCount.toString()
        nav_level.text = level.toString()
        nav_rank.text = rank.toString()
        SLog.d(
            "Sam", "积分：${nav_coin.text}\n" +
                    "等级：${nav_level.text}\n" +
                    "排名：${nav_rank.text}"
        )
    }

    override fun showLoading() {}

    override fun hideLoading() {}

    override fun showError(errorMsg: String) {}

    override fun onDestroy() {
        super.onDestroy()
        homeFragment = null
        knowledgeTreeFragment = null
        wxAccountFragment = null
        navigationFragment = null
        projectFragment = null

        if (mPresenter != null) {
            mPresenter?.detachView()
            mPresenter = null
        }
    }
}
