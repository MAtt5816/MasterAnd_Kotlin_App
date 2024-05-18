package com.example.kotlinlab.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.kotlinlab.data.Player
import com.example.kotlinlab.repositories.PlayersRepository

class LoginViewModel(private val playersRepository: PlayersRepository) : ViewModel() {
    var playerId = mutableStateOf(0L)
    val name = mutableStateOf("")
    val email = mutableStateOf("")

    suspend fun savePlayer() {
        val players = playersRepository.getPlayersByEmail(email.value)
        if (players.size == 1) {
            val player = players.first()
            playerId.value = player.playerId

            playersRepository.updatePlayer(Player(playerId.value, name.value, email.value))
        }
        else {
            playerId.value = 0L
            val id = playersRepository.insertPlayer(Player(playerId.value, name.value, email.value))
            playerId.value = id
        }
    }
}