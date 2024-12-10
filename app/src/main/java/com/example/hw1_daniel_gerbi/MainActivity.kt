package com.example.hw1_daniel_gerbi

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import com.example.hw1_daniel_gerbi.logic.GameManager
import com.example.hw1_daniel_gerbi.utilities.Constants
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.textview.MaterialTextView

class MainActivity : AppCompatActivity() {

    private lateinit var main_IMG_hearts: Array<AppCompatImageView>

    private lateinit var main_LBL_score: MaterialTextView

    private lateinit var main_FAB_right: ExtendedFloatingActionButton

    private lateinit var main_FAB_left: ExtendedFloatingActionButton

    private lateinit var main_IMG_players: Array<AppCompatImageView>

    private lateinit var main_IMG_cakes: Array<Array<AppCompatImageView>>

    private lateinit var gameManager: GameManager

    val handler = Handler(Looper.getMainLooper())

    private val runnable = object : Runnable {
        override fun run() {
            handler.postDelayed(this, 1000)
            gameManager.moveCakesDown()
            refreshUI()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViews()
        gameManager = GameManager(main_IMG_hearts.size)
        initViews()
    }

    private fun findViews() {

        main_LBL_score = findViewById(R.id.main_LBL_score)

        main_IMG_hearts = arrayOf(
            findViewById(R.id.main_IMG_heart1),
            findViewById(R.id.main_IMG_heart2),
            findViewById(R.id.main_IMG_heart3)
        )

        main_FAB_right = findViewById(R.id.main_FAB_right)
        main_FAB_left = findViewById(R.id.main_FAB_left)

        main_IMG_players = arrayOf(
            findViewById(R.id.main_IMG_player1),
            findViewById(R.id.main_IMG_player2),
            findViewById(R.id.main_IMG_player3)
        )
        main_IMG_cakes = arrayOf(
            arrayOf(
                findViewById(R.id.main_IMG_cake1),
                findViewById(R.id.main_IMG_cake2),
                findViewById(R.id.main_IMG_cake3)
            ),
            arrayOf(
                findViewById(R.id.main_IMG_cake4),
                findViewById(R.id.main_IMG_cake5),
                findViewById(R.id.main_IMG_cake6)
            ),
            arrayOf(
                findViewById(R.id.main_IMG_cake7),
                findViewById(R.id.main_IMG_cake8),
                findViewById(R.id.main_IMG_cake9)
            ),
            arrayOf(
                findViewById(R.id.main_IMG_cake10),
                findViewById(R.id.main_IMG_cake11),
                findViewById(R.id.main_IMG_cake12)
            ),
            arrayOf(
                findViewById(R.id.main_IMG_cake13),
                findViewById(R.id.main_IMG_cake14),
                findViewById(R.id.main_IMG_cake15)
            )
        )

    }

    private fun initViews() {

        main_LBL_score.text = gameManager.score.toString()

        main_FAB_right.setOnClickListener {
            movePlayerRight()
        }
        main_FAB_left.setOnClickListener {
            movePlayerLeft()
        }
        handler.postDelayed(runnable, 1000)
        refreshUI()
    }

    override fun onStop() {
        super.onStop()
        handler.removeCallbacks(runnable)
    }

    private fun refreshUI() {
        if (gameManager.isGameOver) {
            showGameOverMessage()
            handler.removeCallbacks(runnable)
            changeActivity("Game Over! 😭 ", gameManager.score)
        } else {
            updatePlayers()
            updateCakes()
            if(gameManager.checkCollision()){
                Toast.makeText(this, "You ate the cakes , it's not healthy!", Toast.LENGTH_SHORT).show()
                updateHearts()
            }
            gameManager.updateScore()
            main_LBL_score.text = gameManager.score.toString()
            updateHearts()
        }
    }
    private fun movePlayerLeft() {
        if (gameManager.canMovePlayerLeft()) {
            gameManager.movePlayerLeft()
            refreshUI()
        }
    }

    private fun movePlayerRight() {
        if (gameManager.canMovePlayerRight()) {
            gameManager.movePlayerRight()
            refreshUI()
        }
    }

    private fun showGameOverMessage() {
        Toast.makeText(this, "😭 Game Over!", Toast.LENGTH_SHORT).show()
    }

    private fun updatePlayers() {
        for (i in main_IMG_players.indices) {
            if (i == gameManager.playerPosition) {
                main_IMG_players[i].visibility = View.VISIBLE
            } else {
                main_IMG_players[i].visibility = View.INVISIBLE
            }
        }
    }

    private fun updateCakes() {
        for (row in main_IMG_cakes.indices) {
            for (col in main_IMG_cakes[row].indices) {
                if (gameManager.cakeMatrix[row][col]) {
                    main_IMG_cakes[row][col].visibility = View.VISIBLE
                } else {
                    main_IMG_cakes[row][col].visibility = View.INVISIBLE
                }
            }
        }
    }

    private fun updateHearts() {
        if (gameManager.hitPosition != 0) {
            main_IMG_hearts[main_IMG_hearts.size - gameManager.hitPosition].visibility =
                View.INVISIBLE
            }
        }

    private fun changeActivity(message: String, score: Int) {
        val intent = Intent(this, ScoreActivity::class.java)
        var bundle = Bundle()
        bundle.putInt(Constants.BundleKeys.SCORE_KEY, score)
        bundle.putString(Constants.BundleKeys.STATUS_KEY, message)
        intent.putExtras(bundle)
        startActivity(intent)
        finish()
    }

}

