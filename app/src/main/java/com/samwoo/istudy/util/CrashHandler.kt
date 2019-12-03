package com.samwoo.istudy.util

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Environment
import android.os.Looper
import android.os.SystemClock
import com.samwoo.istudy.App
import com.samwoo.istudy.activity.SplashActivity
import org.jetbrains.anko.toast
import java.io.File
import java.io.FileOutputStream
import java.io.PrintWriter
import java.io.StringWriter
import java.lang.reflect.Field
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class CrashHandler private constructor() : Thread.UncaughtExceptionHandler {

    private lateinit var context: Context
    private var mDefaultHandler: Thread.UncaughtExceptionHandler? = null
    private val infos = HashMap<String, String>()
    private val formatter = SimpleDateFormat("yyyy-MM-dd")

    companion object {
        private var instance: CrashHandler? = null
        fun getInstance(): CrashHandler? {
            if (null == instance) {
                synchronized(CrashHandler::class.java) {
                    if (null == instance) {
                        instance = CrashHandler()
                    }
                }
            }
            return instance
        }
    }

    fun init(context: Context) {
        this.context = context
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler(this)
    }

    override fun uncaughtException(t: Thread?, e: Throwable?) {
        if (!handlerException(e) && mDefaultHandler != null) {
            mDefaultHandler!!.uncaughtException(t, e)
        } else {
            val am = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val intent = Intent(context, SplashActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            intent.putExtra("crash", true)
            val pi = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_ONE_SHOT)
            am.set(AlarmManager.ELAPSED_REALTIME, System.currentTimeMillis() + 1000, pi)
            android.os.Process.killProcess(android.os.Process.myPid())
            System.exit(0)
            System.gc()
        }
    }

    /**
     * handler Exception
     * 处理未捕获的异常信息
     * @param throwable
     * @return
     */
    private fun handlerException(throwable: Throwable?): Boolean {
        if (null == throwable) return false
        try {
            Thread(Runnable {
                Looper.prepare()
                Looper.loop()
            }).start()
            getDeviceInfo(context)
            val fileName = saveCrashInfoToFile(throwable)
            if (fileName.isNullOrEmpty()) {
                App.context.toast("iStudy异常崩溃信息${fileName}已保存")
            }
            SystemClock.sleep(1000)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return true
    }

    /**
     * 采集应用版本信息和设备信息
     *
     * @param context
     */
    private fun getDeviceInfo(context: Context) {
//获取APP版本信息
        val pm = context.packageManager
        try {
            val info = pm.getPackageInfo(context.packageName, PackageManager.GET_ACTIVITIES)
            if (null != info) {
                infos["VersionName"] = info.versionName
                infos["VersionCode"] = "${info.versionCode}"
            }
        } catch (e: PackageManager.NameNotFoundException) {
            SLog.e("Sam", "An error occurred when collect package info.")
        }

        //获取系统设备相关信息
        val fields: Array<Field> = Build::class.java.declaredFields
        for (field in fields) {
            try {
                field.isAccessible = true
                infos[field.name] = field.get(null).toString()
            } catch (e: IllegalAccessException) {
                SLog.e("Sam", "An error occured when collect package info.")
            }
        }
    }

    /**
     * 把崩溃信息写进文件中
     *
     * @param throwable
     */
    private fun saveCrashInfoToFile(throwable: Throwable?): String? {
        val sb = StringBuilder()
        try {
            //拼接时间
            sb.append("\r\n").append(formatCurrentDate()).append("\n")
            //拼接版本信息和设备信息
            for (entry in infos.entries) {
                val key = entry.key
                val value = entry.value
                sb.append(key).append("=").append(value).append("\n")
            }
            //获取崩溃日志信息
            val writer = StringWriter()
            val printWriter = PrintWriter(writer)
            throwable!!.printStackTrace(printWriter)
            printWriter.flush()
            printWriter.close()

            val result = writer.toString()
            sb.append(result)
            //写崩溃信息到文件中
            return writeFile(sb.toString())
        } catch (e: Exception) {
            //异常处理
            SLog.e("Sam", "an error occurred while writing file...")
            sb.append("an error occurred while writing file...")
            writeFile(sb.toString())
        }
        return null
    }

    /**
     * 将字符串写入日志文件并返回文件名
     *
     * @param str
     * @return
     */
    private fun writeFile(str: String): String {
        //文件记录时间
        val time = formatter.format(Date())
        //文件名
        val fileName = "crash_$time.log"
        if (hasExternalStorage()) {
            //文件存储的绝对路径
            val path =
                Environment.getExternalStorageDirectory().absolutePath + File.separator + "iStudy_Crash" + File.separator
            val dir = File(path)
            if (!dir.exists()) {
                dir.mkdirs()
            }
            try {
                val fos = FileOutputStream(path + fileName, true)
                fos.write(str.toByteArray())
                fos.flush()
                fos.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return fileName
    }

    private fun hasExternalStorage(): Boolean {
        val state = Environment.getExternalStorageState()
        return state.isNotEmpty() && state == Environment.MEDIA_MOUNTED
    }
}