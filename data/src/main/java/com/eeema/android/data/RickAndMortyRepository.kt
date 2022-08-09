package com.eeema.android.data

import com.eeema.android.data.datasource.local.CharactersDao
import com.eeema.android.data.datasource.local.converters.CharacterConverter
import com.eeema.android.data.datasource.remote.CharactersDataSource
import com.eeema.android.data.model.Character
import com.eeema.android.data.model.Page
import javax.inject.Inject

class RickAndMortyRepository @Inject constructor(
    private val network: CharactersDataSource,
    private val local: CharactersDao,
    private val converter: CharacterConverter
) : Repository {

    override suspend fun characters(pageIndex: Int): Result<Page<Character>> {
        val localData = local.characters(if (pageIndex < 1) 1 else pageIndex)
        return if (localData.isNotEmpty()) {
            Result.success(converter.dbCharacterToPage(localData))
        } else {
            requestDataFromServer(pageIndex)
        }
    }

    private suspend fun requestDataFromServer(pageIndex: Int?): Result<Page<Character>> {
        return runCatching {
            val characters = network.characters(pageIndex).also {
                val newLocalData = converter.pageToDbCharacter(it)
                local.insertCharacters(newLocalData)
            }
            Result.success(characters)
        }.getOrElse { Result.failure(it) }
    }
}
