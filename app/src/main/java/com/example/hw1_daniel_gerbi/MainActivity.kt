package com.example.hw1_daniel_gerbi

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton

class MainActivity : AppCompatActivity() {

    private lateinit var main_IMG_hearts: Array<AppCompatImageView>
    private lateinit var main_FAB_right: ExtendedFloatingActionButton
    private lateinit var main_FAB_left: ExtendedFloatingActionButton
    private lateinit var main_IMG_players: Array<AppCompatImageView>
    private lateinit var main_IMG_cakes: Array<AppCompatImageView>

override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViews()
        initViews()
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

    private fun refreshUI() {
        if(gameManager.isGameOver()) {
            changeActivity("😭 Game Over! ")
        } else if (gameManager.isGameEnded()) {
            changeActivity("🥳You Won!")
        } else {
            updateHearts()
            updatePlayers()
            updateCakes()
        }
    }

    private fun changeActivity(message: String) {
        val intent = Intent(this, EndActivity::class.java)
        var bundle = Bundle()
        bundle.putString(Constants.BundleKeys.STATUS_KEY, message)
        intent.putExtras(bundle)
        startActivity(intent)
        finish()
    }

    private fun updatePlayers() {
        for (i in 0 until main_IMG_players.size) {
            if (gameManager.isPlayerVisible(i)) {
                main_IMG_players[i].visibility = View.VISIBLE
            } else {
                main_IMG_players[i].visibility = View.INVISIBLE
            }
        }
    }

    private fun updateCakes() {
        for (i in 0 until main_IMG_cakes.size) {
            if (gameManager.isCakeVisible(i)) {
                main_IMG_cakes[i].visibility = View.VISIBLE
            } else {
                main_IMG_cakes[i].visibility = View.INVISIBLE
            }
        }
    }

    private fun updateHearts() {
        if (gameManager.wrongAnswers != 0) {
            main_IMG_hearts[main_IMG_hearts.size - gameManager.wrongAnswers].visibility =
                View.INVISIBLE
        }
    }

}
