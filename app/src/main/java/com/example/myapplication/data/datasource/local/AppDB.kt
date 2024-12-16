package com.example.myapplication.data.datasource.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.myapplication.domain.entity.CachedJoke
import com.example.myapplication.domain.entity.Joke
import com.example.myapplication.data.datasource.remote.JokeDao

@Database(entities = [Joke::class, CachedJoke::class], version = 3)
abstract class AppDB : RoomDatabase() {
    abstract fun jokeDao(): JokeDao
    abstract fun cachedJokeDao(): CachedJokeDao

    companion object {
        @Volatile
        lateinit var INSTANCE: AppDB

        fun initDatabase(context: Context) {
            val instance = Room.databaseBuilder(
                context.applicationContext,
                AppDB::class.java,
                "app_db"
            ).fallbackToDestructiveMigration().build()
            INSTANCE = instance
        }
    }
}