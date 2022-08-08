package com.eeema.android.data.model.deserializer

import com.eeema.android.data.constants.JsonKeys
import com.eeema.android.data.extensions.getGender
import com.eeema.android.data.extensions.getStatus
import com.eeema.android.data.extensions.intOrThrow
import com.eeema.android.data.extensions.stringOrThrow
import com.eeema.android.data.model.Character
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
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
}
