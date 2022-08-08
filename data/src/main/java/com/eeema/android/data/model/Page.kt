package com.eeema.android.data.model

data class Page<T>(
    val nextPageUrl: String? = null,
    val prevPageUrl: String? = null,
    val data: List<T>
) {
    val size: Int
        get() = data.size

    fun getItem(at: Int): T {
        return runCatching { data[at] }.getOrThrow()
    }
}
