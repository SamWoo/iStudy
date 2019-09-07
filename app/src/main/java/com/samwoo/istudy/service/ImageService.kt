package com.samwoo.istudy.service

import android.app.IntentService
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Parcelable
import com.samwoo.istudy.bean.Girl
import com.samwoo.istudy.util.ImageLoader
import org.greenrobot.eventbus.EventBus


class ImageService : IntentService("") {

    companion object {
        fun startService(context: Context, datas: List<Girl>) {
            val intent = Intent(context, ImageService::class.java)
            intent.putParcelableArrayListExtra("data", datas as ArrayList<out Parcelable>)
            context.startService(intent)
        }
    }

    override fun onHandleIntent(intent: Intent?) {
        if (intent == null) return
        val datas = intent.getParcelableArrayListExtra<Girl>("data")
        handleGirlData(datas)
    }

    private fun handleGirlData(datas: List<Girl>?) {
        if (datas!!.isEmpty()) return
        for (data in datas) {
            val bitmap: Bitmap? = ImageLoader.load(this, data.url)
            if (bitmap != null) {
                data.width = bitmap.width
                data.height = bitmap.height
            }
        }
        EventBus.getDefault().post(datas)
    }
}