package com.eeema.android.data.datasource.local.converters

import com.eeema.android.data.extensions.stringGender
import com.eeema.android.data.extensions.stringStatus
import com.eeema.android.data.extensions.toGender
import com.eeema.android.data.extensions.toStatus
import com.eeema.android.data.model.Character
import com.eeema.android.data.model.Page
import com.eeema.android.data.model.local.DatabaseCharacter

class CharacterConverter {

    fun dbCharacterToPage(dbCharacters: List<DatabaseCharacter>): Page<Character> {
        val characters = dbCharacters.map { dbCharacter ->
            Character(
                dbCharacter.id,
                dbCharacter.name,
                dbCharacter.toStatus(),
                dbCharacter.species,
                dbCharacter.toGender(),
                dbCharacter.image,
                dbCharacter.url
            )
        }

        val firstCharacter = dbCharacters.first()
        return Page(
            firstCharacter.fromPage,
            firstCharacter.nextPageIndex,
            firstCharacter.prevPageIndex,
            characters
        )
    }

    fun pageToDbCharacter(page: Page<Character>): List<DatabaseCharacter> {
        return page.data.map { character ->
            DatabaseCharacter(
                character.id,
                character.name,
                character.stringStatus(),
                character.species,
                character.stringGender(),
                character.image,
                character.url,
                page.currentIndex,
                page.prevPageIndex,
                page.nextPageIndex
            )
        }
    }
}
