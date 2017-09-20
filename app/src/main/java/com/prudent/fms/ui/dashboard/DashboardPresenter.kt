package com.prudent.fms.ui.dashboard

import android.app.ProgressDialog
import com.prudent.fms.core.CoreApp
import com.prudent.fms.data.api.model.Logout.Request.LogoutRequest
import com.prudent.fms.data.api.model.Logout.Response.LogoutResponse
import com.prudent.fms.data.api.model.dashboard.Request.DashboardDataRequest
import com.prudent.fms.data.api.model.dashboard.Response.DashboardDataResponse
import com.prudent.fms.data.api.model.login.Request.LoginRequest
import com.prudent.fms.data.api.model.login.Response.LoginResponse
import com.prudent.fms.ui.base.Presenter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.SocketTimeoutException

/**
 * Created by Dharmik Patel on 21-Jul-17.
 */
class DashboardPresenter : Presenter<DashboardView> {

    var dashboardView : DashboardView? = null

    override fun onAttach(view: DashboardView) {
        dashboardView = view
    }

    override fun onDetach() {
        dashboardView = null
    }

    fun Dashboard(corpCentre : String,userId : String){
        dashboardView?.progressShow()
        val dashboard = CoreApp.instance?.getNetworkManager()?.service?.GetDashboard(DashboardDataRequest(
                corpCentre,"Base",userId))
        dashboard?.enqueue(object : Callback<DashboardDataResponse> {
            override fun onResponse(call: Call<DashboardDataResponse>?, response: Response<DashboardDataResponse>?) {
                dashboardView?.progressHide()
                if (response!!.isSuccessful){
                    val list = response.body()
                    dashboardView?.ShowDashboardResponse(list)
                }else{
                    dashboardView?.Error("Error", "Something went wrong")
                }
            }

            override fun onFailure(call: Call<DashboardDataResponse>?, t: Throwable?) {
                dashboardView?.progressHide()
                if (t is java.net.ConnectException) {
                    dashboardView?.Error("Internet Error", "Please, Check your Internet Connection!")
                } else if (t is SocketTimeoutException) {
                    dashboardView?.Error("Timeout Error", "Please, Try again!")
                } else {
                    dashboardView?.Error("Internet Error", "Please, Check your Internet Connection!")
                }
            }

        })
    }

    public fun Logout(Control:String, CorpCentre:String?, ip:String, module:String, password:String,
                       userid:String?, username:String) {
        dashboardView?.progressShow()
        val logout = CoreApp.instance?.getNetworkManager()?.service?.Logout(LogoutRequest(Control,
                CorpCentre,ip,module,password,userid,username))
        logout?.enqueue(object : Callback<LogoutResponse> {
            override fun onResponse(call: Call<LogoutResponse>?, response: Response<LogoutResponse>?) {
                dashboardView?.progressHide()
                if (response!!.isSuccessful){
                    val list = response.body()
                    dashboardView?.ShowLogoutResponse(list)
                }else{
                    dashboardView?.Error("Error", "Something went wrong")
                }
            }

            override fun onFailure(call: Call<LogoutResponse>?, t: Throwable?) {
                dashboardView?.progressHide()
                if (t is java.net.ConnectException) {
                    dashboardView?.Error("Internet Error", "Please, Check your Internet Connection!")
                } else if (t is SocketTimeoutException) {
                    dashboardView?.Error("Timeout Error", "Please, Try again!")
                } else {
                    dashboardView?.Error("Internet Error", "Please, Check your Internet Connection!")
                }
            }

        })
    }
}