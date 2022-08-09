package com.eeema.android.rickandmortyapp.ui.characters

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class CharactersViewModel : ViewModel() {

    private val _state = MutableStateFlow(ViewState.Initial)
    val state: StateFlow<ViewState>
        get() = _state
}
