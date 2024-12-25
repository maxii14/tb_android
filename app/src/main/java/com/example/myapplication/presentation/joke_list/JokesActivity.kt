package com.example.myapplication.presentation.joke_list

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityJokesBinding
import com.example.myapplication.presentation.App
import com.example.myapplication.presentation.fragments.JokesListFragment

class JokesActivity : AppCompatActivity() {


    private lateinit var binding: ActivityJokesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        //supportFragmentManager.fragmentFactory = CustomFragmentFactory()
        (applicationContext as App).appComponent.inject(this)
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_jokes)
        binding = ActivityJokesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            openFragment()
        }
    }

    private fun openFragment() {
        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_container_view, JokesListFragment.newInstance())
            .commit()
    }
}