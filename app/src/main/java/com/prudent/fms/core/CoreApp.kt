package com.prudent.fms.core

import android.app.Application
import com.prudent.fms.data.api.apihelper.ApiConfig
import com.prudent.fms.data.pref.SessionManager
import io.realm.Realm
import io.realm.RealmConfiguration

/**
 * Created by Dharmik Patel on 20-Jul-17.
 */
class CoreApp : Application() {
    companion object {
        var instance: CoreApp? = null
    }

    private var networkManager: ApiConfig? = null
    private var sessionManager: SessionManager? = null

    override fun onCreate() {
        super.onCreate()
        instance = this
        Realm.init(this)
        val config = RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .build()
        Realm.setDefaultConfiguration(config)
    }

    fun getNetworkManager(): ApiConfig {
        if (networkManager == null) {
            networkManager = ApiConfig()
        }
        return networkManager as ApiConfig
    }

    fun getSessionManager(): SessionManager {
        if (sessionManager == null) {
            sessionManager = SessionManager(this)
        }
        return sessionManager as SessionManager
    }
}