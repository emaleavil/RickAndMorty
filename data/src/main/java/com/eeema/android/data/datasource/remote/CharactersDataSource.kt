package com.eeema.android.data.datasource.remote

import com.eeema.android.data.api.RickAndMortyApi
import com.eeema.android.data.model.Character
import com.eeema.android.data.model.Page
import com.eeema.android.data.model.error.Error
import javax.inject.Inject

class CharactersDataSource @Inject constructor(
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
