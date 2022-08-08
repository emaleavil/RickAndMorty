package com.eeema.android.data.model.deserializer

import com.eeema.android.data.model.Character
import com.eeema.android.data.model.Gender
import com.eeema.android.data.model.Page
import com.eeema.android.data.model.Status
import com.eeema.android.data.utils.FileExtensions
import com.google.common.truth.Truth.assertThat
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import org.junit.Test

class PageDeserializerTest {

    private val gson = GsonBuilder()
        .registerTypeAdapter(
            TypeToken.getParameterized(
                TypeToken.get(Page::class.java).type,
                TypeToken.get(Character::class.java).type
            ).type,
            PageDeserializer(Character::class.java)
        )
        .registerTypeAdapter(Character::class.java, CharacterDeserializer())
        .create()

    @Test
    fun `Given empty json Then deserializer should returns empty Page`() {
        val result: Page<Character> = gson.fromJson(
            """{}""",
            TypeToken.getParameterized(
                TypeToken.get(Page::class.java).type,
                TypeToken.get(Character::class.java).type
            ).type
        )
        assertThat(result).isEqualTo(Page<Character>(data = emptyList()))
    }

    @Test
    fun `Given invalid json Then deserializer should returns empty Page`() {
        val result: Page<Character> = gson.fromJson(
            """{"invalidKey":"invalidValue"}""",
            TypeToken.getParameterized(
                TypeToken.get(Page::class.java).type,
                TypeToken.get(Character::class.java).type
            ).type
        )
        assertThat(result).isEqualTo(Page<Character>(data = emptyList()))
    }

    @Test
    fun `Given valid json page Then deserializer should parse page successfully`() {
        val result: Page<Character> = gson.fromJson(
            FileExtensions.readFileFromResources("characters.json"),
            TypeToken.getParameterized(
                TypeToken.get(Page::class.java).type,
                TypeToken.get(Character::class.java).type
            ).type
        )
        assertThat(result).isEqualTo(
            Page(
                nextPageUrl = "https://rickandmortyapi.com/api/character?page=2",
                prevPageUrl = null,
                data = listOf(
                    Character(
                        1,
                        "Rick Sanchez",
                        Status.Alived,
                        "Human",
                        Gender.Male,
                        "https://rickandmortyapi.com/api/character/avatar/1.jpeg",
                        "https://rickandmortyapi.com/api/character/1"
                    )
                )
            )
        )
    }
}
