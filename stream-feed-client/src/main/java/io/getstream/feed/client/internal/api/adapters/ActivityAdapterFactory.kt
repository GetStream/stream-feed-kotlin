package io.getstream.feed.client.internal.api.adapters

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.internal.Util
import io.getstream.feed.client.internal.api.models.ActivitySealedDto
import io.getstream.feed.client.internal.api.models.DownstreamActivityDto
import io.getstream.feed.client.internal.api.models.DownstreamActivitySealedDto
import io.getstream.feed.client.internal.api.models.DownstreamEnrichActivityDto
import io.getstream.feed.client.internal.api.models.UpstreamActivityDto
import io.getstream.feed.client.internal.api.models.UpstreamActivitySealedDto
import io.getstream.feed.client.internal.api.models.UpstreamEnrichActivityDto
import java.lang.reflect.Type

object ActivityAdapterFactory : JsonAdapter.Factory {
    override fun create(type: Type, annotations: MutableSet<out Annotation>, moshi: Moshi): JsonAdapter<*>? =
        when (type) {
            ActivitySealedDto::class.java -> ActivitySealedDtoAdapter(
                DownstreamActivitySealedDtoAdapter(moshi),
                UpstreamActivitySealedDtoAdapter(moshi),
            )
            UpstreamActivitySealedDto::class.java -> UpstreamActivitySealedDtoAdapter(moshi)
            DownstreamActivitySealedDto::class.java -> DownstreamActivitySealedDtoAdapter(moshi)
            else -> null
        }

    private class DownstreamActivitySealedDtoAdapter(moshi: Moshi) : Adapter<DownstreamActivitySealedDto>(moshi) {

        override fun fromJson(reader: JsonReader): DownstreamActivitySealedDto? {
            val mapValues = mapAdapter.fromJson(reader)
            val actor = mapValues?.get("actor") ?: throw Util.missingProperty("actor", "actor", reader)
            return when (actor) {
                is String -> downstreamActivityDtoAdapter.fromJsonValue(mapValues)
                else -> downstreamEnrichActivityDtoAdapter.fromJsonValue(mapValues)
            }
        }

        override fun toJson(writer: JsonWriter, value: DownstreamActivitySealedDto?) {
            when (value) {
                is DownstreamActivityDto -> downstreamActivityDtoAdapter.toJson(writer, value)
                is DownstreamEnrichActivityDto -> downstreamEnrichActivityDtoAdapter.toJson(writer, value)
            }
        }
    }

    private class UpstreamActivitySealedDtoAdapter(moshi: Moshi) : Adapter<UpstreamActivitySealedDto>(moshi) {

        override fun fromJson(reader: JsonReader): UpstreamActivitySealedDto? {
            reader.readJsonValue()
            return null
        }

        override fun toJson(writer: JsonWriter, value: UpstreamActivitySealedDto?) {
            when (value) {
                is UpstreamActivityDto -> upstreamActivityDtoAdapter.toJson(writer, value)
                is UpstreamEnrichActivityDto -> upstreamEnrichActivityDtoAdapter.toJson(writer, value)
            }
        }
    }

    private class ActivitySealedDtoAdapter(
        private val downstreamActivitySealedDtoAdapter: DownstreamActivitySealedDtoAdapter,
        private val upstreamActivitySealedDtoAdapter: UpstreamActivitySealedDtoAdapter
    ) : JsonAdapter<ActivitySealedDto>() {

        override fun fromJson(reader: JsonReader): ActivitySealedDto? =
            downstreamActivitySealedDtoAdapter.fromJson(reader)

        override fun toJson(writer: JsonWriter, value: ActivitySealedDto?) =
            when (value) {
                is DownstreamActivitySealedDto -> downstreamActivitySealedDtoAdapter.toJson(writer, value)
                is UpstreamActivitySealedDto -> upstreamActivitySealedDtoAdapter.toJson(writer, value)
                null -> { }
            }
    }

    private abstract class Adapter<T>(val moshi: Moshi) : JsonAdapter<T>() {
        val mapAdapter: JsonAdapter<MutableMap<String, Any?>> by lazy {
            moshi.adapter(Types.newParameterizedType(Map::class.java, String::class.java, Any::class.java))
        }
        val downstreamActivityDtoAdapter: JsonAdapter<DownstreamActivityDto> by lazy {
            moshi.adapter(DownstreamActivityDto::class.java)
        }
        val downstreamEnrichActivityDtoAdapter: JsonAdapter<DownstreamEnrichActivityDto> by lazy {
            moshi.adapter(DownstreamEnrichActivityDto::class.java)
        }
        val upstreamActivityDtoAdapter: JsonAdapter<UpstreamActivityDto> by lazy {
            moshi.adapter(UpstreamActivityDto::class.java)
        }
        val upstreamEnrichActivityDtoAdapter: JsonAdapter<UpstreamEnrichActivityDto> by lazy {
            moshi.adapter(UpstreamEnrichActivityDto::class.java)
        }
    }
}
