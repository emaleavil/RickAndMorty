package com.eeema.android.rickandmortyapp.ui.characters

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eeema.android.data.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@HiltViewModel
class CharactersViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    private val _state = MutableStateFlow<CharactersState>(CharactersState.Initial)
    val state: StateFlow<CharactersState>
        get() = _state

    init {
        _state.value = CharactersState.Loading
        loadItems(1)
    }

    fun loadItems(index: Int? = null) {
        if (index != null) {
            viewModelScope.launch {
                val result = withContext(Dispatchers.IO) { repository.characters(index) }
                _state.value = result.fold(
                    onSuccess = {
                        when (it.data.isNotEmpty()) {
                            true -> {
                                val shownState = state.value as? CharactersState.Success
                                val shownData = shownState?.data ?: emptyList()
                                CharactersState.Success(shownData.plus(it.data), it.nextPageIndex)
                            }
                            false -> CharactersState.Initial
                        }
                    },
                    onFailure = { CharactersState.Failed }
                )
            }
        }
    }
}
