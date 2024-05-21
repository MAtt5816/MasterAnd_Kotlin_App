package com.example.kotlinlab.repositories

import com.example.kotlinlab.dao.ScoreDao
import com.example.kotlinlab.data.Score
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import javax.inject.Inject

interface ScoresRepository {
    suspend fun insert(score: Score): Long
    suspend fun getScore(id: Long): Int
}

@Module
@InstallIn(ViewModelComponent::class)
abstract class ScoresModule {
    @Binds
    abstract fun bindScoresRepository(scoresRepositoryImpl: ScoresRepositoryImpl): ScoresRepository
}

class ScoresRepositoryImpl @Inject constructor(private val scoreDao: ScoreDao) : ScoresRepository {
    override suspend fun insert(score: Score): Long = scoreDao.insert(score)
    override suspend fun getScore(id: Long): Int = scoreDao.getScore(id)
}