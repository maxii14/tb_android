package com.example.myapplication.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.data.Joke
import com.example.myapplication.databinding.JokeItemBinding

class JokeAdapter(): RecyclerView.Adapter<JokeViewHolder>() {

    private var data = emptyList<Joke>()

    fun setNewData(newData: List<Joke>) {
        data = newData
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JokeViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = JokeItemBinding.inflate(inflater)

        return JokeViewHolder(binding)
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: JokeViewHolder, position: Int) {
        holder.bind(data[position])
    }

}