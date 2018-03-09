package com.crioprecipitati.androidpervasive1718

import android.app.Application
import com.chibatching.kotpref.Kotpref
import com.crioprecipitati.androidpervasive1718.networking.webSockets.SessionWSAdapter

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Kotpref.init(applicationContext)
    }

}