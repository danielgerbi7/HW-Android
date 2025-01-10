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
import com.example.hw1_daniel_gerbi.utilities.SignalManager
import com.google.android.material.button.MaterialButton

import com.google.android.material.textfield.TextInputEditText

class HighScoreFragment : Fragment() {

    private lateinit var highScore_ET_location: TextInputEditText

    private lateinit var highScore_BTN_send: MaterialButton

    private lateinit var highScore_RV_records: RecyclerView

    var score: MutableList<Score> = mutableListOf()
    private val highScoreAdapter = HighScoreAdapter(score)

    var highScoreItemClicked: CallbackHighScoreItemClicked? = null

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


    private fun initViews(context: Context?) {
        if (context == null) {
            return
        }
        highScore_BTN_send.setOnClickListener { _: View ->
            var coordinates = highScore_ET_location.text?.split(",")
            var lat: Double = coordinates?.get(0)?.toDouble() ?: 0.0
            var lon: Double = coordinates?.get(1)?.toDouble() ?: 0.0
            itemClicked(lat, lon)
        }
        highScore_RV_records.adapter = highScoreAdapter
        highScoreAdapter.callback = highScoreItemClicked
        highScore_RV_records.layoutManager = LinearLayoutManager(context)
    }

    private fun itemClicked(lat: Double, lon: Double) {
        if (lat != 0.0 && lon != 0.0) {
            highScoreItemClicked?.highScoreItemClicked(lat, lon)
        } else {
            SignalManager.getInstance().toast("Invalid coordinates")
        }
    }


    private fun findViews(v: View) {
        highScore_ET_location = v.findViewById(R.id.highScore_ET_location)
        highScore_BTN_send = v.findViewById(R.id.highScore_BTN_send)
        highScore_RV_records = v.findViewById(R.id.highScore_RV_records)
    }


    fun updateHighScore() {
        val newScores = ScoreManager.getInstance(requireContext()).scores
        highScoreAdapter.updateScores(newScores)
    }
}