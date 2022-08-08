package com.eeema.android.data.api

import com.eeema.android.data.model.Character
import com.eeema.android.data.model.Page
import retrofit2.http.GET
import retrofit2.http.Query

interface RickAndMortyApi {

    @GET("characters")
    suspend fun characters(
        @Query("page") page: Int? = null
    ): Page<Character>
}
