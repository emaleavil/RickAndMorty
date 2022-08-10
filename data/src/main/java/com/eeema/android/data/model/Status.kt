package com.eeema.android.data.model

sealed interface Status {
    object Alive : Status
    object Unknown : Status
    object Dead : Status
}
