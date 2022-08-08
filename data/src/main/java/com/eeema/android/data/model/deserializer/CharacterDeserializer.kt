package com.eeema.android.data.model.deserializer

import com.eeema.android.data.extensions.intOrThrow
import com.eeema.android.data.extensions.stringOrThrow
import com.eeema.android.data.model.Character
import com.eeema.android.data.model.Gender
import com.eeema.android.data.model.Status
import com.eeema.android.data.model.error.Error
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import java.lang.reflect.Type

class CharacterDeserializer : JsonDeserializer<Character> {

    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): Character {
        val character = json.asJsonObject
        return Character(
            character.intOrThrow("id"),
            character.stringOrThrow("name"),
            character.getStatus(),
            character.stringOrThrow("species"),
            character.getGender(),
            character.stringOrThrow("image"),
            character.stringOrThrow("url")
        )
    }

    private fun JsonObject.getStatus(): Status = try {
        when (get("status").asString.lowercase()) {
            "alive" -> Status.Alived
            "dead" -> Status.Dead
            else -> Status.Unknown
        }
    } catch (e: Throwable) {
        throw Error.InvalidJsonFormat
    }

    private fun JsonObject.getGender(): Gender = try {
        when (get("gender").asString.lowercase()) {
            "male" -> Gender.Male
            "female" -> Gender.Female
            "genderless" -> Gender.Genderless
            else -> Gender.Unknown
        }
    } catch (e: Throwable) {
        throw Error.InvalidJsonFormat
    }
}
