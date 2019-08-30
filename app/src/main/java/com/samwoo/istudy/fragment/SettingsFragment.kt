package com.samwoo.istudy.fragment

import android.content.SharedPreferences
import android.os.Bundle
import android.preference.Preference
import android.preference.PreferenceFragment
import androidx.appcompat.app.AlertDialog
import com.samwoo.istudy.R
import com.samwoo.istudy.util.CacheUtil
import org.jetbrains.anko.browse
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class SettingsFragment : PreferenceFragment(), SharedPreferences.OnSharedPreferenceChangeListener {

    companion object {
        fun instance(): SettingsFragment {
            val fragment = SettingsFragment()
//            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addPreferencesFromResource(R.xml.pref_settings)
        setHasOptionsMenu(true)

        //初始化信息
        showDefaultInfo()

        //清缓存
        findPreference("clearCache").onPreferenceClickListener =
            Preference.OnPreferenceClickListener {
                AlertDialog.Builder(activity!!).run {
                    setIcon(R.mipmap.icon)
                    setTitle(R.string.clear_cache)
                    setMessage(R.string.clear_cache_msg)
                    setPositiveButton(R.string.confirm) { _, _ ->
                        doAsync {
                            CacheUtil.clearAllCache(activity!!)
                            uiThread { showDefaultInfo() }
                        }
                    }
                    setNegativeButton(R.string.cancel) { dialog, _ ->
                        dialog.dismiss()
                    }
                    create().show()
                }
                false
            }

        //version
        try {
            val version = "当前版本 " + activity.packageManager.getPackageInfo(
                activity.packageName,
                0
            ).versionName
            findPreference("version").summary = version
        } catch (e: Exception) {
            e.printStackTrace()
        }

        //sourceCode
        findPreference("sourceCode").setOnPreferenceClickListener {
            browse(getString(R.string.source_code_url))
            false
        }

        //copyright
        findPreference("copyRight").onPreferenceClickListener =
            Preference.OnPreferenceClickListener {
                AlertDialog.Builder(activity!!).run {
                    setTitle(R.string.copyright)
                    setMessage(R.string.copyright_summary)
                    setCancelable(true)
                    show()
                }
                false
            }
    }

    private fun showDefaultInfo() {
        try {
            findPreference("clearCache").summary = CacheUtil.getTotalCacheSize(activity!!)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        key ?: return
        if (key == "color") true
    }

    override fun onResume() {
        super.onResume()
        preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()
        preferenceScreen.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }
}