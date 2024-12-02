package com.example.myapplication.data.api

import retrofit2.http.GET
import retrofit2.http.Query

interface JokesApi {
    @GET("Any?")
    suspend fun getRandomJokes (
        @Query("blacklistFlags") blacklistFlags: String = "nsfw,religious,political,racist,sexist,explicit",
        @Query("type") type: String = "twopart",
        @Query("amount") amount: Int = 10
    ): JokesResponse
}