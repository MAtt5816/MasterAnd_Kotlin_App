package com.example.kotlinlab.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "players")
data class Player(
    @PrimaryKey(autoGenerate = true)
    val playerId: Long = 0,
    val name: String = "",
    val email: String = "",
    val profileImageUri: String = "",
    val description: String = ""
)
