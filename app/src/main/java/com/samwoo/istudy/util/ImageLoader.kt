package com.samwoo.istudy.util

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions

object ImageLoader {
    /**
    * 加载图片
    * @param context
    * @param url
    * @param imageView
    */
    fun load(context: Context?, url: String?, imageView: ImageView?) {
        imageView?.let {
            Glide.with(context!!)
                .load(url)
                .transition(DrawableTransitionOptions().crossFade())
                .into(imageView)
        }
    }
}