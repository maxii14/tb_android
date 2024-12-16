package com.example.myapplication.domain.repository

import com.example.myapplication.domain.entity.Joke
import kotlinx.coroutines.flow.Flow

interface CustomJokesRepository {
    fun getAllJokes(): Flow<List<Joke>>
    suspend fun addJoke(joke: Joke)
    fun deleteAllJokes()
}