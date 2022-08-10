package com.eeema.android.rickandmortyapp.ui.utils

import com.eeema.android.data.model.Status
import com.eeema.android.rickandmortyapp.R

fun Status.toImageResource() = when (this) {
    is Status.Alive -> R.drawable.heartbeat
    is Status.Dead -> R.drawable.tombstone
    else -> R.drawable.unknown
}

fun Status.asStringResource(): Int {
    return when (this) {
        is Status.Alive -> R.string.status_alive
        is Status.Dead -> R.string.status_dead
        else -> R.string.status_unknown
    }
}
