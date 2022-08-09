@file:OptIn(ExperimentalCoroutinesApi::class)

package com.eeema.android.data

import com.eeema.android.data.api.RickAndMortyApi
import com.eeema.android.data.datasource.local.CharactersDao
import com.eeema.android.data.datasource.local.converters.CharacterConverter
import com.eeema.android.data.datasource.remote.CharactersDataSource
import com.eeema.android.data.model.Character
import com.eeema.android.data.model.Gender
import com.eeema.android.data.model.Page
import com.eeema.android.data.model.Status
import com.eeema.android.data.model.deserializer.CharacterDeserializer
import com.eeema.android.data.model.deserializer.PageDeserializer
import com.eeema.android.data.model.local.DatabaseCharacter
import com.eeema.android.data.server.MockWebServerRule
import com.eeema.android.data.server.ResponseStatus
import com.eeema.android.data.server.RetrofitRule
import com.eeema.android.data.server.RickAndMortyDispatcher
import com.google.common.truth.Truth.assertThat
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Rule
import org.junit.Test

class RickAndMortyRepositoryTest {

    @get:Rule
    val serverRule = MockWebServerRule(RickAndMortyDispatcher())
    @get:Rule
    val retrofitRule = RetrofitRule(
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

    private val networkDatasource: CharactersDataSource = CharactersDataSource(retrofitRule.service)

    private val fakeDao = object : CharactersDao {

        val data = mutableListOf<DatabaseCharacter>()

        override suspend fun insertCharacters(characters: List<DatabaseCharacter>) {
            data.addAll(characters)
        }

        override suspend fun characters(pageId: Int): List<DatabaseCharacter> {
            return data.filter { it.fromPage == pageId }
        }
    }

    private val sut = RickAndMortyRepository(
        networkDatasource,
        fakeDao,
        CharacterConverter()
    )

    @After
    fun tearDown() {
        fakeDao.data.clear()
    }

    @Test
    fun `Request characters should return data from network when database is empty`() = runTest {
        assertThat(fakeDao.data).isEmpty()

        val result = sut.characters().getOrNull()

        assertThat(result).isNotNull()
        assertThat(result!!.data).isNotEmpty()
    }

    @Test
    fun `Request characters should store data in local storage`() = runTest {
        assertThat(fakeDao.data).isEmpty()

        sut.characters()

        assertThat(fakeDao.data).isNotEmpty()
    }

    @Test
    fun `Request new characters page should store new data in database`() = runTest {
        assertThat(fakeDao.data).isEmpty()

        sut.characters()

        assertThat(fakeDao.data).hasSize(1)

        sut.characters(2)

        assertThat(fakeDao.data).hasSize(2)
    }

    @Test
    fun `When no local data is stored and server gets error Then Result should be failure`() =
        runTest {
            serverRule.setResponseStatus(ResponseStatus.NotFound)

            val result = sut.characters()

            assertThat(result.isFailure).isTrue()
        }

    @Test
    fun `Given data from local should be successfully converted`() = runTest {
        assertThat(fakeDao.data).isEmpty()

        sut.characters()

        assertThat(fakeDao.data).hasSize(1)

        val page = sut.characters(1).getOrNull()
        assertThat(page).isNotNull()
        val character = page!!.data.first()
        assertThat(fakeDao.data).hasSize(1)
        assertThat(page.currentIndex).isEqualTo(1)
        assertThat(page.prevPageIndex).isEqualTo(null)
        assertThat(page.nextPageIndex).isEqualTo(2)
        assertThat(character.id).isEqualTo(1)
        assertThat(character.name).isEqualTo("Rick Sanchez")
        assertThat(character.status).isEqualTo(Status.Alived)
        assertThat(character.gender).isEqualTo(Gender.Male)
        assertThat(character.species).isEqualTo("Human")
        assertThat(character.url)
            .isEqualTo("https://rickandmortyapi.com/api/character/1")
        assertThat(character.image)
            .isEqualTo("https://rickandmortyapi.com/api/character/avatar/1.jpeg")
    }
}
