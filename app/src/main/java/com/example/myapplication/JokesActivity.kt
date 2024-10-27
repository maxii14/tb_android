package com.example.myapplication

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.data.Joke
import com.example.myapplication.data.JokeGenerator
import com.example.myapplication.databinding.ActivityJokesBinding
import com.example.myapplication.recycler.JokeAdapter

class JokesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityJokesBinding
    private val adapter = JokeAdapter()
    private val jokeGenerator = JokeGenerator()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJokesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        createRWList(7)
    }

    private fun createRWList(jokesCount: Int) {
        binding.rw.adapter = adapter
        adapter.setNewData(jokeGenerator.generateJokesList(jokesCount))
    }
}