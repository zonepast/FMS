package com.prudent.fms.ui.login

import com.prudent.fms.data.api.model.login.Response.LoginResponse
import com.prudent.fms.ui.base.View

/**
 * Created by Dharmik Patel on 20-Jul-17.
 */
interface LoginView : View {

    fun progressShow()

    fun progressHide()

    fun progressbarShow()

    fun progressbarHide()

    fun Error(title: String, message: String, ref : Int)

    fun ShowLoginResponse(response: LoginResponse)


}