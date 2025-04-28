package com.example.myapplication.data.db.repo

import com.example.myapplication.data.Joke
import com.example.myapplication.data.db.dao.JokeDao
import kotlinx.coroutines.flow.Flow

class CustomJokesRepository (private val jokeDao: JokeDao) {
    fun getAllJokes(): Flow<List<Joke>> = jokeDao.getAllJokes()

    suspend fun addJoke(joke: Joke) {
        jokeDao.insert(joke)
    }

    fun deleteAllJokes() {
        jokeDao.deleteAllJokes()
    }
}