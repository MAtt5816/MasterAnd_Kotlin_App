package com.example.kotlinlab

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.kotlinlab.dao.PlayerDao
import com.example.kotlinlab.dao.PlayerScoreDao
import com.example.kotlinlab.dao.ScoreDao
import com.example.kotlinlab.data.Player
import com.example.kotlinlab.data.Score

@Database(
    entities = [Score::class, Player::class], version = 1, exportSchema = false
)
abstract class HighScoreDatabase : RoomDatabase() {
    abstract fun playerDao(): PlayerDao
    abstract fun scoreDao(): ScoreDao
    abstract fun playerScoreDao(): PlayerScoreDao

    companion object {
        @Volatile
        private var Instance: HighScoreDatabase? = null
        fun getDatabase(context: Context): HighScoreDatabase {
            return Room.databaseBuilder(
                context,
                HighScoreDatabase::class.java,
                "highscore_database"
            ).build().also { Instance = it }
        }
    }
}