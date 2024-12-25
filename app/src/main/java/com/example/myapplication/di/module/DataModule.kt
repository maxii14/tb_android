package com.example.myapplication.di.module

import android.content.Context
import androidx.room.Room
import com.example.myapplication.data.datasource.local.AppDB
import com.example.myapplication.data.datasource.local.CachedJokeDao
import com.example.myapplication.data.datasource.remote.JokeDao
import com.example.myapplication.data.datasource.remote.JokesApi
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
class DataModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(
                OkHttpClient.Builder().addInterceptor(HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                })
                .build()
            )
            .addConverterFactory(provideSerializationFactory()) // GsonConverterFactory.create()
            .build()
    }

    @Provides
    @Singleton
    fun provideJokesApi(retrofit: Retrofit): JokesApi {
        return retrofit.create(JokesApi::class.java)
    }

    @Provides
    @Singleton
    fun provideDatabase(context: Context): AppDB {
        return Room.databaseBuilder(
            context.applicationContext,
            AppDB::class.java,
            DB_NAME
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    @Singleton
    fun provideJokeDao(db: AppDB): JokeDao {
        return db.jokeDao()
    }

    @Provides
    @Singleton
    fun provideCachedJokeDao(db: AppDB): CachedJokeDao {
        return db.cachedJokeDao()
    }

    private companion object {
        private const val BASE_URL = "https://v2.jokeapi.dev/joke/"
        private const val DB_NAME = "app_db"
    }

}

fun provideSerializationFactory(): Converter.Factory {
    val contentType: MediaType = "application/json".toMediaType()
    val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }
    return json.asConverterFactory(contentType)
}