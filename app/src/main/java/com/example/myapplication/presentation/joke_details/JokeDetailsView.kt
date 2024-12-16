package com.example.myapplication.presentation.joke_details

import com.example.myapplication.domain.entity.Joke

interface JokeDetailsView {

    fun showJokeInfo(joke: Joke)
    fun showError(error: String)
}