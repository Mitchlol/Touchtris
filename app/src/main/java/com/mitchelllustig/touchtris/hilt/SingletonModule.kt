package com.mitchelllustig.touchtris.hilt

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.mitchelllustig.touchtris.database.HighScoreDao
import com.mitchelllustig.touchtris.database.TouchtrisDB
import com.mitchelllustig.touchtris.settings.AppSettings
import com.mitchelllustig.touchtris.settings.ContinueSettings
import com.mitchelllustig.touchtris.settings.GameSettings
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class SingletonModule {

    @Provides
    @Singleton
    fun sharedPreferences(@ApplicationContext context: Context): SharedPreferences{
        return context.getSharedPreferences("myprefs", Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun gameSettings(prefs: SharedPreferences): GameSettings {
        return GameSettings(prefs)
    }

    @Provides
    @Singleton
    fun appSettings(prefs: SharedPreferences): AppSettings {
        return AppSettings(prefs)
    }

    @Provides
    @Singleton
    fun continueSettings(prefs: SharedPreferences): ContinueSettings {
        return ContinueSettings(prefs)
    }

    @Provides
    @Singleton
    fun touchtrisDB(@ApplicationContext context: Context): TouchtrisDB {
        return Room.databaseBuilder(
            context,
            TouchtrisDB::class.java, "touchtris"
        ).build()
    }

    @Provides
    @Singleton
    fun highScoreDAO(db: TouchtrisDB): HighScoreDao {
        return db.highScoreDao()
    }
}