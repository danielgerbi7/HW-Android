package com.example.hw1_daniel_gerbi.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hw1_daniel_gerbi.R
import com.example.hw1_daniel_gerbi.adapters.HighScoreAdapter
import com.example.hw1_daniel_gerbi.interfaces.CallbackHighScoreItemClicked
import com.example.hw1_daniel_gerbi.logic.ScoreManager
import com.example.hw1_daniel_gerbi.model.Score

import com.google.android.material.textview.MaterialTextView

class HighScoreFragment : Fragment() {

    lateinit var highScore_LBL_title: MaterialTextView
    private lateinit var highScore_RV_records: RecyclerView
    private var score: MutableList<Score> = mutableListOf()
    private val highScoreAdapter = HighScoreAdapter(score)

    var highScoreItemClicked: CallbackHighScoreItemClicked? = null
        set(value) {
            field = value
            highScoreAdapter.callback = value
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_high_score, container, false)
        findViews(v)
        initViews(context)
        return v
    }


    private fun findViews(v: View) {
        highScore_LBL_title = v.findViewById(R.id.highScore_LBL_title)
        highScore_RV_records = v.findViewById(R.id.highScore_RV_records)
    }

    private fun initViews(context: Context?) {
        if (context == null) {
            return
        }
        highScore_RV_records.adapter = highScoreAdapter
        highScoreAdapter.callback = object : CallbackHighScoreItemClicked {
            override fun highScoreItemClicked(lat: Double, lon: Double) {
                highScoreItemClicked?.highScoreItemClicked(lat, lon)
            }
        }
    highScore_RV_records.layoutManager = LinearLayoutManager(context)
    }

    fun updateHighScore() {
        val newScores = ScoreManager.getInstance(requireContext()).scores
        highScoreAdapter.updateScores(newScores)
    }
}