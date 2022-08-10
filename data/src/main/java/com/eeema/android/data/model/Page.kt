package com.eeema.android.data.model

data class Page<T>(
    val currentIndex: Int = 1,
    val nextPageIndex: Int? = null,
    val prevPageIndex: Int? = null,
    val data: List<T>
) {
    val size: Int
        get() = data.size

    fun getItem(at: Int): T {
        return runCatching { data[at] }.getOrThrow()
    }
}
