package com.samwoo.istudy.base

import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.samwoo.istudy.BuildConfig
import com.samwoo.istudy.constant.Constant
import com.samwoo.istudy.event.NetworkChangeEvent
import com.samwoo.istudy.receiver.NetworkChangeReceiver
import com.samwoo.istudy.util.Preference
import com.samwoo.istudy.util.SLog
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

abstract class BaseActivity : AppCompatActivity() {
    //检查是否登录
    protected var isLogin: Boolean by Preference(Constant.LOGIN_KEY, false)
    //上次的网络状态
    private var lastNetworkStatus: Boolean by Preference(Constant.LAST_NETWORK_STATUS_KEY, true)

    //是否使用EventBus
    open fun useEventBus(): Boolean = true

    //networkchange receiver
    private var receiver: NetworkChangeReceiver? = null

    //无网络-->有网络，重新请求数据
    open fun doReConnect() {
        requestData()
    }

    //开始请求数据
    abstract fun requestData()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutResId())
        //先加载数据，否则adapter获取不到数据
        initData()
        initView()
        requestData()
    }

    override fun onResume() {
        val intentFilter = IntentFilter()
        intentFilter.addAction(Constant.NETWORK_CHANGE)
        receiver = NetworkChangeReceiver()
        registerReceiver(receiver, intentFilter)
        super.onResume()
    }

    override fun onPause() {
        if (receiver != null) {
            unregisterReceiver(receiver)
            receiver = null
        }
        super.onPause()
    }

    override fun onStart() {
        if (useEventBus()) {
            if (!EventBus.getDefault().isRegistered(this)) EventBus.getDefault().register(this)
        }
        super.onStart()
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onDestroy() {
        if (useEventBus()) {
            if (EventBus.getDefault().isRegistered(this)) EventBus.getDefault().unregister(this)
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onNetworkChangeEvent(event: NetworkChangeEvent) {
        SLog.d("Sam", "${javaClass.simpleName}.isConnected--->${event.isConnected}")
        lastNetworkStatus = event.isConnected
        if (event.isConnected) doReConnect()
    }
}