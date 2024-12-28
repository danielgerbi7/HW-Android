package com.example.hw1_daniel_gerbi.logic

import com.example.hw1_daniel_gerbi.utilities.Constants

class GameManager(private val lifeCount: Int = 3, private val numOfLanes : Int = 5) {

    var score: Int = 0
        private set

    var playerPosition = 1
    var hitPosition = 0

    val cakeMatrix: Array<Array<Boolean>> = getRandomMatrix()
    val coinMatrix: Array<Array<Boolean>> = getRandomMatrix()

    fun canMovePlayerLeft(): Boolean {
        return playerPosition > 0
    }

    fun canMovePlayerRight(): Boolean {
        return playerPosition < 4
    }

    fun movePlayerLeft() {
        if (canMovePlayerLeft())
            playerPosition--
    }

    fun movePlayerRight() {
        if (canMovePlayerRight())
            playerPosition++
    }

    val isGameOver: Boolean
        get() = hitPosition == lifeCount

    private fun getRandomMatrix() : Array<Array<Boolean>>{
        val arr: Array<Array<Boolean>> = Array(8) { Array(numOfLanes) { false } }
        for(i in 0..4){
            arr[i] = getRandomBooleanArray(numOfLanes)
        }
        return arr
    }

    fun moveCakesDown() {
        for (row in 7 downTo 1) {
            for (col in 0 until 5) {
                    cakeMatrix[row][col] = cakeMatrix[row - 1][col]
            }
        }
        cakeMatrix[0] = getRandomBooleanArray(numOfLanes)
    }

    fun moveCoinDown() {
        for (row in 7 downTo 1) {
            for (col in 0 until 5) {
                coinMatrix[row][col] = coinMatrix[row - 1][col]
            }
        }
        coinMatrix[0] = getRandomBooleanArray(numOfLanes)
    }

    private fun getRandomBooleanArray(size: Int): Array<Boolean> {
        val array = Array(size) { false }
        val randomIndex = (0 until size).random()
        array[randomIndex] = true
        return array
    }

    fun checkCollisionCake(): Boolean {
        if (cakeMatrix[6][playerPosition]) {
            hitPosition++
            return true
        }
        return false
    }

    fun checkCollisionCoin(): Boolean {
        if (coinMatrix[6][playerPosition]) {
            score += Constants.GameLogic.CATCH_COINS
            return true
        }
        return false
    }

    fun updateScore() {
        score += Constants.GameLogic.AVOID_POINTS
    }

}
