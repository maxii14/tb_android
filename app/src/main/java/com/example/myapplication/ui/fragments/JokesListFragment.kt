package com.example.myapplication.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.replace
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.myapplication.R
import com.example.myapplication.data.JokeGenerator
import com.example.myapplication.databinding.FragmentJokesListBinding
import com.example.myapplication.ui.joke_list.recycler.JokeAdapters.JokeAdapterForFragment
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


class JokesListFragment : Fragment(R.layout.fragment_jokes_list) {


    private val adapter = JokeAdapterForFragment { _, joke ->
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(
                R.id.fragment_container_view,
                JokesFragment.newInstance(joke.title, joke.catogory, joke.answer)
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
            val jokes = jokeGenerator.getJokes()
            delay(2000L) // Строчка выше получает данные из БД
            if (jokes.size == 0) {
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