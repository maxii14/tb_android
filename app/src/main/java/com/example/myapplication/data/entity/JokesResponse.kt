package com.example.myapplication.data.entity

import com.example.myapplication.domain.entity.Joke
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