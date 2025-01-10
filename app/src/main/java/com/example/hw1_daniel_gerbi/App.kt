package com.example.hw1_daniel_gerbi

import android.app.Application
import com.example.hw1_daniel_gerbi.logic.ScoreManager
import com.example.hw1_daniel_gerbi.utilities.BackgroundMusicPlayer
import com.example.hw1_daniel_gerbi.utilities.SignalManager

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        SignalManager.init(this)
        BackgroundMusicPlayer.init(this)
        BackgroundMusicPlayer.getInstance().setResourceId(R.raw.background_song)
        ScoreManager.getInstance(this)
        ScoreManager.getInstance(this).loadScores()
    }

    override fun onTerminate() {
        super.onTerminate()
        BackgroundMusicPlayer.getInstance().stopMusic()
    }

}