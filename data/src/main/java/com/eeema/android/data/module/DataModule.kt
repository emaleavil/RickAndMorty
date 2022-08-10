package com.eeema.android.data.module

import android.content.Context
import androidx.room.Room
import com.eeema.android.data.BuildConfig
import com.eeema.android.data.Repository
import com.eeema.android.data.RickAndMortyRepository
import com.eeema.android.data.api.RickAndMortyApi
import com.eeema.android.data.datasource.local.CharactersDao
import com.eeema.android.data.datasource.local.RickAndMortyDatabase
import com.eeema.android.data.datasource.remote.CharactersDataSource
import com.eeema.android.data.model.Character
import com.eeema.android.data.model.Page
import com.eeema.android.data.model.deserializer.CharacterDeserializer
import com.eeema.android.data.model.deserializer.PageDeserializer
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Provides
    fun provideRepository(
        repository: RickAndMortyRepository
    ): Repository = repository

    @Provides
    fun provideNetworkDatasource(
        api: RickAndMortyApi
    ): CharactersDataSource = CharactersDataSource(api)

    @Provides
    fun provideNetworkApi(): RickAndMortyApi {

        val client = OkHttpClient.Builder().apply {
            if (BuildConfig.DEBUG) {
                addInterceptor(
                    HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
                )
            }
        }.build()

        val gson = GsonBuilder()
            .registerTypeAdapter(
                TypeToken.getParameterized(
                    TypeToken.get(Page::class.java).type,
                    TypeToken.get(Character::class.java).type
                ).type,
                PageDeserializer(Character::class.java)
            )
            .registerTypeAdapter(
                TypeToken.get(Character::class.java).type,
                CharacterDeserializer()
            )
            .create()

        return Retrofit.Builder()
            .baseUrl("https://rickandmortyapi.com/api/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client)
            .build()
            .create(RickAndMortyApi::class.java)
    }

    @Provides
    fun provideAppDatabase(@ApplicationContext appContext: Context): RickAndMortyDatabase {
        return Room.databaseBuilder(
            appContext,
            RickAndMortyDatabase::class.java,
            "RickAndMortyDB"
        ).build()
    }

    @Provides
    fun provideDao(db: RickAndMortyDatabase): CharactersDao = db.charactersDao()
}
