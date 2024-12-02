package com.example.myapplication.data.db.repo

import com.example.myapplication.data.CachedJoke
import com.example.myapplication.data.db.dao.CachedJokeDao
import kotlinx.coroutines.flow.Flow

class CachedJokesRepository (private val cachedJokeDao: CachedJokeDao) {
    fun getAllCachedJokes(): Flow<List<CachedJoke>> = cachedJokeDao.getAllJokesFromCache()

    suspend fun addJokesToCache(jokes: List<CachedJoke>) {
        cachedJokeDao.insertAllToCache(jokes)
    }

    suspend fun clearOldCache() {
        cachedJokeDao.deleteOldCache(System.currentTimeMillis() - 24 * 60 * 60 * 1000L)
    }
}