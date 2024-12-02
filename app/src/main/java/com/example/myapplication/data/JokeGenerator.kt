package com.example.myapplication.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.api.RetrofitInstance
import com.example.myapplication.data.db.AppDB
import com.example.myapplication.data.db.repo.CachedJokesRepository
import com.example.myapplication.data.db.repo.CustomJokesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

object JokeGenerator : ViewModel() {

    val data = mutableListOf<Joke>()
    private val runtimeData = mutableListOf<Joke>()

    private val cachedJokesRepository: CachedJokesRepository by lazy {
        CachedJokesRepository(
            AppDB.INSTANCE.cachedJokeDao()
        )
    }
    private val customJokesRepository: CustomJokesRepository by lazy {
        CustomJokesRepository(
            AppDB.INSTANCE.jokeDao()
        )
    }
    private val _customJokesFlow = MutableStateFlow<List<Joke>>(emptyList())
    val customJokesFlow: StateFlow<List<Joke>> get() = _customJokesFlow
    private val _cachedJokesFlow = MutableStateFlow<List<Joke>>(emptyList())
    val cachedJokesFlow: StateFlow<List<Joke>> get() = _cachedJokesFlow

    private val jokeSet = mutableSetOf(
        Joke("1", "Какая самая дорогая чашка кофе?",
            "Про кофе",
            "Самая дорогая в мире чашка кофе - это растворимый \"Нескафе\" с двумя кусочками сахара, пролитый на клавиатуру работающего ноутбука"
        ),
        Joke("2", "Чем юмор отличается от сатиры?",
            "Про юмор",
            "Юмор — это когда страшно хочется смеяться, а сатира — когда хочется смеяться, но страшно."
        ),
        Joke("3","Случай в лифте:",
            "Про жизнь",
            "Надпись в лифте: \"Все бабы - дуры!\". Ниже, другим почерком: \"Не все!\". Еще ниже: \"Дамы, не ссорьтесь!\"."
        ),
        Joke("4","В чём проблема восьмичасового сна?",
            "Про жизнь",
            "Проблема полноценного восьмичасового сна в том, что вы просыпаетесь с ясной головой и полным пониманием ужаса происходящего..."
        ),
        Joke("5","Как познакомились твои родители?",
            "Про жизнь",
            "Мама хотела мальчика, а папа - девочку. Так они и познакомились..."
        ),
        Joke("6","Чем хороши дети-пессимисты?",
            "Про жизнь",
            "Дети-пессимисты нужны, чтобы в старости было кому подать вам наполовину пустой стакан воды."
        ),
        Joke("7","Что такое старость?",
            "Про возраст",
            "Старость - это когда видишь, как малолетки смеются над новыми малолетками."
        ),
        Joke("8","Настало время переходить с летних колёс на зимние!",
            "Про колёса",
            "Машины у меня нет, я про антидепрессанты."
        ),
        Joke("9","Кто является невезучим?",
            "Про везение",
            "Невезучим является тот, кто не успел в круглосуточный магазин."
        ),
        Joke("10","Легенда:",
            "Легенды",
            "Существует легенда, что есть такие женщины, которые открывая шкаф знают, что наденут."
        ),
    )

    suspend fun getInitialApiJokes(): MutableList<Joke> {
        if (runtimeData.isEmpty() && isInternetAvailable()) {
            runtimeData.addAll(loadJokesFromNetwork(10))
        }

        return runtimeData
    }

    suspend fun loadMoreApiJokes() {
        runtimeData.addAll(loadJokesFromNetwork(10))
    }

    fun getCustomJokes(): MutableList<Joke> {
        return data
    }

    fun addCustomJoke(joke: Joke) {
        data.add(joke)
        viewModelScope.launch {
            customJokesRepository.addJoke(joke)
        }
    }

    fun addJokesToCache(jokes: List<Joke>) {
        val cachedJokes = convertToDatabaseJokes(jokes)
        viewModelScope.launch {
            cachedJokesRepository.addJokesToCache(cachedJokes)
        }
    }

    private suspend fun loadJokesFromNetwork(count: Int): List<Joke> {
        return RetrofitInstance.api.getRandomJokes(amount = count).jokes
    }

    private fun isInternetAvailable(): Boolean {
        return Runtime.getRuntime().exec("ping -c 1 google.com").waitFor() == 0
    }

    fun loadAllCustomJokes() {
        viewModelScope.launch {
            customJokesRepository.getAllJokes().collect {
                _customJokesFlow.value = it
            }
        }
    }

    fun loadAllCachedJokes() {
        viewModelScope.launch {
            cachedJokesRepository.clearOldCache()
            cachedJokesRepository.getAllCachedJokes().collect {
                _cachedJokesFlow.value = convertToAppJokes(it)
            }
        }
    }

    private fun convertToDatabaseJokes(jokes: List<Joke>): List<CachedJoke> {
        return jokes.map { joke ->
            CachedJoke(
                id = joke.id,
                title = joke.title,
                category = joke.category,
                answer = joke.answer,
                fromApi = joke.fromApi,
                timeCreated = System.currentTimeMillis()
            )
        }
    }

    private fun convertToAppJokes(jokes: List<CachedJoke>): List<Joke> {
        return jokes.map { joke ->
            Joke(
                id = joke.id,
                title = joke.title,
                category = joke.category,
                answer = joke.answer,
                fromApi = joke.fromApi,
            )
        }
    }
}