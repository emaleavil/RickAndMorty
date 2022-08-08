package com.eeema.android.data.model.deserializer

import com.eeema.android.data.constants.JsonKeys
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
            character.intOrThrow(JsonKeys.id),
            character.stringOrThrow(JsonKeys.name),
            character.getStatus(),
            character.stringOrThrow(JsonKeys.species),
            character.getGender(),
            character.stringOrThrow(JsonKeys.image),
            character.stringOrThrow(JsonKeys.url)
        )
    }

    private fun JsonObject.getStatus(): Status = try {
        when (get(JsonKeys.status).asString.lowercase()) {
            JsonKeys.alive -> Status.Alived
            JsonKeys.dead -> Status.Dead
            else -> Status.Unknown
        }
    } catch (e: Throwable) {
        throw Error.InvalidJsonFormat
    }

    private fun JsonObject.getGender(): Gender = try {
        when (get(JsonKeys.gender).asString.lowercase()) {
            JsonKeys.male -> Gender.Male
            JsonKeys.female -> Gender.Female
            JsonKeys.genderless -> Gender.Genderless
            else -> Gender.Unknown
        }
    } catch (e: Throwable) {
        throw Error.InvalidJsonFormat
    }
}
