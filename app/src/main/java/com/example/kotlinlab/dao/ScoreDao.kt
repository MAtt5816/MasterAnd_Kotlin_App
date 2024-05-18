package com.example.kotlinlab.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.kotlinlab.data.Score

@Dao
interface ScoreDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(score: Score): Long

    @Query("SELECT points from scores WHERE scoreId = :id")
    suspend fun getScore(id: Long): Int
}