package com.samwoo.istudy.view

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import com.samwoo.istudy.R
import kotlinx.android.synthetic.main.dialog_loading.*

class LoadingDialog : AlertDialog {

    companion object {
        fun getInstance(context: Context): LoadingDialog {
            val loadingDialog = LoadingDialog(context, R.style.TransparentDialog)
            loadingDialog.apply {
                setCancelable(false)
                setCanceledOnTouchOutside(false)
            }
            return loadingDialog
        }
    }

    constructor(context: Context, themeResId: Int) : super(context, themeResId)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.setContentView(R.layout.dialog_loading)
    }

    override fun show() {
        super.show()
        avi.show()
    }

    override fun dismiss() {
        super.dismiss()
        avi.hide()
    }
}