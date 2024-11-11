package com.example.myapplication.ui.joke_list.recycler

import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.data.Joke
import com.example.myapplication.databinding.JokeItemBinding

class JokeViewHolder(
    private val binding: JokeItemBinding
): RecyclerView.ViewHolder(binding.root) {

    fun bind(joke: Joke) {
        binding.jokeTitle.text = joke.title
        binding.jokeText.text = joke.answer
        binding.jokeCategory.text = "Категория: ${joke.catogory}"
    }
}