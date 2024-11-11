package com.example.myapplication.ui.joke_details

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import com.example.myapplication.data.Joke
import com.example.myapplication.data.JokeGenerator
import com.example.myapplication.databinding.ActivityJokeDetailsBinding
import com.example.myapplication.databinding.ActivityJokesBinding

class JokeDetailsActivity : AppCompatActivity(), JokeDetailsView {

    private var jokePosition: Int = -1
    private lateinit var binding: ActivityJokeDetailsBinding
    private lateinit var presenter: JokeDetailsPresenter

    companion object {
        private const val JOKE_POSITION_EXTRA = "JOKE_POSITION"
        fun getInstance(context: Context, jokePosition: Int): Intent {
            return Intent(context, JokeDetailsActivity::class.java).apply {
                putExtra(JOKE_POSITION_EXTRA, jokePosition)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJokeDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        presenter = JokeDetailsPresenter(this)
        handleExtra()
    }

    private fun handleExtra() {
        jokePosition = intent.getIntExtra(JOKE_POSITION_EXTRA, -1)
        presenter.loadJokeDetails(jokePosition)
    }


    override fun showJokeInfo(joke: Joke) {
        with(binding) {
            jokeTitle.text = "${joke.title}"
            jokeCategory.text = "${joke.catogory}"
            jokeText.text = "Шутка: ${joke.answer}"
        }
    }

    override fun showError(error: String) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
        finish()
    }
}