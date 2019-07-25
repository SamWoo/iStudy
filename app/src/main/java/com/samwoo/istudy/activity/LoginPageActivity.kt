package com.samwoo.istudy.activity

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.samwoo.istudy.R
import com.samwoo.istudy.util.ViewUtil
import kotlinx.android.synthetic.main.activity_login_page.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.toast
import org.jetbrains.anko.startActivityForResult

class LoginPageActivity : AppCompatActivity() {
    private val mRequestCode = 0
    private var isRemeber = false
    private var mPassword = "000000"
    private var mPhone = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_page)
        rg_login.setOnCheckedChangeListener { group, checkedId -> resetHint(checkedId) }
        et_phone.addTextChangedListener(HideTextWatcher(et_phone))
        et_password.addTextChangedListener(HideTextWatcher(et_password))
        ck_remember.setOnCheckedChangeListener { buttonView, isChecked -> isRemeber = isChecked }
        btn_forget.setOnClickListener { doForget() }
        btn_login.setOnClickListener { doLogin() }
    }

    private fun resetHint(checkId: Int) {
        when (checkId) {
            R.id.rb_password -> {
                tv_password.text = "登录密码："
                et_password.hint = "请输入密码"
                btn_forget.text = "忘记密码"
                ck_remember.visibility = View.VISIBLE
            }
            R.id.rb_verifycode -> {
                tv_password.text = "　验证码："
                et_password.hint = "请输入验证码"
                btn_forget.text = "获取验证码"
                ck_remember.visibility = View.INVISIBLE
            }
            else -> {
                //TODO
            }
        }
    }

    private inner class HideTextWatcher(private val et: EditText) : TextWatcher {
        val maxLength = ViewUtil.getMaxLength(et)
        private var mStr: CharSequence? = null

        override fun afterTextChanged(s: Editable?) {
            if (mStr.isNullOrEmpty()) return
            if (mStr!!.length == 11 && maxLength == 11 || mStr!!.length == 6 && maxLength == 6) {
                //隐藏输入法面板
                ViewUtil.hideOneInputMethod(this@LoginPageActivity, et)
            }
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            mStr = s
        }

    }

    private fun doForget() {
        val phone = et_phone.text.toString()
        mPhone = phone

        if (phone.isBlank() || phone.length < 11) {
            toast("请输入正确的手机号")
            return
        }
        if (rb_password.isChecked) {
            val intent = intentFor<LoginForgetActivity>(Pair("phone", phone))
            startActivityForResult(intent, mRequestCode)
        } else if (rb_verifycode.isChecked) {
            val verifyCode = String.format("%06d", (Math.random() * 1000000 / 1000000).toInt())
            alert("本次手机号${phone}的验证码为${verifyCode}，5分钟内有效，请及时输入验证！", "请记住验证码")
        }
    }

    private fun doLogin() {
        val phone = et_phone.text.toString()
        if (phone.isBlank() || phone.length < 11) {
            toast("请输入正确的手机号！")
            return
        }
        if (rb_password.isChecked) {
            val password = et_password.text.toString()
            if (password.isNullOrEmpty()) {
                toast("密码不正确")
                return
            } else {
                login(phone, password)
            }
        } else if (rb_verifycode.isChecked) {
            val verifyCode=et_password.text.toString()
        }
    }

    private fun login(phone: String, password: String): String? {
        //TODO
        return ""
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == mRequestCode && data != null) {
            mPassword = data.getStringExtra("new_password")
            et_password.setText(mPassword)
            login(mPhone, mPassword)
        }
    }

    override fun onRestart() {
        et_password.setText("")
        super.onRestart()
    }
}