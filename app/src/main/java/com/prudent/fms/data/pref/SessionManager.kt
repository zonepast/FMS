package com.prudent.fms.data.pref

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import com.prudent.fms.kotlin_extensions.flags
import com.prudent.fms.kotlin_extensions.intent
import com.prudent.fms.ui.dashboard.DashboardActivity
import com.prudent.fms.ui.login.LoginActivity
import com.prudent.fms.utils.Config
import java.util.HashMap

/**
 * Created by Dharmik Patel on 21-Jul-17.
 */

class SessionManager(private val mContext: Context) {

    private val pref: SharedPreferences
    private val editor: SharedPreferences.Editor

    init {
        val PRIVATE_MODE = 0
        pref = mContext.getSharedPreferences(Config().PREF_NAME, PRIVATE_MODE)
        editor = pref.edit()
    }

    fun SaveUserNamePassword(username: String, password: String) {
        editor.putString(Config().KEY_USER_NAME, username)
        editor.putString(Config().KEY_PASSWORD, password)
        editor.putBoolean(Config().IS_LOGIN,true)
        editor.commit()
        val flags = flags(Intent.FLAG_ACTIVITY_CLEAR_TOP, Intent.FLAG_ACTIVITY_NEW_TASK)
        mContext.startActivity(mContext.intent<DashboardActivity>(flags))
    }

    fun SaveBranch(BranchXname: String, BranchXcode: String) {
        editor.putString(Config().KEY_BRANCH_NAME, BranchXname)
        editor.putString(Config().KEY_BRANCH_CODE, BranchXcode)
        editor.commit()
    }

    fun checkLogin() {
        if (!this.isLoggedIn) {
            val flags = flags(Intent.FLAG_ACTIVITY_CLEAR_TOP, Intent.FLAG_ACTIVITY_NEW_TASK)
            //mContext.startActivity(mContext.intent<DashboardActivity>(flags))
        }

    }

    val username: String
        get() = pref.getString(Config().KEY_USER_NAME, null)

    val branchcode: String
        get() = pref.getString(Config().KEY_BRANCH_CODE, null)

    val password: String
        get() = pref.getString(Config().KEY_PASSWORD, null)

    fun logoutUser() {
        editor.clear()
        editor.commit()
        val flags = flags(Intent.FLAG_ACTIVITY_CLEAR_TOP, Intent.FLAG_ACTIVITY_NEW_TASK)
        mContext.startActivity(mContext.intent<LoginActivity>(flags))
    }

    val isLoggedIn: Boolean
        get() = pref.getBoolean(Config().IS_LOGIN, false)
}
