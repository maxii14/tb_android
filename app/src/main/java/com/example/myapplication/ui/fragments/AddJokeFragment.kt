package com.example.myapplication.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.myapplication.R
import com.example.myapplication.data.Joke
import com.example.myapplication.data.JokeGenerator
import com.example.myapplication.databinding.FragmentAddJokeBinding
import java.util.UUID


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class AddJokeFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    private val bindingFragmentAddJoke: FragmentAddJokeBinding by viewBinding(FragmentAddJokeBinding::bind)
    private val jokeGenerator = JokeGenerator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_joke, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setOnClickListenerOnAddJokeButton()
    }

    private fun setOnClickListenerOnAddJokeButton() {
        bindingFragmentAddJoke.btCommitAddJoke.setOnClickListener {
            val title = bindingFragmentAddJoke.etTitle.text.toString()
            val category = bindingFragmentAddJoke.etCategory.text.toString()
            val answer = bindingFragmentAddJoke.etAnswer.text.toString()
            if (title == "" || category == "" || answer == "") {
                Toast.makeText(requireActivity(), "Заполните все поля", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val joke = Joke(UUID.randomUUID().toString(), title, category, answer, false)
            jokeGenerator.addCustomJoke(joke)
            Toast.makeText(requireActivity(), "Шутка добавлена", Toast.LENGTH_SHORT).show()
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            AddJokeFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}