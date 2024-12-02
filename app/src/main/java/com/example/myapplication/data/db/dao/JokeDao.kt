package com.example.myapplication.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.example.myapplication.data.Joke
import kotlinx.coroutines.flow.Flow
import androidx.room.Query

@Dao
interface JokeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(jokes: List<Joke>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(joke: Joke)

    @Query("SELECT * FROM jokes")
    fun getAllJokes(): Flow<List<Joke>>

    @Query("DELETE FROM jokes")
    fun deleteAllJokes()
}