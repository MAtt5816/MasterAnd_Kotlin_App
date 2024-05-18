package com.example.kotlinlab.repositories

import com.example.kotlinlab.dao.ScoreDao
import com.example.kotlinlab.data.Score

interface ScoresRepository {
    suspend fun insert(score: Score): Long
    suspend fun getScore(id: Long): Int
}

class ScoresRepositoryImpl(private val scoreDao: ScoreDao) : ScoresRepository {
    override suspend fun insert(score: Score): Long = scoreDao.insert(score)
    override suspend fun getScore(id: Long): Int = scoreDao.getScore(id)
}