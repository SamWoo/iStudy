package com.samwoo.istudy.activity

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.*
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isNotEmpty
import com.google.android.material.appbar.AppBarLayout
import com.just.agentweb.*
import com.samwoo.istudy.BuildConfig
import com.samwoo.istudy.R
import com.samwoo.istudy.base.BaseActivity
import com.samwoo.istudy.constant.Constant
import com.samwoo.istudy.util.SLog
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

    override fun getLayoutResId(): Int = R.layout.activity_content


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
        mWebView.setBackgroundColor(Color.parseColor("#00000000"))
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

        override fun onLoadResource(view: WebView?, url: String?) {
            super.onLoadResource(view, url)
            injectJS()
        }

        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
            // TODO something
        }
    }

    //inject JsScript
    private fun injectJS() {
        //修改body背景色为透明
        val bgcolor =
            "javascript:(function(){var obj=document.getElementsByTagName('body');obj[obj.length-1].style.cssText = \"background-color:#ffffff;\";})()"
        //简书header
        val jsheader =
            "javascript:(function(){var obj = document.getElementsByClassName('header-wrap');obj[obj.length-1].remove();})()"
        //简书reward-panel
        val jsreward =
            "javascript:(function(){var obj=document.getElementById('free-reward-panel').remove();})()"
        //简书广告
        val jsad =
            "javascript:(function(){var obj=document.getElementsByClassName('note-comment-above-ad-wrap');obj[obj.length-1].remove();})()"
        //简书推荐
        val jsrecomm =
            "javascript:(function(){var obj=document.getElementById('recommended-notes').remove();})()"
        //简书footer
        val jsfooter =
            "javascript:(function(){var obj=document.getElementById('footer').remove();})()"
        //简书浮标
        val jsfubiao =
            "javascript:(function(){var obj=document.getElementsByClassName('fubiao-dialog');obj[obj.length-1].remove();})()"
        //简书评论
        val jscomment =
            "javascript:(function(){var obj=document.getElementById('comment-main').remove();})()"
        //简书点赞
        val jsgrace =
            "javascript:(function(){var obj=document.getElementsByClassName('note-graceful-button');obj[obj.length-1].remove();})()"
        //简书openbtn
        val jsopen =
            "javascript:(function(){var obj=document.getElementsByClassName('open-app-btn');obj[obj.length-1].remove();})()"

        //掘金适配
        val jjheader =
            "javascript:(function(){var obj=document.getElementsByClassName('main-header-box');obj[obj.length-1].remove();})()"
        //bannner
        val jjbanner =
            "javascript:(function(){var obj=document.getElementsByClassName('article-banner');obj[obj.length-1].remove();})()"
        //footer-author
        val jjauthor =
            "javascript:(function(){var obj=document.getElementsByClassName('footer-author-block');obj[obj.length-1].remove();})()"
        //tag-list
        val jjtag =
            "javascript:(function(){var obj=document.getElementsByClassName('tag-list-box');obj[obj.length-1].remove();})()"
        //comment-box
        val jjcomment =
            "javascript:(function(){document.getElementById('comment-box').remove();})()"
        //books
        val jjbooks =
            "javascript:(function(){var obj=document.getElementsByClassName('books-recommend');obj[obj.length-1].remove();})()"
        //button
        val jjbtn =
            "javascript:(function(){var obj=document.getElementsByClassName('open-in-app');obj[obj.length-1].remove();})()"
        //action-bar
        val jjaction =
            "javascript:(function(){var obj=document.getElementsByClassName('action-bar');obj[obj.length-1].remove();})()"

        //csdn适配
        val csheader =
            "javascript:(function(){$('#csdn-toolbar').remove();})()"
        //mask
        val csmask =
            "javascript:(function(){var obj=$('.mask-lock-box');obj[obj.length-1].remove();})()"
        //comment
        val cscomment = "javascript:(function(){$('#comment').remove();})()"
        //operate
        val csoperate = "javascript:(function(){$('#operate').remove();})()"
        //remove ad
        val csad =
            "javascript:(function(){var obj=$('.overhidden');for(var i=0;i<obj.length;i++){obj[i].remove();}})()"


        mWebView.loadUrl(bgcolor)
        //简书适配
        mWebView.loadUrl(jsheader)
        mWebView.loadUrl(jsreward)
        mWebView.loadUrl(jsad)
        mWebView.loadUrl(jsrecomm)
        mWebView.loadUrl(jsfooter)
        mWebView.loadUrl(jsfubiao)
        mWebView.loadUrl(jscomment)
        mWebView.loadUrl(jsgrace)
        mWebView.loadUrl(jsopen)
        //掘金适配
        mWebView.loadUrl(jjheader)
        mWebView.loadUrl(jjbanner)
        mWebView.loadUrl(jjauthor)
        mWebView.loadUrl(jjtag)
        mWebView.loadUrl(jjbooks)
        mWebView.loadUrl(jjcomment)
        mWebView.loadUrl(jjbtn)
        mWebView.loadUrl(jjaction)
        //csdn适配
        mWebView.loadUrl(csheader)
        mWebView.loadUrl(csmask)
        mWebView.loadUrl(cscomment)
        mWebView.loadUrl(csoperate)
        mWebView.loadUrl(csad)
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
                    SLog.d("Sam", "onPrepareOptionsPanel--->$e")
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
        return if (agentWeb?.handleKeyEvent(keyCode, event)) {
            true
        } else {
            super.onKeyDown(keyCode, event)
        }
    }
}