package com.example.myapplication.data

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable
data class Joke (
    @SerialName("id")
    val id: String,
    @SerialName("setup")
    var title: String,
    @SerialName("category")
    var category: String,
    @SerialName("delivery")
    var answer: String,
    var fromApi: Boolean = true
)
