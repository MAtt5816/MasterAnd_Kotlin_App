package com.example.kotlinlab.repositories

import com.example.kotlinlab.dao.PlayerScoreDao
import com.example.kotlinlab.data.PlayerWithScore
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface PlayerScoresRepository {
    fun loadPlayersWithScores(): Flow<List<PlayerWithScore>>
}

@Module
@InstallIn(ViewModelComponent::class)
abstract class PlayerScoresModule {
    @Binds
    abstract fun bindPlayerScoresRepository(playerScoresRepositoryImpl: PlayerScoresRepositoryImpl): PlayerScoresRepository
}

class PlayerScoresRepositoryImpl @Inject constructor(private val scoreDao: PlayerScoreDao) : PlayerScoresRepository {
    override fun loadPlayersWithScores(): Flow<List<PlayerWithScore>> =
        scoreDao.loadPlayersWithScores()
}