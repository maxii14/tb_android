package com.example.myapplication.ui.joke_list

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.myapplication.R
import com.example.myapplication.data.JokeGenerator
import com.example.myapplication.databinding.ActivityJokesBinding
import com.example.myapplication.databinding.FragmentJokesListBinding
import com.example.myapplication.ui.fragments.FViewModel
import com.example.myapplication.ui.fragments.JokesListFragment
import com.example.myapplication.ui.joke_details.JokeDetailsActivity
import com.example.myapplication.ui.joke_list.recycler.JokeAdapters.JokeAdapterForActivity

class JokesActivity : AppCompatActivity() {

    private val adapter = JokeAdapterForActivity {
        startActivity(JokeDetailsActivity.getInstance(this, it))
    }
    private val jokeGenerator = JokeGenerator
    private lateinit var binding: ActivityJokesBinding

    private val viewModel: FViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        //supportFragmentManager.fragmentFactory = CustomFragmentFactory()
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_jokes)
        binding = ActivityJokesBinding.inflate(layoutInflater)
        setContentView(binding.root)


        //createRWList(7)
        if (savedInstanceState == null) {
            openFragment()
        }
    }

    private fun openFragment() {
        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_container_view, JokesListFragment.newInstance())
            .addToBackStack(null)
            .commit()
    }

//    private fun createRWList(jokesCount: Int) {
//        binding.rw.adapter = adapter
//        adapter.setNewData(jokeGenerator.generateJokesList(jokesCount))
//    }
}