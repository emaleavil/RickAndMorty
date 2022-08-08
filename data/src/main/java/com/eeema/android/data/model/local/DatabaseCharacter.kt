package com.eeema.android.data.model.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "characters"
)
class DatabaseCharacter(
    @PrimaryKey val id: Int,
    val name: String,
    val status: String,
    val species: String,
    val gender: String,
    val image: String,
    val url: String,
    @ColumnInfo(name = "page_id") val fromPage: Int,
    @ColumnInfo(name = "prev_page_id") val prevPageIndex: Int?,
    @ColumnInfo(name = "next_page_id") val nextPageIndex: Int?
)
