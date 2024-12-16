package com.example.myapplication.data.repository

import com.example.myapplication.domain.entity.CachedJoke
import com.example.myapplication.data.datasource.local.CachedJokeDao
import com.example.myapplication.domain.repository.CachedJokesRepository
import kotlinx.coroutines.flow.Flow

class CachedJokesRepository (private val cachedJokeDao: CachedJokeDao): CachedJokesRepository {
    override fun getAllCachedJokes(): Flow<List<CachedJoke>> = cachedJokeDao.getAllJokesFromCache()

    override suspend fun addJokesToCache(jokes: List<CachedJoke>) {
        cachedJokeDao.insertAllToCache(jokes)
    }

    override suspend fun clearAllCache() {
        cachedJokeDao.deleteOldCache(System.currentTimeMillis())
    }

    override suspend fun clearOldCache() {
        cachedJokeDao.deleteOldCache(System.currentTimeMillis() - 24 * 60 * 60 * 1000L)
    }
}