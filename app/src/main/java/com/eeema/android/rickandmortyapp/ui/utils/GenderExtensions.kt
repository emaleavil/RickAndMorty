package com.eeema.android.rickandmortyapp.ui.utils

import com.eeema.android.data.model.Gender
import com.eeema.android.rickandmortyapp.R

fun Gender.asStringResource(): Int {
    return when (this) {
        is Gender.Genderless -> R.string.gender_genderless
        is Gender.Female -> R.string.gender_female
        is Gender.Male -> R.string.gender_male
        is Gender.Unknown -> R.string.gender_unknown
    }
}
