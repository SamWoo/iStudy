package com.samwoo.istudy.activity

import android.os.Handler
import android.view.View
import com.samwoo.istudy.R
import com.samwoo.istudy.base.BaseActivity
import com.samwoo.istudy.bean.LoginData
import com.samwoo.istudy.constant.Constant
import com.samwoo.istudy.mvp.contract.RegisterContract
import com.samwoo.istudy.mvp.presenter.RegisterPresenter
import com.samwoo.istudy.util.Preference
import kotlinx.android.synthetic.main.activity_register.*
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.toast

class RegisterActivity : BaseActivity(), RegisterContract.View {

    private var mPresenter: RegisterPresenter? = null
    private val handler by lazy { Handler() }

    private var username: String by Preference(Constant.USERNAME_KEY, "")
    private var password: String by Preference(Constant.PASSWORD_KEY, "")

    override fun useEventBus(): Boolean = false
    override fun requestData() {}

    override fun getLayoutResId(): Int {
        return R.layout.activity_register
    }

    override fun initView() {
        mPresenter = RegisterPresenter()
        mPresenter?.attachView(this)

        et_username.setText("")
        et_password.setText("")
        et_repassword.setText("")

        btn_register.setOnClickListener(onClickListener)
        btn_to_login.setOnClickListener(onClickListener)
    }

    private val onClickListener = View.OnClickListener { view ->
        when (view.id) {
            R.id.btn_register -> {
                btn_register.startAnim()
                handler.postDelayed({
                    register()
                }, 3000)
            }
            R.id.btn_to_login -> {
                val intent = intentFor<LoginActivity>()
                startActivity(intent)
                finish()
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
            }
        }
    }

    //register
    private fun register() {
        if (validate()) {
            mPresenter?.register(
                et_username.text.toString(),
                et_password.text.toString(),
                et_repassword.text.toString()
            )
        } else {
            btn_register.reset("注 册")
        }
    }

    //check data
    private fun validate(): Boolean {
        var valid = true
        val username = et_username.text.toString()
        val password = et_password.text.toString()
        val repassword = et_repassword.text.toString()

        if (username.isEmpty()) {
            valid = false
            et_username.error = getString(R.string.username_not_empty)
        }

        if (password.isEmpty()) {
            valid = false
            et_password.error = getString(R.string.password_not_empty)
        }

        if (password != repassword) {
            valid = false
            et_repassword.error = getString(R.string.password_not_equal)
        }

        return valid
    }

    override fun initData() {
    }

    override fun registerSuccess(data: LoginData) {
        username = data.username
        password = data.password

        val intent = intentFor<LoginActivity>()
        startActivity(intent)
        finish()
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
    }

    override fun showLoading() {

    }

    override fun hideLoading() {
    }

    override fun showError(errorMsg: String) {
        toast(errorMsg)
        btn_register.reset("注 册")
    }

    override fun onStop() {
        super.onStop()
        btn_register.stopAnim()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mPresenter != null) {
            mPresenter?.detachView()
            mPresenter = null
        }
    }

}