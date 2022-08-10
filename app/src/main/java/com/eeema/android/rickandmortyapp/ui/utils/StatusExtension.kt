package com.eeema.android.rickandmortyapp.ui.utils

import com.eeema.android.data.model.Status
import com.eeema.android.rickandmortyapp.R

fun Status.toImageResource() = when (this) {
    is Status.Alive -> R.drawable.ic_alive
    is Status.Dead -> R.drawable.ic_dead
    else -> R.drawable.ic_unknown
}

fun Status.asStringResource(): Int {
    return when (this) {
        is Status.Alive -> R.string.status_alive
        is Status.Dead -> R.string.status_dead
        else -> R.string.status_unknown
    }
}
