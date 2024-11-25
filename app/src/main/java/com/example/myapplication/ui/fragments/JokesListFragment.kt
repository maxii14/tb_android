package com.example.myapplication.ui.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.myapplication.R
import com.example.myapplication.data.Joke
import com.example.myapplication.data.JokeGenerator
import com.example.myapplication.databinding.FragmentJokesListBinding
import com.example.myapplication.ui.joke_list.recycler.JokeAdapters.JokeAdapterForFragment
import kotlinx.coroutines.launch


class JokesListFragment : Fragment(R.layout.fragment_jokes_list) {


    private val adapter = JokeAdapterForFragment { _, joke ->
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(
                R.id.fragment_container_view,
                JokesFragment.newInstance(joke.title, joke.category, joke.answer, "Из сети: ${if (joke.fromApi) "да" else "нет"}")
            )
            .addToBackStack(null)
            .commit()
    }
    private val jokeGenerator = JokeGenerator

    private val bindingFragmentList: FragmentJokesListBinding by viewBinding(FragmentJokesListBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getAndPushDataToRecycler()
        
        bindingFragmentList.btAddJoke.setOnClickListener {
            openFragment()
        }
        bindingFragmentList.rw.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            @SuppressLint("NotifyDataSetChanged")
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if (!recyclerView.canScrollVertically(3)) {
                    lifecycleScope.launch {
                        bindingFragmentList.progressBar.visibility = ProgressBar.VISIBLE
                        jokeGenerator.loadMoreApiJokes()
                        val jokes = mutableListOf<Joke>()
                        jokes.addAll(jokeGenerator.getCustomJokes())
                        jokes.addAll(jokeGenerator.getInitialApiJokes())
                        adapter.setNewData(jokes)
                        bindingFragmentList.progressBar.visibility = ProgressBar.GONE
                    }
                }
            }
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_jokes_list, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    companion object {
        fun newInstance() =
            JokesListFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }

    private fun getAndPushDataToRecycler() {
        lifecycleScope.launch {
            bindingFragmentList.rw.adapter = adapter

            val jokes = mutableListOf<Joke>()
            jokes.addAll(jokeGenerator.getCustomJokes())
            jokes.addAll(jokeGenerator.getInitialApiJokes())

            if (jokes.isEmpty()) {
                bindingFragmentList.tvNoJokes.visibility = View.VISIBLE
            } else {
                bindingFragmentList.tvNoJokes.visibility = View.GONE
                adapter.setNewData(jokes)
            }
            bindingFragmentList.progressBar.visibility = ProgressBar.GONE
        }
    }

    private fun openFragment() {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container_view, AddJokeFragment.newInstance())
            .addToBackStack(null)
            .commit()
    }
}