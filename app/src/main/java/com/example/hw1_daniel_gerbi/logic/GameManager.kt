package com.example.hw1_daniel_gerbi.logic

class GameManager(private val lifeCount: Int = 3) {
    var playerPosition = 1
    var hitPosition = 0
    private var cakesVisibility = BooleanArray(15) { true }
    val cakePositions = IntArray(15) { (0..2).random() }
    private val totalColums = 3

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


    fun movePlayerRight() {
        if (canMovePlayerRight())
            playerPosition++
    }

    val isGameOver: Boolean
        get() = hitPosition == lifeCount

    fun isCakeVisible(index: Int): Boolean {
        return cakesVisibility[index]
    }

    fun isPlayerVisible(index: Int): Boolean {
        return playerPosition == index
    }

    fun moveCakesDown() {
        for (i in cakePositions.indices) {
            if (cakesVisibility[i]) {
                if (cakePositions[i] == 2) {
                    cakesVisibility[i] = false
                } else {
                    cakePositions[i] = cakePositions[i] + 1
                }
            }
        }
    }

    private fun isCorrectPosition(position: Int): Boolean {
        val validPositions = listOf(0, 1, 2)
        return validPositions.contains(position)
    }

    fun checkPosition(position: Int) {
        if (position !in 0..2) {
            hitPosition++
        }
    }

    fun showRandomCake() {
        val randomIndex = cakesVisibility.indices.filter { !cakesVisibility[it] }.randomOrNull()
        randomIndex?.let {
            cakesVisibility[it] = true
            cakePositions[it] = (0..2).random()
        }
    }

    fun hideRandomCake() {
        val randomIndex = cakesVisibility.indices.random()
        cakesVisibility[randomIndex] = false
    }

    fun hasCollision(): Boolean {
        for (i in cakePositions.indices) {
            if (cakePositions[i] == playerPosition && cakesVisibility[i]) {
                return true
            }
        }
        return false
    }
}