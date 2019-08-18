package com.samwoo.istudy.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.samwoo.istudy.BuildConfig
import com.samwoo.istudy.constant.Constant
import com.samwoo.istudy.event.NetworkChangeEvent
import com.samwoo.istudy.util.NetworkUtil
import com.samwoo.istudy.util.Preference
import com.samwoo.istudy.util.SLog
import org.greenrobot.eventbus.EventBus

class NetworkChangeReceiver : BroadcastReceiver() {
    //上次的网络状态
    private var lastNetworkStatus: Boolean by Preference(Constant.LAST_NETWORK_STATUS_KEY, true)

    override fun onReceive(context: Context?, intent: Intent?) {
        /**
         * 每次进入activity/fragment都会触发广播，故要缓存上次进入时的网络状态，有两种情况：
         * 1.上次网络状态为连接状态，再次进入页面，此时不post EventBus事件；
         * 2.上次网络状态为未连接
         */
        var isConnected = NetworkUtil.isNetworkConnected(context!!)
        SLog.d("do receiver-->post event....${isConnected}/$lastNetworkStatus/${intent?.action}")
        if (isConnected) {
            if (isConnected != lastNetworkStatus) {
                SLog.d("first")
                EventBus.getDefault().post(NetworkChangeEvent(isConnected))
            }
        } else {
            SLog.d("second")
            EventBus.getDefault().post(NetworkChangeEvent(isConnected))
        }
    }

}