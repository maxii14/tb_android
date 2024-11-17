package com.example.myapplication.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.myapplication.R
import com.example.myapplication.data.JokeGenerator
import com.example.myapplication.databinding.FragmentJokesListBinding
import com.example.myapplication.ui.joke_list.recycler.JokeAdapters.JokeAdapterForFragment


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
        createRWList(7)
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

    private fun createRWList(jokesCount: Int) {
        bindingFragmentList.rw.adapter = adapter
        adapter.setNewData(jokeGenerator.generateJokesList(jokesCount))
    }
}