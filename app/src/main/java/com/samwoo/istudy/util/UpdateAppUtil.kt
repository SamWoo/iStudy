package com.samwoo.istudy.util

import android.app.DownloadManager
import android.app.DownloadManager.Query
import android.app.DownloadManager.Request
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.Handler
import android.provider.MediaStore
import android.text.style.ForegroundColorSpan
import androidx.core.content.FileProvider
import com.google.gson.Gson
import com.samwoo.istudy.bean.VersionCheck
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.progressDialog
import org.jetbrains.anko.toast
import org.jetbrains.anko.uiThread
import java.io.File
import java.net.URL

class UpdateAppUtil {

    private var downloadId: Long = 0
    private val handler: Handler by lazy { Handler() }
    private lateinit var dialog: ProgressDialog
    private lateinit var appVc: VersionCheck


    companion object {
        private lateinit var context: Context
        private var instance: UpdateAppUtil? = null
        fun bind(context: Context): UpdateAppUtil {
            this.context = context
            if (instance == null)
                instance = UpdateAppUtil()
            return instance as UpdateAppUtil
        }
    }

    fun checkUpdate() {
        val pi = context.packageManager.getPackageInfo(context.packageName, 0)
        var result: String? = null
        //开启分线程执行后端接口调用
        doAsync {
            //从服务段获取版本升级信息
//            val url = "$checkUrl?package_name=${pi.packageName}&version_name=${pi.versionName}"
//            result = URL(url).readText()
            //测试使用爱奇艺的安装包，包名是com.qiyi.video
            result =
                "{\"app_name\":\"爱奇艺\",\"package_name\":\"com.qiyi.video\",\"version_code\":100200," +
                        "\"version_name\":\"10.8.5\",\"need_update\":true," +
                        "\"download_url\":\"https://3g.lenovomm.com/w3g/yydownload/com.qiyi.video/60020\"}"
            //回到主线程在界面上弹框提示待升级的版本
            uiThread {
                //                checkUpdate(result)
            }
        }
        //利用gson库将json字符串自动转换成对应的数据实例
        val vc = Gson().fromJson(result, VersionCheck::class.java)
        //检查本地是否有相同的版本的安装包
        vc.local_path = getLocalPath(vc)
        val message = "系统检测到${vc.app_name}的最新版本号是${vc.version_name}, " +
                if (vc.local_path.isNotEmpty()) "本次升级无需流量。" else "快去在线升级吧。"
        //高亮显示文本信息中的最新版本号
        val spanText = message.highlight(vc.version_name, ForegroundColorSpan(Color.RED))
        //弹出提示升级的对话框
        context.alert(spanText, "发现新版本") {
            positiveButton("确定升级") {
                //                UpdateService.startService(context, vc)
                startInstallApp(vc)
            }
            negativeButton("以后再说") {
                dismiss()
            }
        }.show()
    }


    private fun getLocalPath(vc: VersionCheck): String {
        var local_path = ""
        //遍历本地所有的APK文件
        val cursor = context.contentResolver.query(
            MediaStore.Files.getContentUri("external"), null,
            "mime_type=\"application/vnd.android.package-archive\"", null, null
        )
        while (cursor.moveToNext()) {
            //TITLE获取文件名，DATA获取文件完整路径，SIZE获取文件大小
            val path = cursor.getString(cursor.getColumnIndex(MediaStore.Files.FileColumns.DATA))
            //从apk文件中解析得到该安装包的程序信息
            val pi =
                context.packageManager.getPackageArchiveInfo(
                    path,
                    PackageManager.GET_ACTIVITIES
                )
            if (pi != null) {
                //找到包名和版本号都符合条件的apk文件
                if (vc.package_name == pi.packageName && vc.version_name == pi.versionName) {
                    local_path = path
                }
            }
        }
        cursor.close()
        return local_path
    }

    fun startInstallApp(vc: VersionCheck) {
        appVc = vc
        //本地路径非空，表示存储卡上找到最新版本的安装包，则直接进行安装操作；如果不存在，则从网络下载
        if (vc.local_path.isNotEmpty()) {
            handler.postDelayed(mInstall, 100)
        } else {
            //构建安装包下载地址的请求任务
            val down = Request(Uri.parse(vc.download_url))
            down.setAllowedNetworkTypes(Request.NETWORK_WIFI or Request.NETWORK_MOBILE)
            //隐藏通知栏上的下载信息
            down.setNotificationVisibility(Request.VISIBILITY_HIDDEN)
            down.setVisibleInDownloadsUi(false)
            //指定下载文件的保存路径
            down.setDestinationInExternalFilesDir(
                context,
                Environment.DIRECTORY_DOWNLOADS,
                "${vc.package_name}.apk"
            )
            //将请求任务添加到下载列表中
            downloadId = context.downloader.enqueue(down)
            handler.postDelayed(mRefresh, 100)
            //弹出进度对话框，用于展示下载进度
            val message = "正在下载${appVc.app_name}的安装包"
            dialog = context.progressDialog(message, "请稍等...")
            dialog.show()
        }
    }

    private val mRefresh = object : Runnable {
        override fun run() {
            var isFinished = false
            //构建下载任务的查询对象
            val down_query = Query()
            down_query.setFilterById(downloadId)
            //获取下载管理器的查询游标，轮询下载任务的下载进度
            val cursor = context.downloader.query(down_query)
            while (cursor.moveToNext()) {
                val uriIdx = cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI)
                val totalSizeIdx = cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES)
                val nowSizeIdx =
                    cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR)
                //计算当前的下载进度
                val progress =
                    (100 * cursor.getLong(nowSizeIdx) / cursor.getLong(totalSizeIdx)).toInt()
                if (cursor.getString(uriIdx) == null) break
                //实时刷新进度对话框的下载进度
                dialog.progress = progress
                if (progress >= 100) isFinished = true
            }
            cursor.close()
            if (!isFinished) {
                handler.postDelayed(this, 100)
            } else {
                //下载完成，进行后续的安装操作
                dialog.dismiss()
                context.toast("${appVc.app_name}的安装包已下载完成")
                handler.postDelayed(mInstall, 100)
            }
        }
    }

    private val mInstall = Runnable {
        val apkPath = File(
            context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS),
            "${appVc.package_name}.apk"
        )
        //安装APK
        val intent = Intent(Intent.ACTION_VIEW)
        //版本在7.0以上的是不能通过uri访问的
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            val apkUri: Uri =
                FileProvider.getUriForFile(context, "com.samwoo.istudy.fileprovider", apkPath)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive")
        } else {
            intent.setDataAndType(
                Uri.fromFile(apkPath),
                "application/vnd.android.package-archive"
            )
        }
        context.startActivity(intent)

        /**
        val message = "正在安装${appVc.app_name}的最新版本"
        dialog = context.progressDialog(message, "请稍等...")
        dialog.show()
        handler.postDelayed({
        if (dialog.isShowing) {
        dialog.dismiss()
        //弹出升级完成的提醒对话框
        val message = "${appVc.app_name}的${appVc.version_name}版本升级完成。"
        val spanText =
        message.highlight(appVc.version_name, ForegroundColorSpan(Color.RED))
        context.alert(spanText, "升级完成") {
        positiveButton("我知道了") { dismiss() }
        }.show()
        }
        }, 5000)
         */
    }
}