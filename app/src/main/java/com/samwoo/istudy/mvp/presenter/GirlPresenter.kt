package com.samwoo.istudy.mvp.presenter

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import android.widget.ImageView
import com.samwoo.istudy.R
import com.samwoo.istudy.base.BasePresenter
import com.samwoo.istudy.bean.GankBody
import com.samwoo.istudy.callback.Callback
import com.samwoo.istudy.mvp.contract.GirlContract
import com.samwoo.istudy.mvp.model.GirlModel
import com.samwoo.istudy.util.SLog
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.toast
import org.jetbrains.anko.uiThread
import java.io.File
import java.io.File.separator
import java.io.FileOutputStream
import java.io.InputStream
import java.net.URL

class GirlPresenter : BasePresenter<GirlContract.View>(), GirlContract.Presenter {
    private val model: GirlModel by lazy { GirlModel() }

    override fun getGirlPhoto(page: Int) {
        model.getGirlPhoto(page, object : Callback<GankBody, String> {
            override fun onSuccess(data: GankBody) {
                if (isViewAttached()) {
//                    mView?.showGirlPhoto(data.results)
                    mView?.onSuccess(data.results)
                }
            }

            override fun onFail(msg: String) {
                if (isViewAttached()) {
                    mView?.showError(msg)
                }
            }

        })
    }

    //保存图片
    override fun savePhoto(
        context: Context,
        photoUrl: String,
        desc: String,
        imageView: ImageView
    ) {
        val pathName = Environment.getExternalStorageDirectory().path + separator + "myFolder"
        val file = File(pathName)
        if (!file.exists()) file.mkdir()

        val fileName = File(pathName, "${desc}.jpg")
        if (fileName.exists()) {
            context.toast("该图片已存在!!")
            return
//                fileName.delete()
        }

        //下载成功标志位
        var successed = true

        doAsync {
            var ins: InputStream? = null
            var fos: FileOutputStream? = null
            try {
                ins = URL(photoUrl).openStream()
                var bitmap = BitmapFactory.decodeStream(ins)
                fos = FileOutputStream(fileName)
                bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, fos)
                fos.flush()
            } catch (e: Exception) {
                successed = false
                SLog.d("${e.printStackTrace()}")
            } finally {
                fos?.close()
                ins?.close()
            }
            uiThread {
                when (successed) {
                    true -> {
                        imageView.setImageResource(R.drawable.ic_photo_downloaded)
                        context.toast("${fileName}下载成功!!")
                        //发广播告诉相册有图片需要更新，这样可以在图册下看到保存的图片了
                        val intent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
                        intent.data = Uri.fromFile(fileName)
                        context.sendBroadcast(intent)
                    }
                    else -> {
                        context.toast("${fileName}下载失败!!")
                        return@uiThread
                    }
                }
            }
        }
    }
}