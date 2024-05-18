package com.example.kotlinlab.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.kotlinlab.data.Score
import com.example.kotlinlab.repositories.ScoresRepository

class GameViewModel(
    private val scoresRepository: ScoresRepository
) : ViewModel() {
    val playerId = mutableStateOf(0L)
    var scoreId = mutableStateOf(0L)
    val points = mutableStateOf(0)

    suspend fun saveScore() {
        scoreId.value = 0L
        scoreId.value = scoresRepository.insert(Score(scoreId.value, playerId.value, points.value))
    }
}