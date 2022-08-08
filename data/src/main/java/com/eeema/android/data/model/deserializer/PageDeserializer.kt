package com.eeema.android.data.model.deserializer

import com.eeema.android.data.constants.JsonKeys
import com.eeema.android.data.extensions.getJsonArray
import com.eeema.android.data.extensions.getJsonObject
import com.eeema.android.data.extensions.stringOrNull
import com.eeema.android.data.model.Character
import com.eeema.android.data.model.Page
import com.google.gson.JsonArray
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type
import java.util.regex.Pattern

class PageDeserializer<T>(
    private val clazz: Class<T>
) : JsonDeserializer<Page<Character>> {

    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): Page<Character> {

        val pageNode = json.asJsonObject
        val infoNode = pageNode.getJsonObject(JsonKeys.info)
        val resultsNode = pageNode.getJsonArray(JsonKeys.results)

        return Page(
            infoNode.stringOrNull(JsonKeys.next).extractIndex(),
            infoNode.stringOrNull(JsonKeys.prev).extractIndex(),
            resultsNode?.transformToList(context) ?: emptyList()
        )
    }

    private fun <T> JsonArray.transformToList(
        context: JsonDeserializationContext
    ): List<T> = try {
        this.map<JsonElement?, T> { context.deserialize(it, clazz) }
    } catch (e: Throwable) {
        emptyList()
    }

    private fun String?.extractIndex(): Int? {
        return this?.let {
            val regex = ".*page=(\\d+).*"
            val pattern = Pattern.compile(regex)
            val matcher = pattern.matcher(it)
            if (matcher.matches()) {
                matcher.group(1)?.toInt()
            } else null
        }
    }
}
