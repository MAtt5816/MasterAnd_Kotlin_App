package com.example.kotlinlab.repositories

import com.example.kotlinlab.dao.PlayerDao
import com.example.kotlinlab.data.Player
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface PlayersRepository {
    fun getAllPlayersStream(): Flow<List<Player>>
    fun getPlayerStream(id: Long): Flow<Player?>
    suspend fun getPlayersByEmail(email: String): List<Player>
    suspend fun insertPlayer(player: Player): Long
    suspend fun updatePlayer(player: Player)
}

@Module
@InstallIn(ViewModelComponent::class)
abstract class PlayersModule {
    @Binds
    abstract fun bindScoresRepository(playersRepositoryImpl: PlayersRepositoryImpl): PlayersRepository
}

class PlayersRepositoryImpl @Inject constructor(private val playerDao: PlayerDao) : PlayersRepository {
    override fun getAllPlayersStream(): Flow<List<Player>> =
        playerDao.getAllPlayersStream()

    override fun getPlayerStream(playerId: Long): Flow<Player?> =
        playerDao.getPlayerStream(playerId)

    override suspend fun getPlayersByEmail(email: String): List<Player> =
        playerDao.getPlayersByEmail(email)

    override suspend fun insertPlayer(player: Player): Long = playerDao.insert(player)

    override suspend fun updatePlayer(player: Player) = playerDao.update(player)
}