package com.eeema.android.data.datasource

import com.eeema.android.data.api.RickAndMortyApi
import com.eeema.android.data.model.Character
import com.eeema.android.data.model.Page
import com.eeema.android.data.model.error.Error

class CharactersDataSource(
    private val api: RickAndMortyApi
) {

    suspend fun characters(
        page: Int? = null
    ): Page<Character> {
        return runCatching {
            api.characters(page)
        }.getOrElse { throw Error.CharactersNotRetrieved }
    }
}
