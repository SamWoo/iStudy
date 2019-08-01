package com.samwoo.istudy.activity

import android.graphics.Bitmap
import android.view.Menu
import android.view.MenuItem
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.widget.LinearLayout
import com.just.agentweb.AgentWeb
import com.just.agentweb.WebChromeClient
import com.just.agentweb.WebViewClient
import com.samwoo.istudy.R
import com.samwoo.istudy.base.BaseActivity
import com.samwoo.istudy.constant.Constant
import com.samwoo.istudy.util.getAgentWeb
import kotlinx.android.synthetic.main.activity_content.*
import kotlinx.android.synthetic.main.content_main.*
import org.jetbrains.anko.toast

class ContentActivity : BaseActivity() {
    private lateinit var title: String
    private var id: Int = -1
    private lateinit var url: String
    private lateinit var agentWeb: AgentWeb

    override fun getLayoutResId(): Int {
        return R.layout.activity_content
    }

    override fun initView() {
        intent.extras?.let {
            id = it.getInt(Constant.CONTENT_ID_KEY, -1)
            title = it.getString(Constant.CONTENT_TITLE_KEY, "")
            url = it.getString(Constant.CONTENT_URL_KEY, "")
        }

        toolbar.run {
            title = ""
            setSupportActionBar(this)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        initAgentWeb()
    }

    override fun initData() {}

    fun initAgentWeb() {
        agentWeb = url.getAgentWeb(
            this, container, LinearLayout.LayoutParams(-1, -1),
            webChromeClient = mWebChromeClient, webViewClient = mWebViewClient
        )
    }

    private val mWebChromeClient = object : WebChromeClient() {
        override fun onReceivedTitle(view: WebView?, title: String?) {
            super.onReceivedTitle(view, title)
            toolbar.title = title
        }
    }

    private val mWebViewClient = object : WebViewClient() {
        override fun shouldInterceptRequest(view: WebView?, request: WebResourceRequest?): WebResourceResponse? {
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
                toast("Share it!!")
                true
            }
            R.id.action_like -> {
                toast("Like it!!")
                true
            }
            R.id.action_browser -> {
                toast("Browser it!!")
                true
            }
        }
        return super.onOptionsItemSelected(item)
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
        agentWeb.webLifeCycle.onDestroy()
    }

}