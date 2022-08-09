package com.eeema.android.rickandmortyapp.ui.characters

import com.eeema.android.data.model.Character

sealed interface ViewState {
    object Initial : ViewState
    object Loading : ViewState
    data class Success(val data: List<Character>) : ViewState
    object Failed : ViewState
}
