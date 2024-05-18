package com.example.kotlinlab.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.kotlinlab.data.PlayerWithScore
import kotlinx.coroutines.flow.Flow

@Dao
interface PlayerScoreDao {
    @Query("SELECT players.playerId AS playerId, scores.scoreId AS scoreId "
            + "FROM players, scores WHERE players.playerId = scores.playerId")
    fun loadPlayersWithScores(): Flow<List<PlayerWithScore>>
}