package com.samwoo.istudy.service

import android.app.IntentService
import android.content.Context
import android.content.Intent
import com.samwoo.istudy.bean.VersionCheck

class UpdateService : IntentService("") {
    companion object {
        fun startService(context: Context, vc: VersionCheck) {
            val intent = Intent(context, UpdateService::class.java)
            intent.putExtra("data", vc)
            context.startService(intent)
        }
    }

    override fun onHandleIntent(intent: Intent?) {
        if (intent == null) return
        val data = intent.getParcelableExtra<VersionCheck>("data")
        updateApp(data)
    }

    private fun updateApp(vc: VersionCheck) {
        val appVc = vc
        //本地路径非空，表示存储卡找到最新版本的安装包，此时无需下载即可进行安装操作
        if (vc.local_path.isNotEmpty()) {
        }

    }
}