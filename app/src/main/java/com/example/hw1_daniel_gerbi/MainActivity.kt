package com.example.hw1_daniel_gerbi

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import com.example.hw1_daniel_gerbi.interfaces.TiltCallback
import com.example.hw1_daniel_gerbi.logic.GameManager
import com.example.hw1_daniel_gerbi.utilities.BackgroundMusicPlayer
import com.example.hw1_daniel_gerbi.utilities.Constants
import com.example.hw1_daniel_gerbi.utilities.SignalManager
import com.example.hw1_daniel_gerbi.utilities.SingleSoundPlayer
import com.example.hw1_daniel_gerbi.utilities.TiltDetector
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.textview.MaterialTextView

class MainActivity : AppCompatActivity() {

    private lateinit var main_IMG_hearts: Array<AppCompatImageView>

    private lateinit var main_LBL_score: MaterialTextView

    private lateinit var main_FAB_right: ExtendedFloatingActionButton

    private lateinit var main_FAB_left: ExtendedFloatingActionButton

    private lateinit var main_IMG_players: Array<AppCompatImageView>

    private lateinit var main_IMG_cakes: Array<Array<AppCompatImageView>>

    private lateinit var main_IMG_coins: Array<Array<AppCompatImageView>>

    private lateinit var gameManager: GameManager

    private lateinit var tiltDetector: TiltDetector

    private var isUsingSensors : Boolean = false
    private var selectedSpeed : String = "Normal"


    val handler = Handler(Looper.getMainLooper())

    private var isRunning = false

    private val runnable = object : Runnable {
        override fun run() {
            if (!isRunning) {
                isRunning = true
                try {
                        gameManager.moveCakesDown()
                        gameManager.moveCoinsDown()
                        refreshUI()
                } finally {
                    isRunning = false
                }
            }
            handler.postDelayed(this, Constants.GameLogic.DELAY_MILLIS)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        isUsingSensors = intent.getBooleanExtra("IS_USING_SENSORS", false)
        selectedSpeed = intent.getStringExtra("SELECTED_SPEED") ?: "Normal"

        findViews()
        gameManager = GameManager(main_IMG_hearts.size)
        initViews()
        if(isUsingSensors){
            initTiltDetector()
            main_FAB_left.visibility = View.INVISIBLE
            main_FAB_right.visibility = View.INVISIBLE
        }
        startGameLoop()
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
            findViewById(R.id.main_IMG_player3),
            findViewById(R.id.main_IMG_player4),
            findViewById(R.id.main_IMG_player5)
        )
        main_IMG_cakes = arrayOf(
            arrayOf(
                findViewById(R.id.main_IMG_cake1),
                findViewById(R.id.main_IMG_cake2),
                findViewById(R.id.main_IMG_cake3),
                findViewById(R.id.main_IMG_cake4),
                findViewById(R.id.main_IMG_cake5)
            ),
            arrayOf(
                findViewById(R.id.main_IMG_cake6),
                findViewById(R.id.main_IMG_cake7),
                findViewById(R.id.main_IMG_cake8),
                findViewById(R.id.main_IMG_cake9),
                findViewById(R.id.main_IMG_cake10)
            ),
            arrayOf(
                findViewById(R.id.main_IMG_cake11),
                findViewById(R.id.main_IMG_cake12),
                findViewById(R.id.main_IMG_cake13),
                findViewById(R.id.main_IMG_cake14),
                findViewById(R.id.main_IMG_cake15)
            ),
            arrayOf(
                findViewById(R.id.main_IMG_cake16),
                findViewById(R.id.main_IMG_cake17),
                findViewById(R.id.main_IMG_cake18),
                findViewById(R.id.main_IMG_cake19),
                findViewById(R.id.main_IMG_cake20)
            ),
            arrayOf(
                findViewById(R.id.main_IMG_cake21),
                findViewById(R.id.main_IMG_cake22),
                findViewById(R.id.main_IMG_cake23),
                findViewById(R.id.main_IMG_cake24),
                findViewById(R.id.main_IMG_cake25)
            ),
            arrayOf(
                findViewById(R.id.main_IMG_cake26),
                findViewById(R.id.main_IMG_cake27),
                findViewById(R.id.main_IMG_cake28),
                findViewById(R.id.main_IMG_cake29),
                findViewById(R.id.main_IMG_cake30)
            ),
            arrayOf(
                findViewById(R.id.main_IMG_cake31),
                findViewById(R.id.main_IMG_cake32),
                findViewById(R.id.main_IMG_cake33),
                findViewById(R.id.main_IMG_cake34),
                findViewById(R.id.main_IMG_cake35)
            ),
            arrayOf(
                findViewById(R.id.main_IMG_cake36),
                findViewById(R.id.main_IMG_cake37),
                findViewById(R.id.main_IMG_cake38),
                findViewById(R.id.main_IMG_cake39),
                findViewById(R.id.main_IMG_cake40)
            )
        )

        main_IMG_coins = arrayOf(
            arrayOf(
                findViewById(R.id.main_IMG_coin1),
                findViewById(R.id.main_IMG_coin2),
                findViewById(R.id.main_IMG_coin3),
                findViewById(R.id.main_IMG_coin4),
                findViewById(R.id.main_IMG_coin5)
            ),
            arrayOf(
                findViewById(R.id.main_IMG_coin6),
                findViewById(R.id.main_IMG_coin7),
                findViewById(R.id.main_IMG_coin8),
                findViewById(R.id.main_IMG_coin9),
                findViewById(R.id.main_IMG_coin10)
            ),
            arrayOf(
                findViewById(R.id.main_IMG_coin11),
                findViewById(R.id.main_IMG_coin12),
                findViewById(R.id.main_IMG_coin13),
                findViewById(R.id.main_IMG_coin14),
                findViewById(R.id.main_IMG_coin15)
            ),
            arrayOf(
                findViewById(R.id.main_IMG_coin16),
                findViewById(R.id.main_IMG_coin17),
                findViewById(R.id.main_IMG_coin18),
                findViewById(R.id.main_IMG_coin19),
                findViewById(R.id.main_IMG_coin20)
            ),
            arrayOf(
                findViewById(R.id.main_IMG_coin21),
                findViewById(R.id.main_IMG_coin22),
                findViewById(R.id.main_IMG_coin23),
                findViewById(R.id.main_IMG_coin24),
                findViewById(R.id.main_IMG_coin25)
            ),
            arrayOf(
                findViewById(R.id.main_IMG_coin26),
                findViewById(R.id.main_IMG_coin27),
                findViewById(R.id.main_IMG_coin28),
                findViewById(R.id.main_IMG_coin29),
                findViewById(R.id.main_IMG_coin30)
            ),
            arrayOf(
                findViewById(R.id.main_IMG_coin31),
                findViewById(R.id.main_IMG_coin32),
                findViewById(R.id.main_IMG_coin33),
                findViewById(R.id.main_IMG_coin34),
                findViewById(R.id.main_IMG_coin35)
            ),
            arrayOf(
                findViewById(R.id.main_IMG_coin36),
                findViewById(R.id.main_IMG_coin37),
                findViewById(R.id.main_IMG_coin38),
                findViewById(R.id.main_IMG_coin39),
                findViewById(R.id.main_IMG_coin40)
            )
        )
    }

