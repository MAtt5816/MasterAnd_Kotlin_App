package com.example.kotlinlab

import android.content.Context
import com.example.kotlinlab.dao.PlayerDao
import com.example.kotlinlab.dao.PlayerScoreDao
import com.example.kotlinlab.dao.ScoreDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {
    @Provides
    fun providesPlayerDao(highScoreDatabase: HighScoreDatabase): PlayerDao {
        return highScoreDatabase.playerDao()
    }@Provides
    fun providesPlayerScoreDao(highScoreDatabase: HighScoreDatabase): PlayerScoreDao {
        return highScoreDatabase.playerScoreDao()
    }@Provides
    fun providesScoreDao(highScoreDatabase: HighScoreDatabase): ScoreDao {
        return highScoreDatabase.scoreDao()
    }

    @Provides
    @Singleton
    fun provideHighScoreDatabase(
        @ApplicationContext applicationContext: Context
    ) : HighScoreDatabase {
        return HighScoreDatabase.getDatabase(applicationContext)
    }
}