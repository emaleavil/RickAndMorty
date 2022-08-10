package com.eeema.android.data.model.error

import kotlin.Error

sealed class Error(cause: String) : Throwable(cause) {
    object InvalidJsonFormat : Error("Invalid json format")
    object CharactersNotRetrieved : Error("Characters not retrieved")
}
