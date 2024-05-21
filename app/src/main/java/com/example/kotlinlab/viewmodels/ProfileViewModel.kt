package com.example.kotlinlab.viewmodels

import android.net.Uri
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.kotlinlab.data.Player
import com.example.kotlinlab.repositories.PlayersRepository
import kotlinx.coroutines.flow.first

class ProfileViewModel(private val playersRepository: PlayersRepository) : ViewModel() {
    var playerId = mutableStateOf(0L)
    val name = mutableStateOf("")
    val email = mutableStateOf("")
    val profileImageUri = mutableStateOf<Uri?>(null)
    val description = mutableStateOf("")

    suspend fun showPlayer(id: Long) {
        playerId.value = id
        val player = playersRepository.getPlayerStream(playerId.value).first()
        name.value = player?.name ?: ""
        email.value = player?.email ?: ""
        description.value = player?.description ?: ""
        if (player?.profileImageUri != null && player.profileImageUri != "") {
            profileImageUri.value = Uri.parse(player.profileImageUri)
        } else {
            profileImageUri.value = null
        }
    }

    suspend fun savePlayerDescription() {
        playersRepository.updatePlayer(
            Player(
                playerId.value,
                name.value,
                email.value,
                profileImageUri.value?.toString() ?: "",
                description.value
            )
        )
    }
}