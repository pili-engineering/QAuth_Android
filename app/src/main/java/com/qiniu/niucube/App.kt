package com.qiniu.niucube

import android.app.Application
import android.util.Log
import com.qiniu.qlogin.QAuth

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        QAuth.init(this, BuildConfig.app_id, BuildConfig.app_key)
        QAuth.setDebug(true)
    }
}