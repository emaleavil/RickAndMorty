package com.eeema.android.data

import com.eeema.android.data.model.Character
import com.eeema.android.data.model.Page

interface Repository {
    suspend fun characters(pageIndex: Int = 1): Result<Page<Character>>
}
