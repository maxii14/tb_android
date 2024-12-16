package com.example.myapplication.data.datasource.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.myapplication.domain.entity.CachedJoke
import kotlinx.coroutines.flow.Flow

@Dao
interface CachedJokeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllToCache(jokes: List<CachedJoke>)

    @Query("DELETE FROM j_cache WHERE timeCreated < :dayAgoMillis")
    suspend fun deleteOldCache(dayAgoMillis: Long)

    @Query("DELETE FROM j_cache")
    suspend fun deleteCache()

    @Query("SELECT * FROM j_cache")
    fun getAllJokesFromCache(): Flow<List<CachedJoke>>
}