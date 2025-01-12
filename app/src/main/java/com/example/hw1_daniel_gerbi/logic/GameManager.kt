package com.example.hw1_daniel_gerbi.logic

import com.example.hw1_daniel_gerbi.utilities.Constants

class GameManager(private val lifeCount: Int = 3, private val numOfLanes : Int = 5) {

    var score: Int = 0
        private set

    var playerPosition = 1
    var hitPosition = 0
    val cakeMatrix: Array<Array<Boolean>>
    val coinMatrix: Array<Array<Boolean>>

    init {
        cakeMatrix = getRandomMatrix()
        coinMatrix = getRandomMatrixWithAvoidance(cakeMatrix)
    }

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

    private fun getRandomMatrixWithAvoidance(obstacleMatrix: Array<Array<Boolean>>): Array<Array<Boolean>> {
        val arr: Array<Array<Boolean>> = Array(8) { Array(numOfLanes) { false } }
        for (i in arr.indices) {
            arr[i] = getRandomBooleanArrayWithoutOverlap(numOfLanes, obstacleMatrix[i])
        }
        return arr
    }

    private fun getRandomBooleanArray(size: Int): Array<Boolean> {
        val array = Array(size) { false }
        val randomIndex = (0 until size).random()
        array[randomIndex] = true
        return array
    }
    fun moveCakesDown() {
        for (row in 7 downTo 1) {
            for (col in 0 until 5) {
                    cakeMatrix[row][col] = cakeMatrix[row - 1][col]
            }
        }
        cakeMatrix[0] = getRandomBooleanArray(numOfLanes)
    }

    private fun getRandomBooleanArrayWithoutOverlap(size: Int, obstacleRow: Array<Boolean>): Array<Boolean> {
        val array = Array(size) { false }
        val possibleIndexes = (0 until size).filter { !obstacleRow[it] }
        val coinAppearanceProbability = 0.2
        if (possibleIndexes.isNotEmpty() && Math.random() < coinAppearanceProbability) {
            val randomIndex = possibleIndexes.random()
            array[randomIndex] = true
        }
        return array
    }

    fun moveCoinsDown() {
        for (row in 7 downTo 1) {
            for (col in 0 until 5) {
                coinMatrix[row][col] = coinMatrix[row - 1][col]
            }
        }
        coinMatrix[0] = getRandomBooleanArrayWithoutOverlap(numOfLanes, cakeMatrix[0])
    }


    fun checkCollisionCake(): Boolean {
        if (cakeMatrix[7][playerPosition]) {
            hitPosition++
            return true
        }
        return false
    }

    fun checkCollisionCoin(): Boolean {
        if (coinMatrix[7][playerPosition]) {
            score += Constants.GameLogic.CATCH_COINS
            return true
        }
        return false
    }

    fun updateScore() {
        score += Constants.GameLogic.AVOID_POINTS
    }
}
