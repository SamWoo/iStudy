package com.samwoo.istudy.base

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.samwoo.istudy.constant.Constant
import com.samwoo.istudy.util.Preference
import org.greenrobot.eventbus.EventBus

abstract class BaseActivity : AppCompatActivity() {
    //检查是否登录
    protected var isLogin: Boolean by Preference(Constant.LOGIN_KEY, false)

    //是否使用EventBus
    open fun useEventBus(): Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutResId())
        if (useEventBus()) {
            EventBus.getDefault().register(this)
        }

        initData() //先加载数据，否则adapter获取不到数据
        initView()
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onDestroy() {
        if (useEventBus()) {
            EventBus.getDefault().unregister(this)
        }
        super.onDestroy()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        //fragment逐个退出
        val count = supportFragmentManager.backStackEntryCount
        if (count == 0) {
            super.onBackPressed()
        } else {
            supportFragmentManager.popBackStack()
        }
    }

    //获取布局id
    abstract fun getLayoutResId(): Int

    //初始化控件
    abstract fun initView()

    //初始化数据
    abstract fun initData()
}