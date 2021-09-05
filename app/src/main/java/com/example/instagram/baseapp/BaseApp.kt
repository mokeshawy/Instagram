package com.example.instagram.baseapp

import android.app.Application
import android.content.Context
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class BaseApp : Application(){

    override fun onCreate() {
        super.onCreate()

        // global context.
        appContext = applicationContext
    }

    // global context.
    companion object{
        lateinit var appContext: Context
    }
}