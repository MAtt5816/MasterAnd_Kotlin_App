package com.example.kotlinlab.repositories

import com.example.kotlinlab.dao.PlayerScoreDao
import com.example.kotlinlab.data.PlayerWithScore
import kotlinx.coroutines.flow.Flow

interface PlayerScoresRepository {
    fun loadPlayersWithScores(): Flow<List<PlayerWithScore>>
}

class PlayerScoresRepositoryImpl(private val scoreDao: PlayerScoreDao) : PlayerScoresRepository {
    override fun loadPlayersWithScores(): Flow<List<PlayerWithScore>> =
        scoreDao.loadPlayersWithScores()
}