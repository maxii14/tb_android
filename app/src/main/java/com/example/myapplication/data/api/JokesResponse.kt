package com.example.myapplication.data.api

import com.example.myapplication.data.Joke
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class JokesResponse (
    @SerialName("error")
    val error: Boolean,
    @SerialName("amount")
    val amount: Int,
    @SerialName("jokes")
    val jokes: List<Joke>,
)