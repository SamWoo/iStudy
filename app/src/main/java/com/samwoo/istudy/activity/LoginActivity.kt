package com.samwoo.istudy.activity

import android.animation.Animator
import android.annotation.SuppressLint
import android.os.Handler
import android.view.View
import com.samwoo.istudy.App
import com.samwoo.istudy.R
import com.samwoo.istudy.base.BaseActivity
import com.samwoo.istudy.bean.LoginData
import com.samwoo.istudy.constant.Constant
import com.samwoo.istudy.event.LoginEvent
import com.samwoo.istudy.mvp.contract.LoginContract
import com.samwoo.istudy.mvp.presenter.LoginPresenter
import com.samwoo.istudy.util.NetworkUtil
import com.samwoo.istudy.util.Preference
import kotlinx.android.synthetic.main.activity_login.*
import org.greenrobot.eventbus.EventBus
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.toast

class LoginActivity : BaseActivity(), LoginContract.View {

    private var username: String by Preference(Constant.USERNAME_KEY, "")
    private var password: String by Preference(Constant.PASSWORD_KEY, "")
    private var isRemember: Boolean by Preference(Constant.REMEMBER_PASSWORD_KEY, true)

    private var mPresenter: LoginPresenter? = null

    private val handler by lazy { Handler() }

    private lateinit var animator: Animator

    override fun useEventBus(): Boolean = true

    override fun requestData() {}

    override fun getLayoutResId(): Int {
        return R.layout.activity_login
    }

    override fun initView() {
        mPresenter = LoginPresenter()
        mPresenter?.attachView(this)

//        cl_login.background.alpha = 0

        when (isRemember) {
            true -> {
                et_username.setText(username)
                et_password.setText(password)
            }
            else -> {
                et_username.setText("")
                et_password.setText("")
            }
        }
        cb_remember_password.isChecked = isRemember
        btn_login.setOnClickListener(onClickListener)
        btn_to_register.setOnClickListener(onClickListener)
        //remeber password
        cb_remember_password.setOnClickListener(onClickListener)
    }

    private val onClickListener = View.OnClickListener { view ->
        when (view.id) {
            R.id.btn_login -> {
                if (!NetworkUtil.isNetworkAvailable(App.context)) {
                    toast("网络未连接!!")
                    return@OnClickListener
                }
                btn_login.startAnim()
                handler.postDelayed({
                    login()
                }, 3000)
            }
            R.id.btn_to_register -> {
                val intent = intentFor<RegisterActivity>()
                startActivity(intent)
                finish()
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
            }
            R.id.cb_remember_password -> isRemember = !isRemember
        }
    }

    //login
    private fun login() {
        if (validate()) {
            mPresenter?.login(et_username.text.toString(), et_password.text.toString())
        } else {
            btn_login.reset("登 录")
        }
    }

    //校验
    private fun validate(): Boolean {
        var valid = true
        val username: String = et_username.text.toString()
        val password: String = et_password.text.toString()

        if (username.isEmpty()) {
            valid = false
            et_username.error = getString(R.string.username_not_empty)
        }

        if (password.isEmpty()) {
            valid = false
            et_password.error = getString(R.string.password_not_empty)
        }

        return valid
    }

    override fun initData() {

    }

    @SuppressLint("NewApi")
    override fun loginSuccess(data: LoginData) {
        btn_login.stopAnim()
        username = et_username.text.toString() //data.username
        password = et_password.text.toString() //data.password
        isLogin = true
        //notify update ui
        EventBus.getDefault().post(LoginEvent(isLogin))
        finish()
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
    }

    override fun showLoading() {
    }

    override fun hideLoading() {
    }

    override fun showError(errorMsg: String) {
        toast(errorMsg)
        btn_login.reset("登 录")
    }

    override fun onStop() {
        super.onStop()
//        animator.cancel()
        btn_login.stopAnim()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mPresenter != null) {
            mPresenter?.detachView()
            mPresenter = null
        }
    }
}