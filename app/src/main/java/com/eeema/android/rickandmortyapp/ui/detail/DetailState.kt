package com.eeema.android.rickandmortyapp.ui.detail

import com.eeema.android.data.model.Character

sealed interface DetailState {
    object Loading : DetailState
    data class Data(val data: Character) : DetailState
    object Failed : DetailState
}
