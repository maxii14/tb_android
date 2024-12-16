package com.example.myapplication.domain.repository

import com.example.myapplication.domain.entity.CachedJoke
import kotlinx.coroutines.flow.Flow

interface CachedJokesRepository {
    fun getAllCachedJokes(): Flow<List<CachedJoke>>
    suspend fun addJokesToCache(jokes: List<CachedJoke>)
    suspend fun clearAllCache()
    suspend fun clearOldCache()
}