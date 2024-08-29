package com.peterfarlow.sentryandroidcodeownersdemo.domain

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.peterfarlow.core.models.Cat
import com.peterfarlow.core.service.HttpClient
import com.peterfarlow.sentryandroidcodeownersdemo.BuildConfig
import io.ktor.util.reflect.typeInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import kotlin.reflect.KClass

class MainViewModel(
    private val httpClient: HttpClient,
) : ViewModel() {
    fun onCatClicked(id: String) {
        _viewState.update { viewState ->
            val cat = viewState.cats.find { it.id == id } ?: throw IllegalStateException("no")
            val index = viewState.cats.indexOf(cat)
            val newCat = cat.copy(selected = !cat.selected)
            val newList = viewState.cats.toMutableList().apply {
                set(index, newCat)
            }
            viewState.copy(cats = newList.toList())
        }
    }

    private val _viewState = MutableStateFlow(ViewState(isLoading = true))
    val viewState = _viewState.asStateFlow()

    init {
        viewModelScope.launch {
            val cats = httpClient.get<List<Cat>?>("https://api.thecatapi.com/v1/images/search?limit=40&has_breeds=1", typeInfo = typeInfo<List<Cat>>()) ?: emptyList()
            val cats2 = cats.map { it.copy(id = UUID.randomUUID().toString()) }
            _viewState.value = ViewState(cats = cats2, isError = cats.isEmpty())
        }
    }

    companion object Factory : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: KClass<T>, extras: CreationExtras): T {
            return MainViewModel(httpClient = HttpClient(apiKey = BuildConfig.catApiKey)) as T
        }
    }
}

data class ViewState(
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val cats: List<Cat> = emptyList()
)


