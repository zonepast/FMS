package com.prudent.fms.ui.dashboard

import com.prudent.fms.data.api.model.Logout.Response.LogoutResponse
import com.prudent.fms.data.api.model.dashboard.Response.DashboardDataResponse
import com.prudent.fms.ui.base.View

/**
 * Created by Dharmik Patel on 21-Jul-17.
 */
interface DashboardView : View {

    fun progressShow()

    fun progressHide()

    fun Error(title: String, message: String)

    fun ShowDashboardResponse(response: DashboardDataResponse)

    fun ShowLogoutResponse(response: LogoutResponse)
}