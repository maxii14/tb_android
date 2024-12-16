package com.example.myapplication.domain.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "jokes")
data class Joke (
    @SerialName("id")
    @PrimaryKey
    val id: String,
    @SerialName("setup")
    var title: String,
    @SerialName("category")
    var category: String,
    @SerialName("delivery")
    var answer: String,
    var fromApi: Boolean = true
)
