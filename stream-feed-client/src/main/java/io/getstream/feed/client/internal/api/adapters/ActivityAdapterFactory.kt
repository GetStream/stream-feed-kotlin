package io.getstream.feed.client.internal.api.adapters

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import com.squareup.moshi.Moshi
import io.getstream.feed.client.internal.api.models.ActivitySealedDto
import io.getstream.feed.client.internal.api.models.DownstreamActivityDto
import io.getstream.feed.client.internal.api.models.UpstreamActivityDto
import java.lang.reflect.Type

/**
 * A [JsonAdapter.Factory] which provide [JsonAdapter] to serialize/deserialize [ActivitySealedDto] entities.
 */
internal object ActivityAdapterFactory : JsonAdapter.Factory {
    override fun create(type: Type, annotations: MutableSet<out Annotation>, moshi: Moshi): JsonAdapter<*>? =
        when (type) {
            ActivitySealedDto::class.java -> ActivitySealedDtoAdapter(
                moshi.downstreamActivityDtoAdapter,
                moshi.adapter(UpstreamActivityDto::class.java),
            )
            else -> null
        }

    /**
     * A [JsonAdapter] to serialize/deserialize [ActivitySealedDto] entities.
     *
     * @property downstreamActivitySealedDtoAdapter used to serialize/deserialize [DownstreamActivityDto] entities.
     * @property upstreamActivityDtoAdapter used to serialize/deserialize [UpstreamActivityDto] entities.
     */
    private class ActivitySealedDtoAdapter(
        private val downstreamActivitySealedDtoAdapter: JsonAdapter<DownstreamActivityDto>,
        private val upstreamActivityDtoAdapter: JsonAdapter<UpstreamActivityDto>
    ) : JsonAdapter<ActivitySealedDto>() {

        override fun fromJson(reader: JsonReader): ActivitySealedDto? =
            downstreamActivitySealedDtoAdapter.fromJson(reader)

        override fun toJson(writer: JsonWriter, value: ActivitySealedDto?) =
            when (value) {
                is DownstreamActivityDto -> downstreamActivitySealedDtoAdapter.toJson(writer, value)
                is UpstreamActivityDto -> upstreamActivityDtoAdapter.toJson(writer, value)
                null -> { }
            }
    }

    private val Moshi.downstreamActivityDtoAdapter: JsonAdapter<DownstreamActivityDto>
        get() = this.adapter(DownstreamActivityDto::class.java)
}
