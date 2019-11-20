package com.samwoo.istudy.view

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import com.samwoo.istudy.R
import com.samwoo.istudy.util.DisplayManager.dp2px

class TipDialog : AlertDialog {
    private var width: Int = 0
    private var height: Int = 0
    private var layout: View
    private var title: String? = null

    constructor(
        context: Context,
//        width: Int,
//        height: Int,
        layout: View,
        style: Int
    ) : super(context, style) {
//        this.width = width
//        this.height = height
        this.layout = layout
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout)
        val params: WindowManager.LayoutParams = this.window.attributes
        params.gravity = Gravity.CENTER
//        params.width = WindowManager.LayoutParams.MATCH_PARENT
        params.height = dp2px(200)
        this.window.attributes = params
    }

    override fun setTitle(title: CharSequence?) {
        layout.findViewById<TextView>(R.id.tv_title).text = title
    }

    override fun setMessage(message: CharSequence?) {
        layout.findViewById<TextView>(R.id.tv_msg).text = message
    }

    fun setCancleBtnText(str: String) {
        layout.findViewById<TextView>(R.id.btn_cancel).text = str
    }

    fun setConfirmBtnText(str: String) {
        layout.findViewById<TextView>(R.id.btn_confirm).text = str
    }

    fun setCancleBtnClickListener(listener: View.OnClickListener) {
        layout.findViewById<TextView>(R.id.btn_cancel).setOnClickListener(listener)
    }

    fun setConfirmBtnClickListener(listener: View.OnClickListener) {
        layout.findViewById<TextView>(R.id.btn_confirm).setOnClickListener(listener)
    }
}