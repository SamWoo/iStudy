package com.samwoo.istudy.activity

import android.view.View
import com.samwoo.istudy.R
import com.samwoo.istudy.base.BaseActivity
import com.samwoo.istudy.bean.LoginData
import com.samwoo.istudy.constant.Constant
import com.samwoo.istudy.event.LoginEvent
import com.samwoo.istudy.mvp.contract.LoginContract
import com.samwoo.istudy.mvp.presenter.LoginPresenter
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

    override fun useEventBus(): Boolean = false

    override fun getLayoutResId(): Int {
        return R.layout.activity_login
    }

    override fun initView() {
        mPresenter = LoginPresenter()
        mPresenter?.attachView(this)

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
            R.id.btn_login -> login()
            R.id.btn_to_register -> {
                val intent = intentFor<RegisterActivity>()
                startActivity(intent)
                finish()
//                overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
            }
            R.id.cb_remember_password -> isRemember = !isRemember
        }
    }

    //login
    private fun login() {
        if (validate()) {
            mPresenter?.login(et_username.text.toString(), et_password.text.toString())
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

    override fun loginSuccess(data: LoginData) {
        toast("登录成功")
        username = data.username
        password = data.password
        isLogin = true

        EventBus.getDefault().post(LoginEvent(isLogin))
        finish()
    }

    override fun showLoading() {
    }

    override fun hideLoading() {
    }

    override fun showError(errorMsg: String) {
        toast(errorMsg)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mPresenter != null) {
            mPresenter?.detachView()
            mPresenter = null
        }
    }
}