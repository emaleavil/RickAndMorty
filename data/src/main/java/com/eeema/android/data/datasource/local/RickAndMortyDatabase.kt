package com.eeema.android.data.datasource.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.eeema.android.data.model.local.DatabaseCharacter

@Database(entities = [DatabaseCharacter::class], version = 1)
abstract class RickAndMortyDatabase : RoomDatabase() {
    abstract fun charactersDao(): CharactersDao
}
