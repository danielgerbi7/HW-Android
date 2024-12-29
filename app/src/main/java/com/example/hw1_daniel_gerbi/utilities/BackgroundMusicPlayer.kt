package com.example.hw1_daniel_gerbi.utilities

import android.content.Context
import android.media.MediaPlayer
import java.lang.ref.WeakReference

class BackgroundMusicPlayer private constructor(context: Context) {
    private val contextRef = WeakReference(context)
    private var mediaPlayer: MediaPlayer? = null
    private var resId: Int = 0

    fun setResourceId(id: Int) {
        this.resId = id
        initMediaPlayer()
    }

    private fun initMediaPlayer() {
        if (mediaPlayer != null) {
            release()
        }

        mediaPlayer = MediaPlayer.create(contextRef.get(), resId)
        mediaPlayer!!.isLooping = true
        mediaPlayer!!.setVolume(0.4f, 0.4f)
    }

    private fun release() {
        if (mediaPlayer == null){
            return
        }

        try {
            mediaPlayer!!.release()
            mediaPlayer = null
        } catch (ex: IllegalStateException){
            ex.printStackTrace()
        }

    }
    fun playMusic(){
        if (mediaPlayer == null || mediaPlayer!!.isPlaying){
            return
        }

        try {
            mediaPlayer!!.start()
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        }
    }

    fun pauseMusic(){
        if (mediaPlayer == null || !mediaPlayer!!.isPlaying){
            return
        }

        try {
            mediaPlayer!!.pause()
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        }
    }

    fun stopMusic(){
        if (mediaPlayer == null || !mediaPlayer!!.isPlaying){
            return
        }

        try {
            mediaPlayer!!.stop()
            release()
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        }
    }

    companion object{
        @Volatile
        private var instance: BackgroundMusicPlayer? = null

        fun init(context: Context): BackgroundMusicPlayer{
            return instance ?: synchronized(this){
                instance ?: BackgroundMusicPlayer(context).also { instance = it }
            }
        }

        fun getInstance(): BackgroundMusicPlayer{
            return instance ?:
            throw IllegalStateException("BackgroundMusicPlayer must be initialized by calling init (before use)")
        }
    }
}