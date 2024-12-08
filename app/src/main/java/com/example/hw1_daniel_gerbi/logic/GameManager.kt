package com.example.hw1_daniel_gerbi.logic

class GameManager(private val lifeCount: Int = 3) {
    var playerPosition = 1
    var hitPosition = 0
    private var cakesVisibility = BooleanArray(15) { true }


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

    private fun isCorrectPosition(position: Int): Boolean {
        val validPositions = listOf(0, 1, 2)
        return validPositions.contains(position)
    }

    fun checkPosition(position: Int) {
        if (!isCorrectPosition(position)) {
            hitPosition++
        }
    }

    fun hideRandomCake() {
        val randomIndex = cakesVisibility.indices.random()
        cakesVisibility[randomIndex] = false
    }

    fun hasCollision(): Boolean {
        for (i in cakesVisibility.indices) {
            if (cakesVisibility[i] && playerPosition == i) {
                cakesVisibility[i] = false
                checkPosition(playerPosition)
                return true
            }
        }
        return false
    }
}