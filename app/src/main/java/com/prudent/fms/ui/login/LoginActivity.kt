package com.prudent.fms.ui.login

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.support.annotation.RequiresApi
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.WindowManager
import com.prudent.fms.R
import com.prudent.fms.core.CoreApp
import com.prudent.fms.data.api.model.login.Response.LoginResponse
import com.prudent.fms.data.db.model.dbLogin
import com.prudent.fms.extensions.DeletedbLogin
import com.prudent.fms.extensions.GetRealmCount
import com.prudent.fms.extensions.SaveDBNavigation
import com.prudent.fms.extensions.realm.save
import com.prudent.fms.kotlin_extensions.*
import com.prudent.fms.ui.dashboard.DashboardActivity
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity(), LoginView {

    lateinit var loginPresenter: LoginPresenter
    var mProgressDialog: ProgressDialog? = null
    var doubleBackToExitPressedOnce: Boolean? = false

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        initPresenter()
        onAttach()

        setProgress()

        if (lollipopOrNewer()) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = Color.parseColor("#1397DA")
        }

        checkLogin()
    }

    private fun checkLogin() {
        if (CoreApp.instance?.getSessionManager()?.isLoggedIn!!) {
            // loginPresenter.Login(CoreApp.instance?.getSessionManager()?.username!!, CoreApp.instance?.getSessionManager()?.password!!,1)
            start<DashboardActivity>()
        }
    }

    private fun validation() {
        var failFlag = false
        if (edt_username_login.text.toString().trim({ it <= ' ' }).isEmpty()) {
            failFlag = true
            txt_lyt_username_login.error = "Username Cannot be Empty"
        }
        if (edt_password_login.text.toString().trim({ it <= ' ' }).isEmpty()) {
            failFlag = true
            txt_lyt_password_login.error = "Password Cannot be Empty"
        }
        if (!failFlag) {
            val email = edt_username_login.text.toString()
            val password = edt_password_login.text.toString()
            loginPresenter.Login(email, password, 2)
        }
    }

    private fun setButtonListener() {
        btn_login.setOnClickListener {
            validation()
        }
    }

    private fun initPresenter() {
        loginPresenter = LoginPresenter()
        setButtonListener()
    }

    private fun setProgress() {
        mProgressDialog = progressDialog("Please Wait", "Loading")
    }

    override fun onDestroy() {
        onDetach()
        super.onDestroy()
    }

    override fun onAttach() {
        loginPresenter.onAttach(this)
    }

    override fun onDetach() {
        loginPresenter.onDetach()
    }

    override fun progressShow() {
        mProgressDialog?.show()
    }

    override fun progressHide() {
        mProgressDialog?.dismiss()
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce!!) {
            val intent = Intent(Intent.ACTION_MAIN)
            intent.addCategory(Intent.CATEGORY_HOME)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP//***Change Here***
            startActivity(intent)
            finish()
            System.exit(0)
            return
        }
        this.doubleBackToExitPressedOnce = true
        toast("Please click BACK again to exit")
        Handler().postDelayed({ doubleBackToExitPressedOnce = false }, 2000)
    }

    override fun Error(title: String, message: String, ref: Int) {
        val dialog = alert(message, title)
        dialog.negativeButton("OK") {

        }
        dialog.positiveButton("RETRY") {
            validation()
        }
        dialog.show()
    }

    override fun progressbarShow() {
        btn_login.visibility = View.GONE;
        txt_lyt_username_login.visibility = View.INVISIBLE;
        txt_lyt_password_login.visibility = View.INVISIBLE;
        progressBar_splash.visibility = View.VISIBLE;
    }

    override fun progressbarHide() {
        progressBar_splash.visibility = View.INVISIBLE;
    }

    override fun ShowLoginResponse(response: LoginResponse) {

        if (response.table!![0].success!! == 1) {
            toast(response.table!![0].message!!)

            val loginResponse = response.table1!!

            if (GetRealmCount()!! == 0) {
                DeletedbLogin()
            }

            for (i in 0..loginResponse.size - 1) {
                dbLogin(loginResponse[i].xmaster!!, loginResponse[i].xcode!!, loginResponse[i].xname!!).save()
            }

            val username = edt_username_login.text.toString()
            val password = edt_password_login.text.toString()

            SaveDBNavigation()

            CoreApp.instance?.getSessionManager()?.SaveUserNamePassword(username, password)

        } else {
            toast(response.table!![0].message!!)
        }
    }

}
