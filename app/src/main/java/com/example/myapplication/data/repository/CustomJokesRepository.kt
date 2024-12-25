package com.example.myapplication.data.repository

import com.example.myapplication.domain.entity.Joke
import com.example.myapplication.data.datasource.remote.JokeDao
import com.example.myapplication.domain.repository.CustomJokesRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow

class CustomJokesRepository @Inject constructor(
    private val jokeDao: JokeDao
): CustomJokesRepository {
    override fun getAllJokes(): Flow<List<Joke>> = jokeDao.getAllJokes()

    override suspend fun addJoke(joke: Joke) {
        jokeDao.insert(joke)
    }

    override fun deleteAllJokes() {
        jokeDao.deleteAllJokes()
    }
}