package com.example.hw1_daniel_gerbi

import android.app.Application
import com.example.hw1_daniel_gerbi.utilities.SignalManager

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        SignalManager.init(this)
    }
}