package com.example.myapplication.presentation.joke_list.recycler.Util
import androidx.recyclerview.widget.DiffUtil
import com.example.myapplication.domain.entity.Joke

class JokeDiffUtilCallback(
    private val oldList: List<Joke>,
    private val newList: List<Joke>,
): DiffUtil.Callback() {
    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].title == newList[newItemPosition].title
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }

}