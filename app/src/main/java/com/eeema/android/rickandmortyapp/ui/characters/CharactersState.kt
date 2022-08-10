package com.eeema.android.rickandmortyapp.ui.characters

import com.eeema.android.data.model.Character

sealed interface CharactersState {
    object Initial : CharactersState
    object Loading : CharactersState
    data class Success(val data: List<Character>, val nextPage: Int?) : CharactersState
    object Failed : CharactersState
}
