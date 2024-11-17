package com.example.myapplication.ui.joke_details

import com.example.myapplication.data.JokeGenerator

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