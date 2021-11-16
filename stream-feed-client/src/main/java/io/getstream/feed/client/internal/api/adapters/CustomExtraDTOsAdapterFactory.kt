package io.getstream.feed.client.internal.api.adapters

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.rawType
import io.getstream.feed.client.internal.api.models.CustomExtraDTOs
import java.lang.reflect.Type

object CustomExtraDTOsAdapterFactory : JsonAdapter.Factory {
    override fun create(type: Type, annotations: MutableSet<out Annotation>, moshi: Moshi): JsonAdapter<*>? =
        when {
            CustomExtraDTOs::class.java.isAssignableFrom(type.rawType) ->
                CustomExtraDTOsJsonAdapter(
                    moshi.mapAdapter,
                    moshi.nextAdapter(this, type, annotations),
                )
            else -> null
        }

    private class CustomExtraDTOsJsonAdapter(
        private val mapAdapter: JsonAdapter<MutableMap<String, Any?>>,
        private val delegateJsonAdapter: JsonAdapter<Any>,
    ) : JsonAdapter<CustomExtraDTOs>() {
        override fun fromJson(reader: JsonReader): CustomExtraDTOs? =
            mapAdapter.fromJson(reader)?.let {
                val notNullValuesMap: Map<String, Any> = it.mapNotNull { it.value?.let { value -> it.key to value } }.toMap()
                (delegateJsonAdapter.fromJsonValue(notNullValuesMap) as CustomExtraDTOs?)?.apply {
                    val keysToRemove: MutableSet<String> = mapAdapter.fromJson(delegateJsonAdapter.toJson(this))?.keys ?: mutableSetOf()
                    extraData.putAll(notNullValuesMap - keysToRemove)
                }
            }

        override fun toJson(writer: JsonWriter, value: CustomExtraDTOs?) =
            mapAdapter.toJson(
                writer,
                mapAdapter.fromJson(
                    delegateJsonAdapter.toJson(value)
                )?.apply {
                    remove("extraData")
                    putAll(value?.extraData ?: emptyMap())
                }
            )
    }

    private val Moshi.mapAdapter: JsonAdapter<MutableMap<String, Any?>>
        get() = this.adapter(Types.newParameterizedType(Map::class.java, String::class.java, Any::class.java))
}
