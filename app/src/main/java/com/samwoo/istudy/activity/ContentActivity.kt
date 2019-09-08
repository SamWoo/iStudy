package com.samwoo.istudy.activity

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Color
import android.util.Log
import android.view.*
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.isNotEmpty
import com.google.android.material.appbar.AppBarLayout
import com.just.agentweb.*
import com.samwoo.istudy.BuildConfig
import com.samwoo.istudy.R
import com.samwoo.istudy.base.BaseActivity
import com.samwoo.istudy.constant.Constant
import com.samwoo.istudy.util.getAgentWeb
import kotlinx.android.synthetic.main.activity_content.*
import kotlinx.android.synthetic.main.content_main.*
import org.jetbrains.anko.browse
import org.jetbrains.anko.share
import org.jetbrains.anko.toast

class ContentActivity : BaseActivity() {
    private lateinit var title: String
    private var id: Int = -1
    private lateinit var url: String
    private lateinit var agentWeb: AgentWeb
    private val mWebView: NestedScrollAgentWebView by lazy { NestedScrollAgentWebView(this) }

//    override fun useEventBus(): Boolean = false

    override fun requestData() {
        initAgentWeb()
    }

    override fun getLayoutResId(): Int {
        return R.layout.activity_content
    }

    override fun initView() {
        toolbar.run {
            title = ""
            setSupportActionBar(this)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        //必须设置为True,否则跑马灯无效
        tb_title.apply {
            isSelected = true
        }
        mWebView.setBackgroundColor(Color.TRANSPARENT)
    }

    override fun initData() {
        intent.extras?.let {
            id = it.getInt(Constant.CONTENT_ID_KEY, -1)
            title = it.getString(Constant.CONTENT_TITLE_KEY, "")
            url = it.getString(Constant.CONTENT_URL_KEY, "")
        }
    }

    private fun initAgentWeb() {
        val params = CoordinatorLayout.LayoutParams(-1, -1)
        params.behavior = AppBarLayout.ScrollingViewBehavior()
        agentWeb = url.getAgentWeb(
            this,
            cl_content,
            params,
            mWebView,
            webChromeClient = mWebChromeClient,
            webViewClient = mWebViewClient
        )
        //设置背景色透明，适配日/夜间模式切换
        agentWeb.webCreator.webParentLayout.setBackgroundColor(Color.TRANSPARENT)
    }

    private val mWebChromeClient = object : WebChromeClient() {
        override fun onReceivedTitle(view: WebView?, title: String?) {
            super.onReceivedTitle(view, title)
//            toolbar.title = title
            tb_title.text = title
        }
    }

    private val mWebViewClient = object : WebViewClient() {
        override fun shouldInterceptRequest(
            view: WebView?,
            request: WebResourceRequest?
        ): WebResourceResponse? {
            return super.shouldInterceptRequest(view, request)
        }

        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
            // TODO something
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_content, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_share -> {
//                toast("Share it!!")
                share(
                    getString(R.string.share_text, getString(R.string.app_name), title, url),
                    title
                )
//                Intent().run {
//                    this.action = Intent.ACTION_SEND
//                    putExtra(
//                        Intent.EXTRA_TEXT,
//                        getString(R.string.share_text, getString(R.string.app_name), title, url)
//                    )
//                    type = Constant.CONTENT_SHARE_TYPE
//                    startActivity(Intent.createChooser(this, getString(R.string.action_share)))
//                }
                true
            }
            R.id.action_like -> {
                toast("Like it!!")
                true
            }
            R.id.action_browser -> {
//                toast("Browser it!!")
                browse(url)
//                Intent().run {
//                    action = "android.intent.action.VIEW"
//                    data = Uri.parse(url)
//                    startActivity(this)
//                }
                true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    /**
     * 默认情况下如果隐藏了Menu item则其下面的icon是不显示的，可以通过反射显示menu下面的icon图标
     */
    override fun onMenuOpened(featureId: Int, menu: Menu?): Boolean {
        if (featureId == Window.FEATURE_ACTION_BAR && menu != null) {
            if (menu::class.java.simpleName == "MenuBuilder") {
                try {
                    val method =
                        menu::class.java.getDeclaredMethod(
                            "setOptionalIconsVisible",
                            java.lang.Boolean.TYPE
                        )
                    method.run {
                        isAccessible = true
                        invoke(menu, true)
                    }
                } catch (e: Exception) {
                    if (BuildConfig.DEBUG) Log.d("Sam", "onMenuOpened--->$e")
                }
            }
        }
        return super.onMenuOpened(featureId, menu)
    }

    /**
     * 如果你是用的是AppCompactActivity，可以使用下面的方法
     */
    @SuppressLint("RestrictedApi")
    override fun onPrepareOptionsPanel(view: View?, menu: Menu?): Boolean {
        if (menu!!.isNotEmpty()) {
            if (menu.javaClass.simpleName == "MenuBuilder") {
                try {
                    val method =
                        menu.javaClass.getDeclaredMethod(
                            "setOptionalIconsVisible",
                            java.lang.Boolean.TYPE
                        )
                    method.run {
                        isAccessible = true
                        invoke(menu, true)
                    }
                } catch (e: Exception) {
                    if (BuildConfig.DEBUG) Log.d("Sam", "onPrepareOptionsPanel--->$e")
                }
            }
        }
        return super.onPrepareOptionsPanel(view, menu)
    }

    override fun onPause() {
        agentWeb.webLifeCycle.onPause()
        super.onPause()
    }

    override fun onResume() {
        agentWeb.webLifeCycle.onResume()
        super.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        AgentWebConfig.clearDiskCache(this.applicationContext) //清空缓存
        agentWeb.webLifeCycle.onDestroy()
    }

    //回退
    override fun onBackPressed() {
        agentWeb?.let {
            if (!it.back()) super.onBackPressed()
        }
    }

    //按键处理
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return if (agentWeb?.handleKeyEvent(keyCode, event)) true else super.onKeyDown(
            keyCode,
            event
        )
    }
}