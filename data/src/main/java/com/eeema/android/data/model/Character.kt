package com.eeema.android.data.model

data class Character(
    val id: Int,
    val name: String,
    val status: Status,
    val species: String,
    val gender: Gender,
    val image: String,
    val url: String
)
