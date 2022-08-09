package com.eeema.android.rickandmortyapp.ui.utils

import com.eeema.android.data.model.Status
import com.eeema.android.rickandmortyapp.R

fun Status.toImageResource() = when (this) {
    is Status.Alived -> R.drawable.heartbeat
    is Status.Dead -> R.drawable.tombstone
    else -> R.drawable.unknown
}
