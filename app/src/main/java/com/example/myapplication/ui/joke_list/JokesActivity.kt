package com.example.myapplication.ui.joke_list

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.myapplication.R
import com.example.myapplication.data.JokeGenerator
import com.example.myapplication.data.db.AppDB
import com.example.myapplication.databinding.ActivityJokesBinding
import com.example.myapplication.databinding.FragmentJokesListBinding
import com.example.myapplication.ui.App
import com.example.myapplication.ui.fragments.FViewModel
import com.example.myapplication.ui.fragments.JokesListFragment

class JokesActivity : AppCompatActivity() {


    private lateinit var binding: ActivityJokesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        //supportFragmentManager.fragmentFactory = CustomFragmentFactory()
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