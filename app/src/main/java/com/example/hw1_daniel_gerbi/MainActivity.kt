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

class MainActivity : AppCompatActivity() {

    private lateinit var main_IMG_hearts: Array<AppCompatImageView>
    private lateinit var main_FAB_right: ExtendedFloatingActionButton
    private lateinit var main_FAB_left: ExtendedFloatingActionButton
    private lateinit var main_IMG_players: Array<AppCompatImageView>
    private lateinit var main_IMG_cakes: Array<AppCompatImageView>
    private lateinit var gameManager: GameManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViews()
        gameManager = GameManager(main_IMG_hearts.size)
        initViews()

        val handler = Handler(Looper.getMainLooper())
        val runnable = object : Runnable {
            override fun run() {
                refreshUI()
                handler.postDelayed(this, 500)
            }
        }
        handler.post(runnable)

    }
    private fun findViews() {

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
        main_IMG_cakes = Array(15) { index ->
            val resourceId = resources.getIdentifier("main_IMG_cake${index + 1}", "id", packageName)
            findViewById(resourceId)
        }
    }
    private fun initViews() {
        main_FAB_right.setOnClickListener {
            movePlayerRight()
        }

        main_FAB_left.setOnClickListener {
            movePlayerLeft()
        }
        refreshUI()
    }
    private fun refreshUI() {
        if(gameManager.isGameOver) {
            showGameOverMessage()
            finish()
        } else {
            gameManager.moveCakesDown()
            gameManager.showRandomCake()

            updateHearts()
            updatePlayers()
            updateCakes()
            checkForCollision()
        }
    }

    private fun movePlayerLeft() {
        if(gameManager.canMovePlayerLeft()) {
            gameManager.movePlayerLeft()
            refreshUI()
        }
    }

    private fun movePlayerRight() {
        if(gameManager.canMovePlayerRight()) {
            gameManager.movePlayerRight()
            refreshUI()
        }
    }


    private fun showGameOverMessage() {
        Toast.makeText(this, "😭 Game Over!", Toast.LENGTH_SHORT).show()
    }

    private fun updatePlayers() {
        for (i in main_IMG_players.indices) {
            if (gameManager.isPlayerVisible(i)) {
                main_IMG_players[i].visibility = View.VISIBLE
            } else {
                main_IMG_players[i].visibility = View.INVISIBLE
            }
        }
    }

    private fun updateCakes() {
        for (i in main_IMG_cakes.indices) {
            if (gameManager.isCakeVisible(i)) {
                main_IMG_cakes[i].visibility = View.VISIBLE
                val position = gameManager.cakePositions[i]
                when (position) {
                    0 -> main_IMG_cakes[i].x = 0f
                    1 -> main_IMG_cakes[i].x = 500f
                    2 -> main_IMG_cakes[i].x = 1000f
                }
            } else {
                main_IMG_cakes[i].visibility = View.INVISIBLE
            }
        }
    }

    private fun updateHearts() {
        if (gameManager.hitPosition != 0) {
            main_IMG_hearts[main_IMG_hearts.size - gameManager.hitPosition].visibility =
                View.INVISIBLE
        }
    }

    private fun checkForCollision() {
        if (gameManager.hasCollision()) {
            gameManager.checkPosition(gameManager.playerPosition)
            refreshUI()
        }
    }
}
