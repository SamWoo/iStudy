package com.samwoo.istudy.mvp.contract

import android.content.Context
import android.widget.ImageView
import com.samwoo.istudy.base.IView
import com.samwoo.istudy.bean.Girl

interface GirlContract {
    interface View : IView {
        fun showGirlPhoto(data: List<Girl>)
    }

    interface Presenter {
        fun getGirlPhoto(page: Int)
        fun savePhoto(
            context: Context,
            photoUrl: String,
            desc: String,
            imageView: ImageView
        )
    }
}