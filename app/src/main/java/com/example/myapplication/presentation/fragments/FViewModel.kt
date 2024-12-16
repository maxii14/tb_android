package com.example.myapplication.presentation.fragments

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.domain.entity.Joke

class FViewModel : ViewModel() {

    private val mutableSelectedItem = MutableLiveData<Joke>()
    val selectedItem: LiveData<Joke>
        get() = mutableSelectedItem

    fun selectItem(item: Joke) {
        mutableSelectedItem.value = item
    }
}