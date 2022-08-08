package com.eeema.android.data.model

sealed interface Status {
    object Alived : Status
    object Unknown : Status
    object Dead : Status
}
