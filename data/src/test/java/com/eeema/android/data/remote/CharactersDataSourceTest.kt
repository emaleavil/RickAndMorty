@file:OptIn(ExperimentalCoroutinesApi::class)

package com.eeema.android.data.remote

import com.eeema.android.data.api.RickAndMortyApi
import com.eeema.android.data.datasource.remote.CharactersDataSource
import com.eeema.android.data.model.Character
import com.eeema.android.data.model.Gender
import com.eeema.android.data.model.Page
import com.eeema.android.data.model.Status
import com.eeema.android.data.model.deserializer.CharacterDeserializer
import com.eeema.android.data.model.deserializer.PageDeserializer
import com.eeema.android.data.model.error.Error
import com.eeema.android.data.server.MockWebServerRule
import com.eeema.android.data.server.ResponseStatus
import com.eeema.android.data.server.RetrofitRule
import com.eeema.android.data.server.RickAndMortyDispatcher
import com.google.common.truth.Truth.assertThat
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

class CharactersDataSourceTest {

    @get:Rule val serverRule = MockWebServerRule(RickAndMortyDispatcher())
    @get:Rule val retrofitRule = RetrofitRule(
        serverRule.url,
        RickAndMortyApi::class.java,
        mapOf(
            TypeToken.getParameterized(
                TypeToken.get(Page::class.java).type,
                TypeToken.get(Character::class.java).type
            ).type to PageDeserializer(Character::class.java),
            Character::class.java to CharacterDeserializer()
        )
    )

    private val sut: CharactersDataSource = CharactersDataSource(retrofitRule.service)

    @Test
    fun `request list of one characters should retrieve character when server response is 200`() =
        runTest {

            val result = sut.characters()

            assertThat(result.size).isEqualTo(1)
            assertThat(result.currentIndex).isEqualTo(1)
            assertThat(result.prevPageIndex).isNull()
            assertThat(result.nextPageIndex).isEqualTo(2)
            val character = result.getItem(0)
            assertThat(character.id).isEqualTo(1)
            assertThat(character.name).isEqualTo("Rick Sanchez")
            assertThat(character.status).isEqualTo(Status.Alived)
            assertThat(character.species).isEqualTo("Human")
            assertThat(character.gender).isEqualTo(Gender.Male)
            assertThat(character.image)
                .isEqualTo("https://rickandmortyapi.com/api/character/avatar/1.jpeg")
            assertThat(character.url)
                .isEqualTo("https://rickandmortyapi.com/api/character/1")
        }

    @Test
    fun `Given second page request Then datasource should returns second page response`() =
        runTest {

            val result = sut.characters(2)

            assertThat(result.size).isEqualTo(1)
            assertThat(result.currentIndex).isEqualTo(2)
            assertThat(result.prevPageIndex).isNull()
            assertThat(result.nextPageIndex).isEqualTo(3)
            val character = result.getItem(0)
            assertThat(character.id).isEqualTo(2)
            assertThat(character.name).isEqualTo("Morty Smith")
            assertThat(character.status).isEqualTo(Status.Dead)
            assertThat(character.species).isEqualTo("Human")
            assertThat(character.gender).isEqualTo(Gender.Female)
            assertThat(character.image)
                .isEqualTo("https://rickandmortyapi.com/api/character/avatar/2.jpeg")
            assertThat(character.url)
                .isEqualTo("https://rickandmortyapi.com/api/character/2")
        }

    @Test(expected = Error.CharactersNotRetrieved::class)
    fun `Given not found server error Then datasource should return NotFoundCharacters error`() {
        runTest {
            serverRule.setResponseStatus(ResponseStatus.NotFound)
            sut.characters(0)
        }
    }

    @Test(expected = Error.CharactersNotRetrieved::class)
    fun `Given server error Then datasource should return NotFoundCharacters error`() {
        runTest {
            serverRule.setResponseStatus(ResponseStatus.ServerNotFound)
            sut.characters(0)
        }
    }
}
