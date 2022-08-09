package com.eeema.android.data.datasource.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.eeema.android.data.model.local.DatabaseCharacter

@Dao
interface CharactersDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCharacters(characters: List<DatabaseCharacter>)

    @Query("SELECT * FROM characters WHERE page_id = :pageId")
    suspend fun characters(pageId: Int): List<DatabaseCharacter>

    @Query("SELECT * FROM characters WHERE id = :id")
    suspend fun character(id: Int): DatabaseCharacter
}
