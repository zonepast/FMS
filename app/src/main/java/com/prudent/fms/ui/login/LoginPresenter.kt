package com.prudent.fms.ui.login

import android.widget.Toast
import com.prudent.fms.core.CoreApp
import com.prudent.fms.data.api.model.login.Request.LoginRequest
import com.prudent.fms.data.api.model.login.Response.LoginResponse
import com.prudent.fms.ui.base.Presenter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.SocketTimeoutException

/**
 * Created by Dharmik Patel on 20-Jul-17.
 */
class LoginPresenter : Presenter<LoginView> {

    var loginView : LoginView? = null

    override fun onAttach(view: LoginView) {
        loginView = view
    }

    override fun onDetach() {
        loginView = null
    }

    fun Login(username : String,password : String, ref : Int){
        if (ref==1) {
            loginView?.progressbarShow()
        }
        if (ref==2) {
            loginView?.progressShow()
        }

        val login = CoreApp.instance?.getNetworkManager()?.service?.Login(LoginRequest("Login",
                "btngetcorporate","","","",username,password))
        login?.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>?, response: Response<LoginResponse>?) {
                if (ref==1) {
                    loginView?.progressbarHide()
                }
                if (ref==2) {
                    loginView?.progressHide()
                }
                if (response!!.isSuccessful){
                    val list = response.body()
                    loginView?.ShowLoginResponse(list)
                }else{
                    loginView?.Error("Error", "Something went wrong",ref)
                }
            }

            override fun onFailure(call: Call<LoginResponse>?, t: Throwable?) {
                if (ref==1) {
                    loginView?.progressbarHide()
                }
                if (ref==2) {
                    loginView?.progressHide()
                }
                if (t is java.net.ConnectException) {
                    loginView?.Error("Internet Error", "Please, Check your Internet Connection!",ref)
                } else if (t is SocketTimeoutException) {
                    loginView?.Error("Timeout Error", "Please, Try again!", ref)
                } else {
                    loginView?.Error("Internet Error", "Please, Check your Internet Connection!",ref)
                }
            }

        })
    }

}
