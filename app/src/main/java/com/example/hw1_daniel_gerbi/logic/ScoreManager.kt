package com.example.hw1_daniel_gerbi.logic

import android.content.Context
import android.util.Log
import com.example.hw1_daniel_gerbi.model.Score
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.reflect.TypeToken
import com.google.gson.Gson

class ScoreManager private constructor(context: Context) {

    private val sharedPreferences = context.getSharedPreferences("HighScores", Context.MODE_PRIVATE)
    private val gson = Gson()
    private var scoresList: MutableList<Score> = loadScores()

    val scores: List<Score>
        get() {
            scoresList = loadScores()
            return scoresList
        }

    private fun sortScores() {
        scoresList.sortByDescending { it.scoreValue }
    }

    fun updateScore(newScore: Score) {
        scoresList.add(newScore)
        sortScores()
        if (scoresList.size > 10) {
            scoresList = scoresList.take(10).toMutableList()
        }
        trimScores()
        saveScores()
    }

    private fun trimScores() {
        if (scoresList.size > 10) {
            scoresList = scoresList.take(10).toMutableList()
        }

    }

    private fun saveScores() {
        val editor = sharedPreferences.edit()
        Log.d("ScoreManager", "Saving scores JSON: $editor")
        val json = gson.toJson(scoresList)
        editor.putString("scores", json)
        editor.apply()
    }

    fun loadScores(): MutableList<Score> {
        val json = sharedPreferences.getString("scores", null)
        Log.d("ScoreManager", "Loaded scores JSON: $json")
        val type = object : TypeToken <MutableList<Score>>() {}.type
        return if (json != null) {
            gson.fromJson(json, type)
        } else {
            mutableListOf()
        }
    }


    companion object {
        private var instance: ScoreManager? = null
        fun getInstance(context: Context): ScoreManager {
            if (instance == null) {
                instance = ScoreManager(context)
            }
            return instance!!
        }
    }
}