    private fun initViews() {

        main_LBL_score.text = gameManager.score.toString()

        if (!isUsingSensors) {
            main_FAB_right.setOnClickListener {
                movePlayerRight()
            }
            main_FAB_left.setOnClickListener {
                movePlayerLeft()
            }
        }
        val speed = speedToDelay(selectedSpeed)
        handler.postDelayed(runnable, speed)
        refreshUI()
    }

    private fun speedToDelay(speed: String): Long {
        return when (speed) {
            "Slow" -> Constants.GameLogic.DELAY_MILLIS * 3
            "Normal" -> Constants.GameLogic.DELAY_MILLIS
            "Fast" -> Constants.GameLogic.DELAY_MILLIS / 3
            else -> Constants.GameLogic.DELAY_MILLIS
        }
    }

    private fun initTiltDetector() {
        tiltDetector = TiltDetector(
            context = this,
            tiltCallback = object : TiltCallback {
                override fun tiltX() {
                    if (tiltDetector.tiltCounterX > 0) {
                        movePlayerRight()
                    } else {
                        movePlayerLeft()
                    }
                }
            }
        )
        tiltDetector.start()
    }

    override fun onResume() {
        super.onResume()
        BackgroundMusicPlayer.getInstance().playMusic()
        if(isUsingSensors){
            tiltDetector.start()
        }
    }

    override fun onPause() {
        super.onPause()
        BackgroundMusicPlayer.getInstance().pauseMusic()
        tiltDetector.stop()
    }

    override fun onStop() {
        super.onStop()
        handler.removeCallbacks(runnable)
        if (isUsingSensors) {
            tiltDetector.stop()
        }
    }

    private fun refreshUI() {
        if (gameManager.isGameOver) {
            showMessage("Game Over! 😭 ")
            handler.removeCallbacks(runnable)
            changeActivity("Game Over! 😭 ", gameManager.score)
        } else {
            updatePlayers()
            updateCakes()
            updateCoins()
            if(gameManager.checkCollisionCake()){
                showMessage("You ate the cakes , it's not healthy!")
                SignalManager.getInstance().vibrate(Constants.GameLogic.DURATION)
                SingleSoundPlayer(this).playSound(R.raw.crash_sound)
                updateHearts()
            }
            if(gameManager.checkCollisionCoin()){
                showMessage("You ate the coins , you are rich!")
                SingleSoundPlayer(this).playSound(R.raw.collect_coins)
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

    private fun showMessage(txt: String) {
        SignalManager.getInstance().toast(txt)
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

    private fun updateCoins() {
        for (row in main_IMG_coins.indices) {
            for (col in main_IMG_coins[row].indices) {
                if (gameManager.coinMatrix[row][col]) {
                    main_IMG_coins[row][col].visibility = View.VISIBLE
                } else {
                    main_IMG_coins[row][col].visibility = View.INVISIBLE
                }
            }
        }
    }

    private fun updateHearts() {
        if (gameManager.hitPosition != 0 && gameManager.hitPosition <= main_IMG_hearts.size) {
            main_IMG_hearts[main_IMG_hearts.size - gameManager.hitPosition].visibility =
                View.INVISIBLE
            }
        }

    private fun startGameLoop() {
        val speed = speedToDelay(selectedSpeed)
        handler.removeCallbacks(runnable)
        handler.postDelayed(runnable, speed)
    }

    private fun changeActivity(message: String, score: Int) {
        handler.removeCallbacks(runnable)
        val intent = Intent(this, ScoreActivity::class.java)
        var bundle = Bundle()
        bundle.putInt(Constants.BundleKeys.SCORE_KEY, score)
        bundle.putString(Constants.BundleKeys.STATUS_KEY, message)
        intent.putExtras(bundle)
        startActivity(intent)
        finish()
    }

}

