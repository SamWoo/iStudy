package com.samwoo.istudy.util

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.samwoo.istudy.R
import android.graphics.Bitmap
import com.bumptech.glide.request.FutureTarget
import com.bumptech.glide.request.RequestOptions.bitmapTransform
import java.lang.Exception


object ImageLoader {
    /**
     * 加载图片
     * @param context
     * @param url
     * @param imageView
     */
    fun load(context: Context?, url: String?, imageView: ImageView?) {
        //通过RequestOption设置属性
        val options: RequestOptions = RequestOptions().run {
            //            placeholder(R.drawable.ic_loading)    //加载成功之前占位图
            error(R.drawable.ic_load_fail)    //加载错误之后的错误图
//            override(100, 100)    //指定图片的尺寸
//            fitCenter()   //指定图片的缩放类型为fitCenter （等比例缩放图片，宽或者是高等于ImageView的宽或者是高。是指其中一个满足即可不会一定铺满 imageview）
//            centerCrop()//指定图片的缩放类型为centerCrop （等比例缩放图片，直到图片的宽高都大于等于ImageView的宽度，然后截取中间的显示。）
//            skipMemoryCache(true)    //不使用内存缓存
//            diskCacheStrategy(DiskCacheStrategy.ALL)    //缓存所有版本的图像
//            diskCacheStrategy(DiskCacheStrategy.NONE)    //不使用硬盘本地缓存
//            diskCacheStrategy(DiskCacheStrategy.DATA)    //只缓存原来分辨率的图片
//            diskCacheStrategy(DiskCacheStrategy.RESOURCE)    //只缓存最终的图片
        }

        imageView?.let {
            Glide.with(context!!)
                .load(url)
                .apply(options)
                .transition(DrawableTransitionOptions().crossFade())
                .into(it)
        }
    }

    fun load(context: Context, url: String, iv: ImageView, placeholder: Int) {
        val options = RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .placeholder(placeholder)

        Glide.with(context)
            .load(url)
            .apply(options)//让Glide既缓存全尺寸图片，下次在任何ImageView中加载图片的时候，全尺寸的图片将从缓存中取出，重新调整大小，然后缓存
            .transition(DrawableTransitionOptions().crossFade())
            .into(iv)
    }

    fun load(context: Context, resId: Int, iv: ImageView) {
        Glide.with(context)
            .load(resId)
            .transition(DrawableTransitionOptions().crossFade())
            .into(iv)
    }

    /**
     * 需要在子线程执行
     *
     * @param context
     * @param url
     * @return
     */
    fun load(context: Context, url: String): Bitmap? {
        val options = RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
        var bitmap: Bitmap? = null
        val bmp = Glide.with(context)
            .asBitmap()
            .load(url)
            .submit()
        try {
            bitmap = bmp.get()
        } catch (e: Exception) {
            print(e.printStackTrace())
        }
        return bitmap
    }
}