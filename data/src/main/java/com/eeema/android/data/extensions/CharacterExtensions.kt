package com.eeema.android.data.extensions

import com.eeema.android.data.constants.JsonKeys
import com.eeema.android.data.model.Character
import com.eeema.android.data.model.Gender
import com.eeema.android.data.model.Status
import com.eeema.android.data.model.error.Error
import com.eeema.android.data.model.local.DatabaseCharacter
import com.google.gson.JsonObject

fun JsonObject.getStatus(): Status = try {
    get(JsonKeys.status).asString.toStatus()
} catch (e: Throwable) {
    throw Error.InvalidJsonFormat
}

fun JsonObject.getGender(): Gender = try {
    get(JsonKeys.gender).asString.toGender()
} catch (e: Throwable) {
    throw Error.InvalidJsonFormat
}

fun Character.stringGender() = when (gender) {
    Gender.Male -> JsonKeys.male
    Gender.Female -> JsonKeys.female
    Gender.Genderless -> JsonKeys.genderless
    else -> JsonKeys.unknown
}

fun Character.stringStatus() = when (status) {
    Status.Alived -> JsonKeys.alive
    Status.Dead -> JsonKeys.dead
    else -> JsonKeys.unknown
}

fun DatabaseCharacter.toStatus() = status.toStatus()

fun DatabaseCharacter.toGender() = gender.toGender()

fun String.toGender() = when (this.lowercase()) {
    JsonKeys.male -> Gender.Male
    JsonKeys.female -> Gender.Female
    JsonKeys.genderless -> Gender.Genderless
    else -> Gender.Unknown
}

fun String.toStatus() = when (this.lowercase()) {
    JsonKeys.alive -> Status.Alived
    JsonKeys.dead -> Status.Dead
    else -> Status.Unknown
}
