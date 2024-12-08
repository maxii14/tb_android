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
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.myapplication.R
import com.example.myapplication.data.Joke
import com.example.myapplication.data.JokeGenerator
import com.example.myapplication.databinding.FragmentJokesListBinding
import com.example.myapplication.ui.joke_list.recycler.JokeAdapters.JokeAdapterForFragment
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch


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
    private val DIRECTION_UP = 1


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //jokeGenerator = ViewModelProvider(this)[JokeGenerator::class.java]
        //clearCustomAndCachedJokes()
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

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun getAndPushDataToRecycler() {
        lifecycleScope.launch {
            bindingFragmentList.progressBar.visibility = ProgressBar.VISIBLE
            bindingFragmentList.rw.adapter = adapter

            // изменяем внутреннее состояние StateFlow и ждём завершения
            val loadedJokes = listOf(
                launch { jokeGenerator.loadAllCustomJokes() },
                launch { jokeGenerator.loadAllCachedJokes() }
            )
            loadedJokes.joinAll()

            // комбинируем, чтобы получить и отобразить данные из 2х таблиц
            combine(
                jokeGenerator.customJokesFlow,
                jokeGenerator.cachedJokesFlow
            ) { customJokes, cachedJokes ->
                customJokes to cachedJokes
            }.flatMapLatest { (customJokes, cachedJokes) ->
                val apiJokes = getApiJokes()
                if (apiJokes.isEmpty()) {
                    Toast.makeText(requireActivity(), "Данные из кэша, нет подключения к сети.", Toast.LENGTH_SHORT).show()
                    flowOf(customJokes + cachedJokes)
                }
                else {
                    flowOf(customJokes + apiJokes)
                }
            }.collect { jokesList ->
                println("BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB")
                if (jokesList.isEmpty()) {
                    bindingFragmentList.tvNoJokes.visibility = View.VISIBLE
                } else {
                    if (jokesList.filter { it.fromApi }.isEmpty()) {
                        Toast.makeText(requireActivity(), "Кэш пуст.", Toast.LENGTH_SHORT).show()
                    }
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

    private fun initListeners() {
        bindingFragmentList.btAddJoke.setOnClickListener {
            openFragment()
        }

        bindingFragmentList.rw.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            @SuppressLint("NotifyDataSetChanged")
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if (!recyclerView.canScrollVertically(DIRECTION_UP) && isInternetAvailable()) {
                    if (!coroutineIsRunning) {
                        try {
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
                            }
                        }
                        catch (e: Exception) {
                            println("Error: $e")
                            Toast.makeText(requireActivity(), "Ошибка при загрузке данных", Toast.LENGTH_SHORT).show()
                        }
                        finally {
                            bindingFragmentList.progressBar.visibility = ProgressBar.GONE
                            coroutineIsRunning = false
                        }
                    }
                }
            }
        })
    }

    private suspend fun getApiJokes(): List<Joke> {
        var apiJokes: List<Joke> = emptyList()
        if (isInternetAvailable()) {
            try {
                apiJokes = jokeGenerator.getInitialApiJokes()
            } catch (e: Exception) {
                println("Error: $e")
            }
        }
        return apiJokes
    }

    private fun clearCustomAndCachedJokes() {
        lifecycleScope.launch {
            jokeGenerator.clearCustomJokes()
            jokeGenerator.clearAllCachedJokes()
        }
    }
}

