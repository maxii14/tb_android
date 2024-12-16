package com.example.myapplication.presentation.joke_details

import com.example.myapplication.data.repository.JokeGenerator

class JokeDetailsPresenter(private val view: JokeDetailsView) {
    fun loadJokeDetails(jokePosition: Int) {
        if (jokePosition == -1) {
            view.showError("Неверный индекс шутки")
        }

        JokeGenerator.data[jokePosition].let {
            view.showJokeInfo(it)
        }
    }
}