package com.example.kotlinlab.data

import androidx.room.Embedded
import androidx.room.Relation

data class PlayerWithScore(
    val scoreId: Long,
    val playerId: Long
)
