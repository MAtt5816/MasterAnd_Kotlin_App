package com.example.kotlinlab.viewmodels

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import com.example.kotlinlab.data.PlayerWithScore
import com.example.kotlinlab.repositories.PlayerScoresRepository
import com.example.kotlinlab.repositories.PlayersRepository
import com.example.kotlinlab.repositories.ScoresRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import javax.inject.Inject

@HiltViewModel
class ScoresViewModel @Inject constructor(
    private val playerWithScoresRepository: PlayerScoresRepository,
    private val playersRepository: PlayersRepository,
    private val scoresRepository: ScoresRepository
) : ViewModel() {
    var playersWithScore = mutableStateListOf<PlayerWithScore>()

    suspend fun showHighscore() {
        val playerScoresFlow = playerWithScoresRepository.loadPlayersWithScores()
        playersWithScore = playerScoresFlow.first().toMutableStateList()
    }

    suspend fun getPlayerName(id: Long): String {
        return playersRepository.getPlayerStream(id).first()?.name ?: ""
    }

    suspend fun getPlayerScore(id: Long): Int {
        return scoresRepository.getScore(id)
    }
}