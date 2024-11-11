package com.example.myapplication.ui.joke_list.recycler.JokeAdapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.data.Joke
import com.example.myapplication.databinding.JokeItemBinding
import com.example.myapplication.ui.joke_list.recycler.JokeViewHolder
import com.example.myapplication.ui.joke_list.recycler.Util.JokeDiffUtilCallback

class JokeAdapterForFragment(
    private val clickListner: (Int, Joke) -> Unit,
): RecyclerView.Adapter<JokeViewHolder>() {

    private var data = emptyList<Joke>()

    fun setNewData(newData: List<Joke>) {
        val diffUtilCallback = JokeDiffUtilCallback(data, newData)
        val newDiff = DiffUtil.calculateDiff(diffUtilCallback)
        data = newData
        newDiff.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JokeViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = JokeItemBinding.inflate(inflater)

        return JokeViewHolder(binding).apply {
            binding.root.setOnClickListener {
                handleJokeClick(adapterPosition)
            }
        }
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: JokeViewHolder, position: Int) {
        holder.bind(data[position])
    }

    private fun handleJokeClick(position: Int) {
        if (position != RecyclerView.NO_POSITION) {
            (data[position] as? Joke)?.let { joke ->
                clickListner(position, joke)
            }
        }
    }

}