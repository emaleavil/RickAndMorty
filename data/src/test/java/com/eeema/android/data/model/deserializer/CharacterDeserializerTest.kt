package com.eeema.android.data.model.deserializer

import com.eeema.android.data.model.Character
import com.eeema.android.data.model.Gender
import com.eeema.android.data.model.Status
import com.eeema.android.data.model.error.Error
import com.google.common.truth.Truth.assertThat
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import org.junit.Before
import org.junit.Test

class CharacterDeserializerTest {

    private lateinit var gson: Gson

    @Before
    fun setUp() {
        gson = GsonBuilder()
            .registerTypeAdapter(Character::class.java, CharacterDeserializer())
            .create()
    }

    @Test(expected = Error.InvalidJsonFormat::class)
    fun `Given invalid character format Then deserializer should throw an error`() {
        val result: Character = gson.fromJson(
            """{"invalidKey": "invalidValue"}""",
            object : TypeToken<Character>() {}.type
        )
    }

    @Test(expected = Error.InvalidJsonFormat::class)
    fun `Given character without name Then deserializer should throw InvalidJsonFormat`() {
        val result: Character = gson.fromJson(
            """{"id": 1}""",
            object : TypeToken<Character>() {}.type
        )
    }

    @Test(expected = Error.InvalidJsonFormat::class)
    fun `Given character without status Then deserializer should throw InvalidJsonFormat`() {
        val result: Character = gson.fromJson(
            """{"id": 1, "name": "Character"}""",
            object : TypeToken<Character>() {}.type
        )
    }

    @Test(expected = Error.InvalidJsonFormat::class)
    fun `Given character without species Then deserializer should throw InvalidJsonFormat`() {
        val result: Character = gson.fromJson(
            """{"id": 1, "name": "Character", "status" : "Alive" }""",
            object : TypeToken<Character>() {}.type
        )
    }

    @Test(expected = Error.InvalidJsonFormat::class)
    fun `Given character without gender Then deserializer should throw InvalidJsonFormat`() {
        val result: Character = gson.fromJson(
            """{"id": 1, "name": "Character", "status" : "Alive", "species": "Human"}""",
            object : TypeToken<Character>() {}.type
        )
    }

    @Test(expected = Error.InvalidJsonFormat::class)
    fun `Given character without image Then deserializer should throw InvalidJsonFormat`() {
        val result: Character = gson.fromJson(
            """{
                |"id": 1, 
                |"name": "Character", 
                |"status" : "Alive", 
                |"species": "Human",
                |"gender": "Female"
            |}
            """.trimMargin(),
            object : TypeToken<Character>() {}.type
        )
    }

    @Test(expected = Error.InvalidJsonFormat::class)
    fun `Given character without url Then deserializer should throw InvalidJsonFormat`() {
        val result: Character = gson.fromJson(
            """{
                |"id": 1, 
                |"name": "Character", 
                |"status" : "Alive", 
                |"species": "Human",
                |"gender": "Female",
                |"image": "http://image.url"
            |}
            """.trimMargin(),
            object : TypeToken<Character>() {}.type
        )
    }

    @Test
    fun `Given character Then deserializer should parse successfully`() {
        val result: Character = gson.fromJson(
            """{
                |"id": 1, 
                |"name": "Character", 
                |"status" : "Alive", 
                |"species": "Human",
                |"gender": "Female",
                |"image": "https://image.url",
                |"url": "https://character.url"
            |}
            """.trimMargin(),
            object : TypeToken<Character>() {}.type
        )

        assertThat(result.id).isEqualTo(1)
        assertThat(result.name).isEqualTo("Character")
        assertThat(result.status).isEqualTo(Status.Alive)
        assertThat(result.species).isEqualTo("Human")
        assertThat(result.gender).isEqualTo(Gender.Female)
        assertThat(result.image).isEqualTo("https://image.url")
        assertThat(result.url).isEqualTo("https://character.url")
    }

    @Test
    fun `Given genderless character Then genderless gender should be retrieved`() {
        val result: Character = gson.fromJson(
            """{
                |"id": 1, 
                |"name": "Character", 
                |"status" : "Alive", 
                |"species": "Human",
                |"gender": "GeNderLess",
                |"image": "https://image.url",
                |"url": "https://character.url"
            |}
            """.trimMargin(),
            object : TypeToken<Character>() {}.type
        )

        assertThat(result.gender).isEqualTo(Gender.Genderless)
    }

    @Test
    fun `Given male character Then male gender should be retrieved`() {
        val result: Character = gson.fromJson(
            """{
                |"id": 1, 
                |"name": "Character", 
                |"status" : "Alive", 
                |"species": "Human",
                |"gender": "MALE",
                |"image": "https://image.url",
                |"url": "https://character.url"
            |}
            """.trimMargin(),
            object : TypeToken<Character>() {}.type
        )

        assertThat(result.gender).isEqualTo(Gender.Male)
    }

    @Test
    fun `Given unknown character Then unknown gender should be retrieved`() {
        val result: Character = gson.fromJson(
            """{
                |"id": 1, 
                |"name": "Character", 
                |"status" : "Alive", 
                |"species": "Human",
                |"gender": "",
                |"image": "https://image.url",
                |"url": "https://character.url"
            |}
            """.trimMargin(),
            object : TypeToken<Character>() {}.type
        )

        assertThat(result.gender).isEqualTo(Gender.Unknown)
    }

    @Test
    fun `Given dead character Then dead status should be retrieved`() {
        val result: Character = gson.fromJson(
            """{
                |"id": 1, 
                |"name": "Character", 
                |"status" : "Dead", 
                |"species": "Human",
                |"gender": "",
                |"image": "https://image.url",
                |"url": "https://character.url"
            |}
            """.trimMargin(),
            object : TypeToken<Character>() {}.type
        )

        assertThat(result.status).isEqualTo(Status.Dead)
    }

    @Test
    fun `Given unknown character Then unknown status should be retrieved`() {
        val result: Character = gson.fromJson(
            """{
                |"id": 1, 
                |"name": "Character", 
                |"status" : "invalid", 
                |"species": "Human",
                |"gender": "",
                |"image": "https://image.url",
                |"url": "https://character.url"
            |}
            """.trimMargin(),
            object : TypeToken<Character>() {}.type
        )

        assertThat(result.status).isEqualTo(Status.Unknown)
    }
}
