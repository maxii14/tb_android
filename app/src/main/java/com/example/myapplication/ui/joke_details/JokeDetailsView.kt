package com.example.myapplication.ui.joke_details

import com.example.myapplication.data.Joke
import java.lang.Error

interface JokeDetailsView {

    fun showJokeInfo(joke: Joke)
    fun showError(error: String)
}