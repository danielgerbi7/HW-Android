package com.example.hw1_daniel_gerbi.logic

class GameManager(private val lifeCount: Int = 3, private val numOfLanes : Int = 3) {

    private var playerPosition = 1
    var hitPosition = 0

    val cakeMatrix: Array<Array<Boolean>> = getRandomMatrix()

    fun canMovePlayerLeft(): Boolean {
        return playerPosition > 0
    }

    fun canMovePlayerRight(): Boolean {
        return playerPosition < 2
    }

    fun movePlayerLeft() {
        if (canMovePlayerLeft())
            playerPosition--
    }

    fun isPlayerVisible(index: Int): Boolean {
        return index == playerPosition
    }

    fun movePlayerRight() {
        if (canMovePlayerRight())
            playerPosition++
    }

    val isGameOver: Boolean
        get() = hitPosition == lifeCount

    private fun getRandomMatrix() : Array<Array<Boolean>>{
        return Array(5) {  getRandomBooleanArray(numOfLanes) }
    }

    fun moveCakesDown() {
        for (row in 4 downTo 1) {
            for (col in 0 until 3) {
                    cakeMatrix[row][col] = cakeMatrix[row - 1][col]
            }
        }
        cakeMatrix[0] = getRandomBooleanArray(numOfLanes)
    }

    private fun getRandomBooleanArray(size: Int): Array<Boolean> {
        val array = Array(size) { false }
        val randomIndex = (0 until size).random()
        array[randomIndex] = true
        return array
    }

    private fun hasCollision(): Boolean {
        for (row in 0 until 5) {
            for (col in 0 until 3) {
                if (cakeMatrix[row][col] && col == playerPosition && row == 4) {
                    return true
                }
            }
        }
        return false
    }






}
