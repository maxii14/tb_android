package com.example.myapplication.di

import androidx.lifecycle.ViewModel
import com.example.myapplication.data.repository.JokeGenerator
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface PresentationModule {
    @Binds
    @IntoMap
    @ViewModelKey(JokeGenerator::class)
    fun bindJokeViewModel(viewModel: JokeGenerator): ViewModel
}