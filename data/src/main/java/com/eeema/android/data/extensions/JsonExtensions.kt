package com.eeema.android.data.extensions

import com.eeema.android.data.model.error.Error
import com.google.gson.JsonArray
import com.google.gson.JsonObject

fun JsonObject.getJsonObject(key: String): JsonObject? =
    if (has(key)) {
        get(key).asJsonObject
    } else null

fun JsonObject.getJsonArray(
    key: String
): JsonArray? = if (has(key)) {
    get(key).asJsonArray
} else null

fun JsonObject?.stringOrNull(
    key: String
) = runCatching {
    this?.get(key)?.asString
}.getOrNull()

fun JsonObject?.stringOrThrow(
    key: String,
    error: Throwable = Error.InvalidJsonFormat
): String = runCatching {
    this!!.get(key).asString
}.getOrElse { throw error }

fun JsonObject?.intOrThrow(
    key: String,
    error: Throwable = Error.InvalidJsonFormat
): Int = runCatching {
    this!!.get(key).asInt
}.getOrElse { throw error }
