package com.example.kotlinlab.viewmodels

import android.net.Uri
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.kotlinlab.data.Player
import com.example.kotlinlab.repositories.PlayersRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val playersRepository: PlayersRepository) : ViewModel() {
    var playerId = mutableStateOf(0L)
    val name = mutableStateOf("")
    val email = mutableStateOf("")
    val description = mutableStateOf("")
    val profileImageUri = mutableStateOf<Uri?>(null)

    suspend fun savePlayer() {
        val players = playersRepository.getPlayersByEmail(email.value)
        if (players.size == 1) {
            val player = players.first()
            playerId.value = player.playerId
            description.value = player.description

            playersRepository.updatePlayer(Player(playerId.value, name.value, email.value, profileImageUri.value?.toString() ?: "", description.value))
        }
        else {
            playerId.value = 0L
            val id = playersRepository.insertPlayer(Player(playerId.value, name.value, email.value, profileImageUri.value?.toString() ?: "", description.value))
            playerId.value = id
        }
    }
}