package com.example.kotlinlab

import android.content.Context
import com.example.kotlinlab.repositories.PlayerScoresRepository
import com.example.kotlinlab.repositories.PlayerScoresRepositoryImpl
import com.example.kotlinlab.repositories.PlayersRepository
import com.example.kotlinlab.repositories.PlayersRepositoryImpl
import com.example.kotlinlab.repositories.ScoresRepository
import com.example.kotlinlab.repositories.ScoresRepositoryImpl

class AppDataContainer(private val context: Context) : AppContainer {
    override val playersRepository: PlayersRepository by lazy {
        PlayersRepositoryImpl(HighScoreDatabase.getDatabase(context).playerDao())
    }

    override val scoresRepository: ScoresRepository by lazy {
        ScoresRepositoryImpl(HighScoreDatabase.getDatabase(context).scoreDao())
    }

    override val playerScoresRepository: PlayerScoresRepository by lazy {
        PlayerScoresRepositoryImpl(HighScoreDatabase.getDatabase(context).playerScoreDao())
    }
}