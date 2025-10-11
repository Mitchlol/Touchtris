package com.mitchelllustig.touchtris.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [HighScore::class], version = 1)
abstract class TouchtrisDB : RoomDatabase() {
    abstract fun highScoreDao(): HighScoreDao
}