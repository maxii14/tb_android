package com.example.myapplication.ui.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.myapplication.R
import com.example.myapplication.data.Joke
import com.example.myapplication.data.JokeGenerator
import com.example.myapplication.databinding.FragmentJokesListBinding
import com.example.myapplication.ui.joke_list.recycler.JokeAdapters.JokeAdapterForFragment
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.toCollection
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import okhttp3.internal.wait


class JokesListFragment : Fragment(R.layout.fragment_jokes_list) {

    private var coroutineIsRunning = false

    private val adapter = JokeAdapterForFragment { _, joke ->
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(
                R.id.fragment_container_view,
                JokesFragment.newInstance(joke.title, joke.category, joke.answer, "Из сети: ${if (joke.fromApi) "да" else "нет"}")
            )
            .addToBackStack(null)
            .commit()
    }
    //lateinit var jokeGenerator: JokeGenerator
    private val jokeGenerator: JokeGenerator by viewModels()
    private val bindingFragmentList: FragmentJokesListBinding by viewBinding(FragmentJokesListBinding::bind)
    private val LOAD_WHEN_LEFT = 1


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //jokeGenerator = ViewModelProvider(this)[JokeGenerator::class.java]
        initListeners()
        getAndPushDataToRecycler()
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
            bindingFragmentList.progressBar.visibility = ProgressBar.VISIBLE
            bindingFragmentList.rw.adapter = adapter

            jokeGenerator.loadAllCustomJokes()
            jokeGenerator.loadAllCachedJokes()
            jokeGenerator.customJokesFlow.collect { customJokes ->
                val jokesList = mutableListOf<Joke>()
                jokesList.addAll(customJokes)
                jokesList.addAll(getApiOrCachedJokes())
                if (jokesList.isEmpty()) {
                    Toast.makeText(requireActivity(), "Кэш пуст.", Toast.LENGTH_SHORT).show()
                    bindingFragmentList.tvNoJokes.visibility = View.VISIBLE
                } else {
                    bindingFragmentList.tvNoJokes.visibility = View.GONE
                    adapter.setNewData(jokesList)
                }
                bindingFragmentList.progressBar.visibility = ProgressBar.GONE
            }
        }
    }

    private fun openFragment() {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container_view, AddJokeFragment.newInstance())
            .addToBackStack(null)
            .commit()
    }

    private fun isInternetAvailable(): Boolean {
        return Runtime.getRuntime().exec("ping -c 1 google.com").waitFor() == 0
    }

    private suspend fun getApiOrCachedJokes(): List<Joke> {
        if (isInternetAvailable()) {
            val apiJokes: List<Joke> = jokeGenerator.getInitialApiJokes()
            jokeGenerator.addJokesToCache(apiJokes)
            return apiJokes
        }
        else {
            Toast.makeText(requireActivity(), "Данные из кэша, нет подключения к сети.", Toast.LENGTH_SHORT).show()
            return jokeGenerator.cachedJokesFlow.value
        }
    }

    private fun initListeners() {
        bindingFragmentList.btAddJoke.setOnClickListener {
            openFragment()
        }

        bindingFragmentList.rw.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            @SuppressLint("NotifyDataSetChanged")
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if (!recyclerView.canScrollVertically(LOAD_WHEN_LEFT) && isInternetAvailable()) {
                    if (!coroutineIsRunning) {
                        lifecycleScope.launch {
                            coroutineIsRunning = true
                            bindingFragmentList.progressBar.visibility = ProgressBar.VISIBLE
                            jokeGenerator.loadMoreApiJokes()
                            val jokes = mutableListOf<Joke>()
                            val apiJokes: List<Joke> = jokeGenerator.getInitialApiJokes()
                            jokeGenerator.addJokesToCache(apiJokes)
                            jokes.addAll(jokeGenerator.customJokesFlow.value)
                            jokes.addAll(apiJokes)
                            adapter.setNewData(jokes)
                            bindingFragmentList.progressBar.visibility = ProgressBar.GONE
                            coroutineIsRunning = false
                        }
                    }
                }
            }
        })
    }
}

