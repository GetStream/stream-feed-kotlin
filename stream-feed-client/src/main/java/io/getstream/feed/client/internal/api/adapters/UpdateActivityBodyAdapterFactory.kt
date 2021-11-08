package io.getstream.feed.client.internal.api.adapters

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import com.squareup.moshi.Moshi
import io.getstream.feed.client.internal.api.models.UpdateActivityByForeignIdRequest
import io.getstream.feed.client.internal.api.models.UpdateActivityByIdRequest
import io.getstream.feed.client.internal.api.models.UpdateActivityRequest
import java.lang.reflect.Type

internal object UpdateActivityBodyAdapterFactory : JsonAdapter.Factory {
    override fun create(type: Type, annotations: MutableSet<out Annotation>, moshi: Moshi): JsonAdapter<*>? =
        when (type) {
            UpdateActivityRequest::class.java -> UpdateActivityBodyAdapter(moshi)
            else -> null
        }

    private class UpdateActivityBodyAdapter(private val moshi: Moshi) : JsonAdapter<UpdateActivityRequest>() {
        override fun fromJson(reader: JsonReader): UpdateActivityRequest? {
            reader.readJsonValue()
            return null
        }

        override fun toJson(writer: JsonWriter, value: UpdateActivityRequest?) {
            when (value) {
                is UpdateActivityByForeignIdRequest -> moshi.adapter(UpdateActivityByForeignIdRequest::class.java).toJson(writer, value)
                is UpdateActivityByIdRequest -> moshi.adapter(UpdateActivityByIdRequest::class.java).toJson(writer, value)
            }
        }
    }
}
