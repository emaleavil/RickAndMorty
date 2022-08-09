package com.eeema.android.rickandmortyapp.ui.characters

import com.eeema.android.data.model.Character

sealed interface CharactersState {
    object Initial : CharactersState
    object Loading : CharactersState
    data class Success(val data: List<Character>) : CharactersState
    object Failed : CharactersState
}
