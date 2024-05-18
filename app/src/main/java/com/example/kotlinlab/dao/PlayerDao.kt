package com.example.kotlinlab.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.kotlinlab.data.Player
import kotlinx.coroutines.flow.Flow

@Dao
interface PlayerDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(player: Player): Long

    @Update(onConflict = OnConflictStrategy.IGNORE)
    suspend fun update(player: Player)

    @Query("SELECT * from players")
    fun getAllPlayersStream(): Flow<List<Player>>

    @Query("SELECT * from players WHERE playerId = :playerId")
    fun getPlayerStream(playerId: Long): Flow<Player>

    @Query("SELECT * from players WHERE email = :email")
    suspend fun getPlayersByEmail(email: String): List<Player>
}