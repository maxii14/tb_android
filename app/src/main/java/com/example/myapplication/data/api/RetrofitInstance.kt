package com.example.myapplication.data.api

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

fun provideSerializationFactory(): Converter.Factory {
    val contentType: MediaType = "application/json".toMediaType()
    val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }
    return json.asConverterFactory(contentType)
}

object RetrofitInstance {
    private const val BASE_URL = "https://v2.jokeapi.dev/joke/"

    val api: JokesApi by lazy {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(provideSerializationFactory()) // GsonConverterFactory.create()
            .build()
            .create(JokesApi::class.java)
    }
}