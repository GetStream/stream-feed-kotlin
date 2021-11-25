package io.getstream.feed.client.internal.api.adapters

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.rawType
import io.getstream.feed.client.internal.api.models.DataDto
import java.lang.reflect.Type

object DataDtoAdapterFactory : JsonAdapter.Factory {
    override fun create(type: Type, annotations: MutableSet<out Annotation>, moshi: Moshi): JsonAdapter<*>? =
        when {
            DataDto::class.java.isAssignableFrom(type.rawType) ->
                DataDtoJsonAdapter(
                    moshi.stringAdapter,
                    moshi.mapAdapter,
                )
            else -> null
        }

    private class DataDtoJsonAdapter(
        private val stringAdapter: JsonAdapter<String>,
        private val mapAdapter: JsonAdapter<MutableMap<String, Any?>>,
    ) : JsonAdapter<DataDto>() {

        @Suppress("UNCHECKED_CAST")
        override fun fromJson(reader: JsonReader): DataDto? {
            val mapValues = try {
                mapAdapter.fromJson(reader)?.apply {
                    put(
                        "data",
                        (get("data") as? Map<String, Any?> ?: emptyMap())
                            .mapNotNull { it.value?.let { value -> it.key to value } }
                            .toMap()
                    )
                } ?: emptyMap<String, Any>()
            } catch (e: Exception) {
                mapOf(
                    "id" to stringAdapter.fromJson(reader),
                    "data" to emptyMap<String, Any>(),
                )
            }
            return DataDto(
                id = mapValues["id"] as String,
                data = mapValues["data"] as Map<String, Any>,
            )
        }

        override fun toJson(writer: JsonWriter, value: DataDto?) {
            stringAdapter.toJson(writer, value?.id)
        }
    }

    private val Moshi.stringAdapter: JsonAdapter<String>
        get() = this.adapter(String::class.java)

    private val Moshi.mapAdapter: JsonAdapter<MutableMap<String, Any?>>
        get() = this.adapter(Types.newParameterizedType(Map::class.java, String::class.java, Any::class.java))
}
