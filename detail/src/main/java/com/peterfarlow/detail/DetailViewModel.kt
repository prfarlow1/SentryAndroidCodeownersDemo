package com.peterfarlow.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.peterfarlow.core.data.apiKey
import com.peterfarlow.core.models.Cat
import com.peterfarlow.core.service.HttpClient
import com.peterfarlow.presentation.DetailViewState
import io.ktor.util.reflect.typeInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DetailViewModel(
    private val httpClient: HttpClient,
) : ViewModel() {

    private val _viewState = MutableStateFlow(DetailViewState(Cat()))
    val viewState = _viewState.asStateFlow()

    init {
        viewModelScope.launch {
            val cat = httpClient.get<Cat>("https://api.thecatapi.com/v1/images/search?limit=40&has_breeds=1", typeInfo = typeInfo<Cat>()) ?: Cat()
            _viewState.value = DetailViewState(cat)
        }
    }

    companion object Factory : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
            return DetailViewModel(httpClient = HttpClient(apiKey = apiKey)) as T
        }
    }
}
