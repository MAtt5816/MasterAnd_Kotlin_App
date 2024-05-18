package com.example.kotlinlab

import com.example.kotlinlab.repositories.PlayerScoresRepository
import com.example.kotlinlab.repositories.PlayersRepository
import com.example.kotlinlab.repositories.ScoresRepository

interface AppContainer {
    val playersRepository: PlayersRepository
    val scoresRepository: ScoresRepository
    val playerScoresRepository: PlayerScoresRepository
}