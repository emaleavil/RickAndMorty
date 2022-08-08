package com.eeema.android.data.model

sealed interface Gender {
    object Female : Gender
    object Male : Gender
    object Genderless : Gender
    object Unknown : Gender
}
